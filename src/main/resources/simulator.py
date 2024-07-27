import random
import time
from datetime import datetime, timezone
import neurokit2 as nk
import psycopg2
import matplotlib.pyplot as plt
import websocket
import json
import requests

previous_values = {}

def write_to_timescaledb(patient_id, vitals):
    try:
        connection = psycopg2.connect(
            user="postgres",
            password="metallica",
            host="localhost",
            port="5432",
            database="postgres"
        )
        cursor = connection.cursor()

        insert_query = """
        INSERT INTO vitals (patient_id, temperature, pulse, respiration_rate, systolic, diastolic, ecg_string, time, saturation)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        record_to_insert = (
            patient_id,
            vitals['fields']['temperature'],
            vitals['fields']['pulse'],
            vitals['fields']['respiration_rate'],
            vitals['fields']['systolic'],
            vitals['fields']['diastolic'],
            vitals['fields']['ecg_string'][:200],
            vitals['time'].strftime("%Y-%m-%d %H:%M:%S"),
            vitals['fields']['saturation']
        )
        cursor.execute(insert_query, record_to_insert)
        connection.commit()
        count = cursor.rowcount
        print(f"Record {record_to_insert} inserted successfully into vitals table")
        try:
            send_websocket_message(record_to_insert)
        except Exception as e:
            print(f"Error during WebSocket communication: {e}")
    except (Exception, psycopg2.Error) as error:
        print("Failed to insert record into vitals table", error)
    finally:
        if connection:
            cursor.close()
            connection.close()

def send_websocket_message(vital_record):
    try:
        url = "http://localhost:8080/vitals"
        data = {
            "patientId": vital_record[0],
            "temperature": vital_record[1],
            "pulse": vital_record[2],
            "respirationRate": vital_record[3],
            "systolic": vital_record[4],
            "diastolic": vital_record[5],
            "ecgString": vital_record[6],
            "saturation": vital_record[8]
        }
        response = requests.post(url, json=data)
        if response.status_code == 200:
            print('Successfully sent vital record to server')
        else:
            print(f'Failed to send vital record. Status code: {response.status_code}')
    except Exception as e:
        print(f"Failed to send vital record: {e}")

def get_patients():
    try:
        connection = psycopg2.connect(
            user="postgres",
            password="metallica",
            host="127.0.0.1",
            port="5432",
            database="postgres"
        )
        cursor = connection.cursor()

        query = "SELECT id, age, active FROM patients"
        cursor.execute(query)
        patients = cursor.fetchall()

        return patients
    except (Exception, psycopg2.Error) as error:
        print("Failed to fetch patients", error)
    finally:
        if connection:
            cursor.close()
            connection.close()

def simulate_vitals():
    while True:
        patients = get_patients()
        for patient in patients:
            patient_id, age, active = patient
            if active:
                print(f'patient id: {patient_id}, active {active}, age: {age}')
                vitals = generate_vitals(age)
                print(f'vitals: {vitals}')
                write_to_timescaledb(patient_id, vitals)
        time.sleep(10)

def initialize_values(age):
    # Initialize values
    global previous_values
    previous_values = {
        "temperature": initialize_temperature(),
        "pulse": initialize_pulse(age),
        "respiration_rate": initialize_respiration_rate(age),
        "blood_pressure": initialize_blood_pressure(),
        "ecg": initialize_ecg(),
        "saturation": initialize_saturation(age)
    }


# Functions for initializing values when first instancing a patient
def initialize_temperature():
    # Normal human body temperature in Celsius
    if random.random() < 0.8:
        body_temperature = round(random.uniform(36.1, 37.5), 1)
    else:
        body_temperature = round(random.uniform(35.1, 40.0), 1)
    return body_temperature


def initialize_pulse(age):
    # Normal resting pulse for adults ranges from 60 to 100 beats per minute
    if random.random() < 0.8:
        if age >= 18:
            pulse_rate = random.randint(60, 100)
        elif 6 <= age < 17:
            pulse_rate = random.randint(70, 100)
        elif 2 <= age < 6:
            pulse_rate = random.randint(80, 130)
        else:
            pulse_rate = random.randint(80, 160)
    else:
        if age >= 18:
            pulse_rate = random.randint(30, 200)
        elif 6 <= age < 17:
            pulse_rate = random.randint(40, 170)
        elif 2 <= age < 6:
            pulse_rate = random.randint(50, 220)
        else:
            pulse_rate = random.randint(50, 240)
    return pulse_rate


def initialize_respiration_rate(age):
    # Normal respiration rate for adults ranges from 12 to 16 breaths per minute
    if random.random() < 0.8:
        if age >= 18:
            respiration_rate = random.randint(12, 20)
        elif 6 <= age < 17:
            respiration_rate = random.randint(18, 30)
        else:
            respiration_rate = random.randint(20, 40)
    else:
        if age >= 18:
            respiration_rate = random.randint(6, 40)
        elif 6 <= age < 17:
            respiration_rate = random.randint(8, 50)
        else:
            respiration_rate = random.randint(10, 60)
    return respiration_rate


def initialize_blood_pressure():
    # Normal systolic/diastolic pressure in mmHg
    if random.random() < 0.8:
        systolic = random.randint(90, 120)
        diastolic = random.randint(60, 80)
    else:
        systolic = random.randint(60, 220)
        diastolic = random.randint(40, 120)
    return systolic, diastolic


def initialize_ecg():
    # ECG value generator which takes into the current heart rate
    ecg = nk.ecg_simulate(duration=8, sampling_rate=1000, heart_rate=80)
    return ecg

def initialize_saturation(age):
    # Normal resting saturation levels for healthy individuals
    if random.random() < 0.8:
        if age >= 18:
            saturation = random.randint(95, 100)
        elif 6 <= age < 17:
            saturation = random.randint(95, 100)
        elif 2 <= age < 6:
            saturation = random.randint(95, 100)
        else:
            saturation = random.randint(95, 100)
    else:
        if age >= 18:
            saturation = random.randint(80, 100)
        elif 6 <= age < 17:
            saturation = random.randint(80, 100)
        elif 2 <= age < 6:
            saturation = random.randint(80, 100)
        else:
            saturation = random.randint(80, 100)
    return saturation



def get_age_based_value(age, adult_min, adult_max, non_adult_min, non_adult_max):
    if age >= 18:
        return random.randint(adult_min, adult_max)
    else:
        return random.randint(non_adult_min, non_adult_max)


# Functions for updating values based on previous values
def update_temperature(previous_temp):
    # Use the previous temperature to slightly alter the new one
    delta = random.uniform(-0.1, 0.1)  # Small change to simulate real temperature fluctuation
    new_temp = round(previous_temp + delta, 1)
    return new_temp


def update_pulse(previous_pulse, age):
    # Pulse change is small unless there is a significant event
    delta = random.randint(-1, 1)
    new_pulse = previous_pulse + delta
    # Keep pulse within reasonable boundaries based on age
    if age >= 18:
        new_pulse = max(60, min(new_pulse, 100))
    elif 6 <= age < 17:
        new_pulse = max(70, min(new_pulse, 100))
    elif 2 <= age < 6:
        new_pulse = max(80, min(new_pulse, 130))
    else:
        new_pulse = max(80, min(new_pulse, 160))
    return new_pulse


def update_respiration_rate(previous_rate, age):
    delta = random.randint(-1, 1)
    new_rate = previous_rate + delta
    # Adjust rates for age groups
    if age >= 18:
        new_rate = max(12, min(new_rate, 20))
    elif 6 <= age < 17:
        new_rate = max(18, min(new_rate, 30))
    else:
        new_rate = max(20, min(new_rate, 40))
    return new_rate


def update_blood_pressure(previous_bp):
    delta_systolic = random.randint(-1, 1)
    delta_diastolic = random.randint(-1, 1)
    new_systolic = max(90, min(previous_bp[0] + delta_systolic, 120))
    new_diastolic = max(60, min(previous_bp[1] + delta_diastolic, 80))
    return new_systolic, new_diastolic


def update_ecg(pulse):
    ecg = nk.ecg_simulate(duration=8, sampling_rate=1000, heart_rate=pulse)
    # Save ECG plot
    plot_filename = f'ecg_plot_{datetime.now(timezone.utc).strftime("%Y%m%d%H%M%S")}.png'
    nk.signal_plot(ecg, labels=["ecg"])
    # plt.savefig(plot_filename)
    plt.close()
    return ecg

def update_saturation(previous_saturation):
    delta_saturation = random.randint(-1, 1)
    new_saturation = max(90, min(previous_saturation + delta_saturation, 100))
    return new_saturation

def generate_vitals(age):
    global previous_values
    if not previous_values:
        initialize_values(age)
    else:
        previous_values["temperature"] = update_temperature(previous_values["temperature"])
        previous_values["pulse"] = update_pulse(previous_values["pulse"], age)
        previous_values["respiration_rate"] = update_respiration_rate(previous_values["respiration_rate"], age)
        previous_values["blood_pressure"] = update_blood_pressure(previous_values["blood_pressure"])
        previous_values["saturation"] = update_saturation(previous_values["saturation"])
        previous_values["ecg"] = update_ecg(previous_values["pulse"])


    ecg_string = ','.join(map(str, previous_values["ecg"]))

    return {
        "measurement": "patient1",
        "fields": {
            'temperature': previous_values["temperature"],
            'pulse': previous_values["pulse"],
            'respiration_rate': previous_values["respiration_rate"],
            'systolic': previous_values["blood_pressure"][0],
            'diastolic': previous_values["blood_pressure"][1],
            'saturation': previous_values["saturation"],
            'ecg_string': ecg_string
        },
        'time': datetime.now(timezone.utc)
    }

if __name__ == "__main__":
    simulate_vitals()
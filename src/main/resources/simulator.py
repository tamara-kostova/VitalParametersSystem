import random
import time
from datetime import datetime, timezone
import numpy as np
import psycopg2
import requests
import threading

class Patient:
    def __init__(self, patient_id, age):
        self.patient_id = patient_id
        self.age = age
        self.initialize_vitals()

    def initialize_vitals(self):
        self.temperature = self.initialize_temperature()
        self.pulse = self.initialize_pulse()
        self.respiration_rate = self.initialize_respiration_rate()
        self.blood_pressure = self.initialize_blood_pressure()
        self.saturation = self.initialize_saturation()

    def initialize_temperature(self):
        return random.uniform(36.1, 37.5)

    def initialize_pulse(self):
        if self.age >= 18:
            return random.randint(60, 100)
        elif 12 <= self.age < 18:
            return random.randint(60, 110)
        elif 6 <= self.age < 12:
            return random.randint(70, 120)
        elif 1 <= self.age < 6:
            return random.randint(80, 130)
        else:
            return random.randint(100, 160)

    def initialize_respiration_rate(self):
        if self.age >= 18:
            return random.randint(12, 20)
        elif 6 <= self.age < 18:
            return random.randint(18, 30)
        else:
            return random.randint(20, 40)

    def initialize_blood_pressure(self):
        if self.age >= 18:
            systolic = random.randint(90, 120)
            diastolic = random.randint(60, 80)
        elif 12 <= self.age < 18:
            systolic = random.randint(90, 120)
            diastolic = random.randint(50, 80)
        else:
            systolic = random.randint(80, 110)
            diastolic = random.randint(50, 70)
        return systolic, diastolic

    def initialize_saturation(self):
        return random.randint(95, 100)

    def update_vitals(self):
        self.temperature = self.update_temperature()
        self.pulse = self.update_pulse()
        self.respiration_rate = self.update_respiration_rate()
        self.blood_pressure = self.update_blood_pressure()
        self.saturation = self.update_saturation()

    def update_temperature(self):
        change = random.gauss(0, 0.1)
        new_temp = self.temperature + change
        return max(35.0, min(new_temp, 41.0))

    def update_pulse(self):
        change = random.gauss(0, 2)
        new_pulse = self.pulse + change
        return max(40, min(new_pulse, 200))

    def update_respiration_rate(self):
        change = random.gauss(0, 1)
        new_rate = self.respiration_rate + change
        return max(8, min(new_rate, 60))

    def update_blood_pressure(self):
        systolic_change = random.gauss(0, 2)
        diastolic_change = random.gauss(0, 1)
        new_systolic = self.blood_pressure[0] + systolic_change
        new_diastolic = self.blood_pressure[1] + diastolic_change
        return (max(80, min(new_systolic, 200)), max(40, min(new_diastolic, 120)))

    def update_saturation(self):
        change = random.gauss(0, 0.5)
        new_saturation = self.saturation + change
        return max(80, min(new_saturation, 100))

    def generate_ecg(self):
        # Simplified ECG generation
        time = np.linspace(0, 8, 1000)
        ecg = np.sin(2 * np.pi * self.pulse / 60 * time) + 0.1 * np.random.randn(1000)
        return ','.join(map(str, ecg))

def write_to_timescaledb(patient):
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
            patient.patient_id,
            round(patient.temperature, 1),
            round(patient.pulse),
            round(patient.respiration_rate),
            round(patient.blood_pressure[0]),
            round(patient.blood_pressure[1]),
            patient.generate_ecg(),
            datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M:%S"),
            round(patient.saturation, 1)
        )
        cursor.execute(insert_query, record_to_insert)
        connection.commit()
        print(f"Record inserted successfully for patient {patient.patient_id}")
        send_websocket_message(record_to_insert)
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

        return {patient[0]: {"age": patient[1], "active": patient[2]} for patient in patients}
    except (Exception, psycopg2.Error) as error:
        print("Failed to fetch patients", error)
        return {}
    finally:
        if connection:
            cursor.close()
            connection.close()

def simulate_patient(patient, stop_event):
    while not stop_event.is_set():
        print(f'Simulate patient for: {patient.patient_id}')
        patient.update_vitals()
        write_to_timescaledb(patient)
        time.sleep(10)

def simulate_vitals():
    patients = {}
    patient_threads = {}
    stop_events = {}

    while True:
        db_patients = get_patients()
        # Remove inactive patients
        for patient_id in list(patients.keys()):
            if patient_id not in db_patients or not db_patients[patient_id]["active"]:
                print(f"Stopping simulation for patient {patient_id}")
                stop_events[patient_id].set()
                patient_threads[patient_id].join()
                del patients[patient_id]
                del patient_threads[patient_id]
                del stop_events[patient_id]

        # Add or update active patients
        for patient_id, patient_info in db_patients.items():
            if patient_info["active"]:
                if patient_id not in patients:
                    print(f"Starting simulation for patient {patient_id}")
                    patients[patient_id] = Patient(patient_id, patient_info["age"])
                    stop_events[patient_id] = threading.Event()
                    patient_threads[patient_id] = threading.Thread(
                        target=simulate_patient,
                        args=(patients[patient_id], stop_events[patient_id])
                    )
                    patient_threads[patient_id].start()
                else:
                    # Update age if it has changed
                    if patients[patient_id].age != patient_info["age"]:
                        patients[patient_id].age = patient_info["age"]
                        print(f"Updated age for patient {patient_id}")

        # Sleep for a while before checking for patient updates again
        time.sleep(60)  # Check for patient updates every minute

if __name__ == "__main__":
    simulate_vitals()
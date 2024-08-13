from openai import OpenAI
from datetime import datetime, timedelta
import psycopg2
from typing import List, Dict, Any
import time

OPENAI_API_KEY=""

def fetch_patient_data(patient_id: int, time_range: timedelta) -> List[Dict[str, Any]]:
    """
    Fetch patient data from the database for a given time range.
    """
    try:
        connection = psycopg2.connect(
            user="postgres",
            password="metallica",
            host="localhost",
            port="5432",
            database="postgres"
        )
        cursor = connection.cursor()

        end_time = datetime.now()
        start_time = end_time - time_range
        print(f'{start_time}-{end_time}')
        query = """
        SELECT temperature, pulse, respiration_rate, systolic, diastolic, saturation, time
        FROM vitals
        WHERE patient_id = %s AND time BETWEEN %s AND %s
        ORDER BY time DESC
        """
        cursor.execute(query, (patient_id, start_time, end_time))
        rows = cursor.fetchall()
        return [
            {
                "temperature": row[0],
                "pulse": row[1],
                "respiration_rate": row[2],
                "systolic": row[3],
                "diastolic": row[4],
                "saturation": row[5],
                "time": row[6]
            }
            for row in rows[:10]
        ]

    except (Exception, psycopg2.Error) as error:
        print("Error fetching patient data:", error)
        return []
    finally:
        if connection:
            cursor.close()
            connection.close()

def generate_health_score(patient_data: List[Dict[str, Any]]) -> str:
    """
    Generate a health score and analysis using OpenAI's ChatCompletion.
    """
    if not patient_data:
        return "No data available for analysis."

    # Prepare the prompt with the most recent vital signs
    latest_vitals = patient_data[0]
    prompt = f"""
    As a medical AI assistant, analyze the following vital signs and provide a health score (0-100) along with a brief explanation. Consider normal ranges and potential health implications.

    Latest vital signs:
    - Temperature: {latest_vitals['temperature']}°C
    - Pulse: {latest_vitals['pulse']} bpm
    - Respiration Rate: {latest_vitals['respiration_rate']} breaths/min
    - Blood Pressure: {latest_vitals['systolic']}/{latest_vitals['diastolic']} mmHg
    - Oxygen Saturation: {latest_vitals['saturation']}%

    Additional context:
    - Number of readings in the past hour: {len(patient_data)}
    - Trend information: [You can add trend analysis here if needed]

    Provide a health score (0-100) and a brief explanation of the patient's condition based on these vital signs.
    """

    try:
        client = OpenAI(api_key=OPENAI_API_KEY)
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "You are a medical AI assistant tasked with analyzing patient vital signs and providing a health score."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=150
        )
        return response.choices[0].message.content
    except Exception as e:
        print(f"Error generating health score: {e}")
        return "Unable to generate health score due to an error."

def main():
    patient_id = 4
    time_range = timedelta(hours=720)

    while True:
        patient_data = fetch_patient_data(patient_id, time_range)
        health_analysis = generate_health_score(patient_data)
        print(f"Patient {patient_id} Health Analysis:")
        print(health_analysis)
        print("\n" + "="*50 + "\n")

        # Wait for 10 minutes before the next analysis
        time.sleep(600)

if __name__ == "__main__":
    main()
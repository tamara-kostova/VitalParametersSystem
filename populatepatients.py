import psycopg2
from psycopg2 import sql
from datetime import datetime

# Database connection parameters
conn = psycopg2.connect(
    dbname='postgres',
    user='postgres',
    password='metallica',
    host='localhost',
    port='5432'
)
conn.autocommit = True

# Open a cursor to perform database operations
with conn.cursor() as cursor:
    # Sample patient data
    patients = [
        ('Tamara', 'Kostova', '2002-12-14', 21, 'Female', '1234567890123'),
        ('Aleksandra', 'Nastoska', '2003-01-17', 21, 'Female', '9876543210987'),
        ('Ilija', 'Bozoski', '2002-08-04', 21, 'Male', '5678901234567')
    ]

    # Insert data into the patients table
    insert_query = sql.SQL("""
        INSERT INTO patients (name, surname, date_of_birth, age, gender, embg)
        VALUES (%s, %s, %s, %s, %s, %s)
    """)

    try:
        for patient in patients:
            cursor.execute(insert_query, patient)
        print("Patients inserted successfully.")
    except psycopg2.Error as e:
        print(f"Error inserting patients: {e}")

# Close the connection
conn.close()

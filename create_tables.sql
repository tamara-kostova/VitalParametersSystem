-- Create the Patient table
CREATE TABLE IF NOT EXISTS patients (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    date_of_birth DATE NOT NULL,
    age INTEGER NOT NULL,
    gender TEXT,
    embg VARCHAR(20) UNIQUE NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

-- Create the Vitals table
CREATE TABLE IF NOT EXISTS vitals (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
    temperature DOUBLE PRECISION,
    pulse INTEGER,
    respiration_rate INTEGER,
    systolic INTEGER,
    diastolic INTEGER,
    ecg_string TEXT,
    time TIMESTAMPTZ NOT NULL
    );

-- Create a unique index on the Vitals table to support hypertable conversion
CREATE UNIQUE INDEX vitals_time_idx ON vitals (time, id);

-- Convert the Vitals table to a hypertable
SELECT create_hypertable('vitals', 'time', migrate_data => true);

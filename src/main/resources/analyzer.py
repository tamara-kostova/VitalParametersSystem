from flask import Flask, request, jsonify
from flask_cors import CORS
import base64
from io import BytesIO
import matplotlib.pyplot as plt

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

@app.route('/analyze_temperature', methods=['POST'])
def analyze_temperature():
    try:
        data = request.json
        temperatures = data
        ranges = {
            'Low': (0, 36.0),
            'Normal': (36.0, 37.5),
            'High': (37.5, 38.0),
            'Fever': (38.0, float('inf'))  # Anything above 38.0
        }

        categories_count = {category: 0 for category in ranges.keys()}

        for temp in temperatures:
            for category, (low, high) in ranges.items():
                if low <= temp < high:
                    categories_count[category] += 1
                    break

        categories = list(categories_count.keys())
        counts = [categories_count[category] for category in categories]

        plt.figure(figsize=(5, 5))
        plt.pie(counts, labels=categories, autopct='%1.1f%%', startangle=140, colors=['#1f77b4', '#ff7f0e', '#2ca02c', '#d62728'])
        plt.title('Temperature Classification')
        plt.legend(categories, loc='upper right')

        buf = BytesIO()
        plt.savefig(buf, format='png')
        buf.seek(0)
        image_base64 = base64.b64encode(buf.read()).decode('utf-8')
        buf.close()

        return jsonify({'image': image_base64})
    except Exception as e:
        print("Error processing request:", e)
        return str(e), 500


@app.route('/analyze_saturation', methods=['POST'])
def analyze_saturation():
    try:
        data = request.json
        saturations = data
        ranges = {
            'Normal': (95.0, 100.0),
            'Insufficient': (90.0, 95.0),
            'Decreased': (85.0, 90.0),
            'Critical': (80.0, 85.0),
            'Severe': (75.0, 80.0),
            'Acute': (0.0, 75.0)
        }

        categories_count = {category: 0 for category in ranges.keys()}

        for sat in saturations:
            if sat is not None:  # Check if sat is not None before comparing
                for category, (low, high) in ranges.items():
                    if low <= sat <= high:
                        categories_count[category] += 1
                        break
            else:
                print("Warning: Found None value in saturations list")
        categories = list(categories_count.keys())
        counts = [categories_count[category] for category in categories]

        plt.figure(figsize=(6, 5))
        plt.pie(counts, labels=categories, autopct='%1.1f%%', startangle=140)
        plt.title('Saturation Classification')
        plt.legend(categories, loc='upper right')

        buf = BytesIO()
        plt.savefig(buf, format='png')
        buf.seek(0)
        image_base64 = base64.b64encode(buf.read()).decode('utf-8')
        buf.close()

        return jsonify({'image': image_base64})
    except Exception as e:
        print("Error processing request:", e)
        return str(e), 500

@app.route('/analyze_respiration_rate', methods=['POST'])
def analyze_respiration_rate():
    try:
        data = request.json
        respirations = data
        ranges = {
            'Eupnea': (12, 20),
            'Tachypnea': (20, float('inf')),
            'Bradypnea': (0, 12),
            'Apnea': (0, 0)  # For simplicity, Apnea is defined as exactly 0 breaths per minute
        }

        categories_count = {category: 0 for category in ranges.keys()}

        for rate in respirations:
            for category, (low, high) in ranges.items():
                if low <= rate <= high:
                    categories_count[category] += 1
                    break
        categories = list(categories_count.keys())
        counts = [categories_count[category] for category in categories]

        plt.figure(figsize=(5, 5))
        plt.pie(counts, labels=categories, autopct='%1.1f%%', startangle=140, colors=['#1f77b4', '#ff7f0e', '#2ca02c', '#d62728'])
        plt.title('Respiration Rate Classification')
        plt.legend(categories, loc='upper right')


        buf = BytesIO()
        plt.savefig(buf, format='png')
        buf.seek(0)
        image_base64 = base64.b64encode(buf.read()).decode('utf-8')
        buf.close()

        return jsonify({'image': image_base64})
    except Exception as e:
        print("Error processing request:", e)
        return str(e), 500


@app.route('/analyze_pulse', methods=['POST'])
def analyze_pulse_rate():
    try:
        data = request.json
        pulses = data
        ranges = {
            'Resting': (60, 100),
            'Moderate': (101, 130),
            'Vigorous': (131, float('inf'))
        }

        categories_count = {category: 0 for category in ranges.keys()}

        for rate in pulses:
            for category, (low, high) in ranges.items():
                if low <= rate <= high:
                    categories_count[category] += 1
                    break
        categories = list(categories_count.keys())
        counts = [categories_count[category] for category in categories]

        plt.figure(figsize=(5, 5))
        plt.pie(counts, labels=categories, autopct='%1.1f%%', startangle=140, colors=['#1f77b4', '#ff7f0e', '#2ca02c', '#d62728'])
        plt.title('Pulse Classification')
        plt.legend(categories, loc='upper right')


        buf = BytesIO()
        plt.savefig(buf, format='png')
        buf.seek(0)
        image_base64 = base64.b64encode(buf.read()).decode('utf-8')
        buf.close()

        return jsonify({'image': image_base64})
    except Exception as e:
        print("Error processing request:", e)
        return str(e), 500


@app.route('/analyze_systolic', methods=['POST'])
def analyze_systolic_rate():
    try:
        data = request.json
        systolic_rates  = data
        ranges = {
            'Normal': (-float('inf'), 120),
            'Elevated': (120, 130),
            'ISH 1': (130, 140),
            'ISH 2': (140, 150),
            'Mild': (150,160),
            'Moderate': (160,180),
            'Severe': (180, float('inf'))
        }

        categories_count = {category: 0 for category in ranges.keys()}

        for rate in systolic_rates:
            for category, (low, high) in ranges.items():
                if low <= rate <= high:
                    categories_count[category] += 1
                    break
        categories = list(categories_count.keys())
        counts = [categories_count[category] for category in categories]

        plt.figure(figsize=(5, 5))
        plt.pie(counts, labels=categories, autopct='%1.1f%%', startangle=140, colors=['#1f77b4', '#ff7f0e', '#2ca02c', '#d62728'])
        plt.title('BP Classification')
        plt.legend(categories, loc='upper right')


        buf = BytesIO()
        plt.savefig(buf, format='png')
        buf.seek(0)
        image_base64 = base64.b64encode(buf.read()).decode('utf-8')
        buf.close()

        return jsonify({'image': image_base64})
    except Exception as e:
        print("Error processing request:", e)
        return str(e), 500
@app.route('/analyze_ecg/<int:patient_id>', methods=['POST'])
def analyze_ecg(patient_id):
    try:
        data = request.json
        ecg_values = data
        print('ecg:')
        print(ecg_values)
        plt.figure(figsize=(10, 5))
        plt.plot(ecg_values, color='blue')
        plt.title(f'ECG Analysis for Patient {patient_id}')
        plt.xlabel('Time')
        plt.ylabel('ECG Value')

        buf = BytesIO()
        plt.savefig(buf, format='png')
        buf.seek(0)
        image_base64 = base64.b64encode(buf.read()).decode('utf-8')
        buf.close()

        return jsonify({'image': image_base64})
    except Exception as e:
        print("Error processing request:", e)
        return str(e), 500

if __name__ == '__main__':
    app.run(port=5000)

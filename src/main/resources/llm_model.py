from flask import Flask, request, jsonify
from flask_cors import CORS
from datetime import datetime, timedelta
from llm_util import fetch_patient_data, generate_health_score

app = Flask(__name__)
CORS(app)

@app.route('/health-score', methods=['POST'])
def get_health_score():
    data = request.json
    patient_id = data.get('patient_id')
    time_range = timedelta(hours=1)

    if not patient_id:
        return jsonify({"error": "Patient ID is required"}), 400

    patient_data = fetch_patient_data(patient_id, time_range)
    health_analysis = generate_health_score(patient_data)

    return jsonify({
        "patient_id": patient_id,
        "health_analysis": health_analysis,
        "timestamp": datetime.now().isoformat()
    })

if __name__ == '__main__':
    app.run(debug=True, port=5001)
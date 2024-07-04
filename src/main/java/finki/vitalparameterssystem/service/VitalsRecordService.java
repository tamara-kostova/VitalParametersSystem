package finki.vitalparameterssystem.service;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.repository.PatientRepository;
import finki.vitalparameterssystem.repository.VitalsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VitalsRecordService {
    @Autowired
    private VitalsRecordRepository vitalsRepository;

    @Autowired
    private PatientRepository patientRepository;

    public List<VitalsRecord> getAllVitals() {
        return vitalsRepository.findAll();
    }

    public VitalsRecord addVitals(Long patientId, VitalsRecord vitals) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        vitals.setPatient(patient);
        return vitalsRepository.save(vitals);
    }

}

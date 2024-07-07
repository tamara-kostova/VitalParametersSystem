package finki.vitalparameterssystem.service.impl;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.repository.PatientRepository;
import finki.vitalparameterssystem.repository.VitalsRecordRepository;
import finki.vitalparameterssystem.service.VitalsRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VitalsRecordServiceImpl implements VitalsRecordService {
    private final PatientRepository patientRepository;
    private final VitalsRecordRepository vitalsRecordRepository;

    public VitalsRecordServiceImpl(PatientRepository patientRepository, VitalsRecordRepository vitalsRecordRepository) {
        this.patientRepository = patientRepository;
        this.vitalsRecordRepository = vitalsRecordRepository;
    }

    @Override
    public List<VitalsRecord> getAllVitals() {
        return vitalsRecordRepository.findAll();
    }

    @Override
    public List<VitalsRecord> getVitalsByPatientId(Long patientId) {
        return vitalsRecordRepository.findByPatientId(patientId);
    }

    @Override
    public VitalsRecord addVitals(Long patientId, VitalsRecord vitals) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        vitals.setPatient(patient);
        return vitalsRecordRepository.save(vitals);
    }

    @Override
    public VitalsRecord findLatestVitalsByPatientId(Long patientId) {
        return vitalsRecordRepository.findByPatientOrderByTimeDesc(patientId);
    }
}

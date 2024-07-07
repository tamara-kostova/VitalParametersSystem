package finki.vitalparameterssystem.service;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.repository.PatientRepository;
import finki.vitalparameterssystem.repository.VitalsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface VitalsRecordService {

    List<VitalsRecord> getAllVitals();

    List<VitalsRecord> getVitalsByPatientId(Long patientId);

    VitalsRecord addVitals(Long patientId, VitalsRecord vitals);
    VitalsRecord findLatestVitalsByPatientId(Long patientId);

}

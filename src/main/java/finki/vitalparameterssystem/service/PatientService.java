package finki.vitalparameterssystem.service;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PatientService {
    List<Patient> getAllPatients();

    Optional<Patient> getPatientById(Long id);
    Optional<Patient> savePatient(Patient patient);
    Optional<Patient> editPatient (Long id, String name, String surname, String gender, LocalDate dateOfBirth, Integer age, String embg);
    void deletePatientById(Long id);
    List<Patient> getAllPatientsDeletedIsFalse();
}

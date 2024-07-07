package finki.vitalparameterssystem.service.impl;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.repository.PatientRepository;
import finki.vitalparameterssystem.service.PatientService;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> getAllPatients() {
        return this.patientRepository.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return this.patientRepository.findById(id);
    }

    @Override
    public Optional<Patient> savePatient(Patient patient) {
        return Optional.of(this.patientRepository.save(patient));
    }

    @Override
    public Optional<Patient> editPatient(Long id, String name, String surname, String gender, LocalDate dateOfBirth, Integer age, String embg) {
        Patient patient = patientRepository.findById(id).get();
        patient.setName(name);
        patient.setSurname(surname);
        patient.setGender(gender);
        patient.setDateOfBirth(dateOfBirth);
        patient.setAge(age);
        patient.setEmbg(embg);
        patientRepository.save(patient);
        return Optional.of(patient);
    }

    @Override
    public void deletePatientById(Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        patientOptional.ifPresent(patient -> {
            patient.setDeleted(true);
            patientRepository.save(patient);
        });
    }

    @Override
    public List<Patient> getAllPatientsDeletedIsFalse() {
        return patientRepository.findAllByDeletedIsFalse();
    }


}

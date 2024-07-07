package finki.vitalparameterssystem.repository;

import finki.vitalparameterssystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByDeletedIsFalse();
}


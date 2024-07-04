package finki.vitalparameterssystem.repository;

import finki.vitalparameterssystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}


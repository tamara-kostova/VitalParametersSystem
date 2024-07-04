package finki.vitalparameterssystem.repository;

import finki.vitalparameterssystem.model.VitalsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalsRecordRepository extends JpaRepository<VitalsRecord, Long> {
    List<VitalsRecord> findByPatientId(Long patientId);
}
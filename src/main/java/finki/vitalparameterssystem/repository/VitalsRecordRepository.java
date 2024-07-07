package finki.vitalparameterssystem.repository;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.model.VitalsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VitalsRecordRepository extends JpaRepository<VitalsRecord, Long> {
    List<VitalsRecord> findByPatientId(Long patientId);
    @Query("SELECT v FROM VitalsRecord v WHERE v.patient.id = :patientId ORDER BY v.time DESC LIMIT 1")
    VitalsRecord findByPatientOrderByTimeDesc(Long patientId);
}
package finki.vitalparameterssystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "vitals")
public class VitalsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Patient patient;
    private Double temperature;
    private Integer pulse;
    private Integer respirationRate;
    private Integer systolic;
    private Integer diastolic;
    private String ecgString;
    private LocalDateTime time;
    private Double saturation;
}

package finki.vitalparameterssystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String gender;
    private LocalDate dateOfBirth;
    private Integer age;
    private String embg;
    private Boolean active;
    private Boolean deleted;

    public Patient(String name, String surname, String gender, LocalDate dateOfBirth, Integer age, String embg, Boolean active, Boolean deleted) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.embg = embg;
        this.active = active;
        this.deleted = deleted;
    }
    public Patient(){}
}


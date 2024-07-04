package finki.vitalparameterssystem.web;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patients";
    }

    @GetMapping("/patients/{id}")
    public String getPatientDetails(@PathVariable Long id, Model model) {
        Optional<Patient> patient = patientService.getPatientById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "details";
        } else {
            // handle patient not found
            return "redirect:/patients";
        }
    }
}


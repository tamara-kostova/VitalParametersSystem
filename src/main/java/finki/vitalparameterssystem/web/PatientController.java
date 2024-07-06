package finki.vitalparameterssystem.web;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.service.PatientService;
import finki.vitalparameterssystem.service.VitalsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private VitalsRecordService vitalsRecordService;
    @GetMapping({"","/","/home"})
    public String home(){
        return "home";
    }

    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patientsStaro";
    }
    @GetMapping("/patients")
    public String searchPatientById(@RequestParam(value = "id",required = false) Long id, Model model) {
        Optional<Patient> patient = patientService.getPatientById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            List<VitalsRecord> vitalsRecords = vitalsRecordService.getVitalsByPatientId(id);
            model.addAttribute("vitalsRecords", vitalsRecords);

            return "details";
        } else {
            return "redirect:/patients";
        }
    }

    @GetMapping("/patients/{id}")
    public String getPatientDetails(@PathVariable Long id, Model model) {
        Optional<Patient> patient = patientService.getPatientById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            List<VitalsRecord> vitalsRecords = vitalsRecordService.getVitalsByPatientId(id);
            model.addAttribute("vitalsRecords", vitalsRecords);

            return "details";
        } else {
            return "redirect:/patients";
        }
    }
}


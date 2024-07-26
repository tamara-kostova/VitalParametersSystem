package finki.vitalparameterssystem.web;

import finki.vitalparameterssystem.model.Patient;
import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.service.PatientService;
import finki.vitalparameterssystem.service.VitalsRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class PatientController {

    private final PatientService patientService;

    private final VitalsRecordService vitalsRecordService;

    @GetMapping({"","/","/home"})
    public String home(){
        return "home";
    }

    public PatientController(VitalsRecordService vitalsRecordService, PatientService patientService) {
        this.vitalsRecordService = vitalsRecordService;
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    public String searchPatientById(@RequestParam(value = "id",required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("allPatients", patientService.getAllPatientsDeletedIsFalse());
            return "patients";
        }
        Optional<Patient> patient = patientService.getPatientById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            List<VitalsRecord> vitalsRecords = vitalsRecordService.getVitalsByPatientId(id);
            VitalsRecord latestRecord = vitalsRecordService.findLatestVitalsByPatientId(patient.get().getId());
            model.addAttribute("allVitalsRecords", vitalsRecords);
            model.addAttribute("vitalsRecords", latestRecord);

            return "details";
        } else {
            return "patients";
        }
    }

    @GetMapping("/patients/{id}")
    public String getPatientDetails(@PathVariable Long id, Model model) {
        Optional<Patient> patient = patientService.getPatientById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            VitalsRecord vitalsRecords = vitalsRecordService.findLatestVitalsByPatientId(patient.get().getId());
            model.addAttribute("vitalsRecords", vitalsRecords);

            return "details";
        } else {
            return "redirect:/patients";
        }
    }
    @GetMapping("/add-patient")
    public String addPatientPage(){
        return "add-patient";
    }
    @PostMapping("/addPatient")
    public String savePatient(@RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String dateOfBirth,
                              @RequestParam Integer age,
                              @RequestParam String gender,
                              @RequestParam String embg){
        Patient patient = new Patient(name
        , surname, gender, LocalDate.parse(dateOfBirth), age, embg, true, false);
        this.patientService.savePatient(patient);
        return "redirect:/patients";
    }
    @GetMapping("/patients/edit-patient/{id}")
    public String editPatientPage (@PathVariable Long id, Model model){
        if (this.patientService.getPatientById(id).isPresent()){
            Patient patient = this.patientService.getPatientById(id).get();
            model.addAttribute("patient", patient);
            return "edit-patient";
        }
        return "redirect:/patients";
    }
    @PostMapping("/edit-patient/{id}")
    public String editPatient(@PathVariable("id") Long id,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String dateOfBirth,
                              @RequestParam Integer age,
                              @RequestParam String gender,
                              @RequestParam String embg){
        this.patientService.editPatient(id, name, surname, gender, LocalDate.parse(dateOfBirth), age, embg);
        return "redirect:/patients";
    }
    @PostMapping("/patients/delete-patient/{id}")
    public String deletePatient(@PathVariable Long id){
        this.patientService.deletePatientById(id);
        return "redirect:/patients";
    }
    @PostMapping("/patients/activate/{id}")
    public String activate(@PathVariable Long id) {
        if (this.patientService.getPatientById(id).isPresent()) {
            Patient patient = this.patientService.getPatientById(id).get();
            patient.setActive(true);
            patientService.savePatient(patient);
        }
        return "redirect:/patients";
    }

    @PostMapping("/patients/deactivate/{id}")
    public String deactivate(@PathVariable Long id) {
        if (this.patientService.getPatientById(id).isPresent()) {
            Patient patient = this.patientService.getPatientById(id).get();
            patient.setActive(false);
            patientService.savePatient(patient);
        }
        return "redirect:/patients";
    }
}


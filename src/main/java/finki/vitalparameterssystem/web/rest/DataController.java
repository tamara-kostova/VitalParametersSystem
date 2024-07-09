package finki.vitalparameterssystem.web.rest;

import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.service.DataService;
import finki.vitalparameterssystem.service.VitalsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class DataController {
    @Autowired
    private DataService dataService;
    @Autowired
    private VitalsRecordService vitalsRecordService;

    @PostMapping("/analyze_temperature")
    public ResponseEntity<?> analyzeTemperature(@RequestBody List<Double> temperatureList){
        try{
            String result = dataService.sendTemperatureToPython(temperatureList);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e.getMessage());
        }
    }

    @PostMapping("/analyze_saturation")
    public ResponseEntity<?> analyzeSaturation(@RequestBody List<Double> saturationList){
        try{
            String result = dataService.sendSaturationToPython(saturationList);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e.getMessage());
        }
    }
    @PostMapping("/analyze_respiration_rate")
    public ResponseEntity<?> analyzeRespirationRate(@RequestBody List<Integer> respirationRateList){
        try{
            String result = dataService.sendRespirationRateToPython(respirationRateList);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e.getMessage());
        }
    }
    @PostMapping("/analyze_pulse")
    public ResponseEntity<?> analyzePulse(@RequestBody List<Integer> pulseList){
        try{
            String result = dataService.sendPulseToPython(pulseList);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e.getMessage());
        }
    }

    @PostMapping("/analyze_systolic")
    public ResponseEntity<?> analyzeSystolic(@RequestBody List<Integer> systolicList){
        try{
            String result = dataService.sendPulseToPython(systolicList);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e.getMessage());
        }
    }

    @GetMapping("/getTemperatureByPatientId/{id}")
    public List<Double> getTemperatureByPatientId(@PathVariable Long id) {
        List<VitalsRecord> recordList = vitalsRecordService.getVitalsByPatientId(id);
        List<Double> temperatureList = new ArrayList<>();
        recordList.forEach(vitalsRecord ->
                temperatureList.add(vitalsRecord.getTemperature()));
        return temperatureList;
    }
    @GetMapping("/getSaturationByPatientId/{id}")
    public List<Double> getSaturationByPatientId(@PathVariable Long id) {
        List<VitalsRecord> recordList = vitalsRecordService.getVitalsByPatientId(id);
        List<Double> saturationList = new ArrayList<>();
        recordList.forEach(vitalsRecord ->
                saturationList.add(vitalsRecord.getSaturation()));
        return saturationList;
    }
    @GetMapping("/getRespirationRateByPatientId/{id}")
    public List<Integer> getRespirationRateByPatientId(@PathVariable Long id) {
        List<VitalsRecord> recordList = vitalsRecordService.getVitalsByPatientId(id);
        List<Integer> respirationRateList = new ArrayList<>();
        recordList.forEach(vitalsRecord ->
                respirationRateList.add(vitalsRecord.getRespirationRate()));
        return respirationRateList;
    }
    @GetMapping("/getPulseByPatientId/{id}")
    public List<Integer> getPulseByPatientId(@PathVariable Long id) {
        List<VitalsRecord> recordList = vitalsRecordService.getVitalsByPatientId(id);
        List<Integer> pulseList = new ArrayList<>();
        recordList.forEach(vitalsRecord ->
                pulseList.add(vitalsRecord.getPulse()));
        return pulseList;
    }
    @GetMapping("/getSystolicByPatientId/{id}")
    public List<Integer> getSystolicByPatientId(@PathVariable Long id) {
        List<VitalsRecord> recordList = vitalsRecordService.getVitalsByPatientId(id);
        List<Integer> systolicList = new ArrayList<>();
        recordList.forEach(vitalsRecord ->
                systolicList.add(vitalsRecord.getSystolic()));
        return systolicList;
    }
    @GetMapping("/getDiastolicByPatientId/{id}")
    public List<Integer> getDiastolicByPatientId(@PathVariable Long id) {
        List<VitalsRecord> recordList = vitalsRecordService.getVitalsByPatientId(id);
        List<Integer> diastolicList = new ArrayList<>();
        recordList.forEach(vitalsRecord ->
                diastolicList.add(vitalsRecord.getDiastolic()));
        return diastolicList;
    }
    @GetMapping("/getEcgByPatientId/{id}")
    public List<Double> getEcgByPatientId(@PathVariable Long id) {
        List<VitalsRecord> recordList = vitalsRecordService.getVitalsByPatientId(id);
        int size = recordList.size();
        List<VitalsRecord> last10Records = recordList.subList(Math.max(size - 10, 0), size);
        List<Double> ecgList = new ArrayList<>();
        for (VitalsRecord record : last10Records){
            String ecgs = record.getEcgString();
            for (String value : ecgs.split(",")) {
                try {
                    ecgList.add(Double.parseDouble(value.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing ECG value: " + value);
                }
            }
        }
        return ecgList;
    }
}

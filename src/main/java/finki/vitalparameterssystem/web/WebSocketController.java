package finki.vitalparameterssystem.web;

import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.service.VitalsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WebSocketController {

    @Autowired
    private VitalsRecordService vitalsRecordService;
    private final SimpMessagingTemplate messagingTemplate;
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//    @MessageMapping("/vitals/{patientId}")
//    @SendTo("/topic/vitals")
//    public VitalsRecord getLatestVitals(Long patientId) {
//        return vitalsRecordService.findLatestVitalsByPatientId(patientId);
//    }
    @PostMapping("/vitals")
    public void sendVitals(@RequestBody VitalsRecord vitalRecord) {
        System.out.println(vitalRecord);
        System.out.println("Sending Vitals: " + vitalRecord);
        messagingTemplate.convertAndSend("/topic/vitals", vitalRecord);
    }
}


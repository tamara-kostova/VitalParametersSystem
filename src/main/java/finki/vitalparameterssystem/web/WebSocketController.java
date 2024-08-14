package finki.vitalparameterssystem.web;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/vitals")
    public void sendVitals(@RequestBody Map<String, Object> vitalRecordData) {
        System.out.println("Sending Vitals: " + vitalRecordData);
        messagingTemplate.convertAndSend("/topic/vitals/"+vitalRecordData.get("patientId"), vitalRecordData);
    }
}


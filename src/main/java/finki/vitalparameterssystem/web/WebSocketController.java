package finki.vitalparameterssystem.web;

import finki.vitalparameterssystem.model.VitalsRecord;
import finki.vitalparameterssystem.service.VitalsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/vitals")
    public void sendVitals(@RequestBody Map<String, Object> vitalRecordData) {
        System.out.println(vitalRecordData);
        System.out.println("Sending Vitals: " + vitalRecordData);
        messagingTemplate.convertAndSend("/topic/vitals", vitalRecordData);
    }
}


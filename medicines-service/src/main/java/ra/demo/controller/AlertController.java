package ra.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.demo.dto.AlertMessage;
import ra.demo.service.SeedMessage;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    @Autowired
    private SeedMessage seedMessage;

    @PostMapping
    public ResponseEntity<String> sendAlert(@RequestBody AlertMessage alertMessage) {
        seedMessage.seedMessage(alertMessage);
        return ResponseEntity.ok("Đã gửi thông báo đến kênh : " + alertMessage.getTopicListen());
    }
}
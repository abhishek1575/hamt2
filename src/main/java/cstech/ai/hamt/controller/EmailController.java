package cstech.ai.hamt.controller;

import cstech.ai.hamt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestBody Map<String, Object> payload) {
        String from = (String) payload.get("from");
        List<String> to = (List<String>) payload.get("to");
        String subject = (String) payload.get("subject");
        String text = (String) payload.get("text");

        return emailService.sendEmail(from, to, subject, text);
    }
}

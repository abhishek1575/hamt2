package cstech.ai.hamt.service;

import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService {

    @Value("${resend.api.key}") // Inject API key from application.properties
    private String apiKey;

    public String sendEmail(String from, List<String> to, String subject, String html) {
        Resend resend = new Resend(apiKey);

        // Convert recipients list to an array
        String[] recipientsArray = to.toArray(new String[0]);

        // Build email request
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(from)
                .to(recipientsArray)
                .subject(subject)
                .html(html)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            return "✅ Email sent successfully! ID: " + response.getId();
        } catch (ResendException e) {
            return "❌ Failed to send email: " + e.getMessage();
        }
    }
}
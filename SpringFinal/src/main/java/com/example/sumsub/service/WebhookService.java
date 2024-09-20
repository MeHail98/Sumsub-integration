package com.example.sumsub.service;
import com.example.sumsub.entity.Webhook;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class WebhookService {

    private WebhookRepository webhookRepository;

    public List<Webhook> findAll(){
        return webhookRepository.findAll();
    }

    public Webhook save(String webhookPayload){
        JSONObject webhookPayloadJson = new JSONObject(webhookPayload);
        String externalUserId = webhookPayloadJson.getString("externalUserId");
        String webhookName = webhookPayloadJson.getString("type");
        String webhookTime = webhookPayloadJson.getString("createdAt");

        LocalDateTime convertedTime = LocalDateTime.parse(webhookTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"));

        Webhook webhook = Webhook.builder().webhookName(webhookName)
                .webhookTime(convertedTime)
                .externalUserId(externalUserId)
                .build();
        return webhookRepository.save(webhook);
    }
}

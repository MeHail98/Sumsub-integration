package com.example.sumsub.controller;
import com.example.sumsub.service.ApplicantService;
import com.example.sumsub.service.WebhookService;
import com.example.sumsub.validator.WebhookValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class WebhookController  {
    WebhookService webhookService;
    ApplicantService applicantService;

    @GetMapping("/webhooks")
    public String findAll(Model model) {
        model.addAttribute("webhooks", webhookService.findAll());
        return "webhooks";
    }

    @PostMapping("/webhook")
    @ResponseBody
    public void handleWebhook(@RequestBody byte[] webhookBody,
                              @RequestHeader(value = "x-payload-digest", required = false) String xPayloadDigest,
                              @RequestHeader(value = "x-payload-digest-alg", required = false) String xPayloadDigestAlg)
            throws NoSuchAlgorithmException, IOException, InvalidKeyException{
        boolean isValid = WebhookValidator.validateWebhook(webhookBody, xPayloadDigest,xPayloadDigestAlg);
        String jsonWebhookBody = new String(webhookBody,StandardCharsets.UTF_8);
        if(isValid){
            webhookService.save(jsonWebhookBody);
            applicantService.update(jsonWebhookBody);
        }
    }
}


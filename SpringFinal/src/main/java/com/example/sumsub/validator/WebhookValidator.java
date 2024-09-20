package com.example.sumsub.validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class WebhookValidator {

    private static String SUMSUB_SPECIFIED_SECRET_KEY;

    public static boolean validateWebhook(byte[] body, String xPayloadDigest, String xPayloadDigestAlgReceived) throws IOException {

        if (xPayloadDigest == null && xPayloadDigestAlgReceived == null){
            log.info("no secret key is used on the sumsub side");
            return true;
        }
        String webhookAlgorithm = "HmacSHA256";
        switch (xPayloadDigestAlgReceived) {
            case "HMAC_SHA512_HEX" -> webhookAlgorithm = "HmacSHA512";
            case "HMAC_SHA1_HEX" -> webhookAlgorithm = "HmacSHA1";
        }
        log.info(webhookAlgorithm + "is used");

        String locallyCalculatedDigest;
        try {
            locallyCalculatedDigest = calculateDigest(body, webhookAlgorithm);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        if (compareDigests(xPayloadDigest, locallyCalculatedDigest)) {
            log.info("webhook is valid. Calculated digest:" +locallyCalculatedDigest+",expected:"+xPayloadDigest);
            return true;
        } else {
            log.error("webhook is NOT valid. Calculated digest:" +locallyCalculatedDigest+",expected:"+xPayloadDigest);
            return false;
        }
    }

    private static String calculateDigest(byte[] bodyBytes, String webhookAlgorithm)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance(webhookAlgorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(SUMSUB_SPECIFIED_SECRET_KEY.getBytes(StandardCharsets.UTF_8), webhookAlgorithm);
        hmac.init(secretKeySpec);
        byte[] encriptedBodyBytes = hmac.doFinal(bodyBytes);
        return Hex.encodeHexString(encriptedBodyBytes);
    }

    private static boolean compareDigests(String expectedDigest, String calculatedDigest) {
        return expectedDigest.equals(calculatedDigest);
    }

    @Value("${sumsub.webhook_secret_key}")
    public void setSumsubWebhookSecretKey(String sumSubSpecifiedSecretKey){
        SUMSUB_SPECIFIED_SECRET_KEY=sumSubSpecifiedSecretKey;
    }

}

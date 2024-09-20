package com.example.sumsub.service;
import com.example.sumsub.entity.Applicant;
import com.example.sumsub.exception.ApplicantException;
import com.example.sumsub.request.Request;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicantService {
    private ApplicantRepository applicantRepository;

    public Applicant findById(String external_user_id) {
        return applicantRepository.findById(external_user_id)
                .orElseThrow(()->new ApplicantException("applicant not found"));
    }

    public List<Applicant> findAll() {
        log.info("findAll method called");
        return applicantRepository.findAll();
    }

    public Applicant save(String externalUserId, String levelName) {
        log.info("save method called");
        if (findAll().stream().anyMatch(applicant -> Objects.equals(applicant.getExternalUserId(), externalUserId))) {
            throw new ApplicantException("applicant already exists");
        }
        log.info("save method executed");
        return applicantRepository.save(Applicant.builder()
                .externalUserId(externalUserId)
                .levelName(levelName).build());
    }

    public void update(String webhookPayload) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        log.info("update method called");

        try {
            JSONObject webhookPayloadJson = new JSONObject(webhookPayload);
            String levelName = webhookPayloadJson.optString("levelName");
            String externalUserId = webhookPayloadJson.optString("externalUserId");

            String applicantDataPayload = Request.getApplicantData(externalUserId);
            System.out.println(applicantDataPayload);
            Applicant applicant = buildApplicant(applicantDataPayload, externalUserId, levelName);

            applicantRepository.save(applicant);
            log.info("update method executed");

        } catch (IllegalArgumentException e) {
            log.error("Failed to update applicant: " + e.getMessage());
        }
    }

    public Applicant buildApplicant(String applicantDataPayload, String externalUserId, String levelName) {
        JSONObject applicantDataPayloadJson = new JSONObject(applicantDataPayload);
        JSONObject info = applicantDataPayloadJson.optJSONObject("info");
        JSONObject fixedInfo = applicantDataPayloadJson.optJSONObject("fixedInfo");

        if (info == null && fixedInfo == null) {
            throw new IllegalArgumentException("Both info and fixedInfo are empty");
        }
        String firstName = getValue(info, fixedInfo, "firstName");
        String lastName = getValue(info, fixedInfo, "lastName");
        String dob = getValue(info, fixedInfo, "dob");

        Applicant.ApplicantBuilder applicantBuilder = Applicant.builder();

        if (firstName != null && !firstName.isEmpty()) {
            applicantBuilder.firstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            applicantBuilder.lastName(lastName);
        }
        if (dob != null && !dob.isEmpty()) {
            applicantBuilder.dateOfBirth(Date.valueOf(dob));
        }
        return applicantBuilder.externalUserId(externalUserId).levelName(levelName).build();
    }

    private String getValue(JSONObject info, JSONObject fixedInfo, String key) {
        if (info != null && info.has(key)) {
            return info.optString(key);
        } else if (fixedInfo != null) {
            return fixedInfo.optString(key);
        } else {
            return null;
        }
    }

    public String getAccessToken (String externalUserId, String levelName)
            throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        log.info("get access token method called");
        String accessTokenPayload = Request.getAccessToken(externalUserId, levelName);
        JSONObject accessTokenPayloadJson = new JSONObject(accessTokenPayload);
        String accessToken = accessTokenPayloadJson.getString("token");
        log. info("get access token method executed with access token:" + accessToken);
        return accessToken;
    }

    public String getVerificationLink(String externalUserId, String levelName)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("get verification link method called");
        String linkPayload = Request.generateLink(externalUserId,levelName);
        JSONObject linkPayloadJson = new JSONObject(linkPayload);
        String link = linkPayloadJson.getString("url");
        log.info("get link executed with link:" + link);
        return link;
    }

    public List<String> getListOfLevelsIndividuals() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("get list of levels method called");
        String levelsList = Request.getListOfLevels();
        List<String> levels = convertJsonListOfLevelsToListOfLevelsName("individual",levelsList);
        log.info("get list of levels executed");
        return levels;

    }

    public static List<String> convertJsonListOfLevelsToListOfLevelsName(String applicantType, String initialLevelsList){
        JSONObject levelsListJson = new JSONObject(initialLevelsList);
        JSONArray itemsArray = levelsListJson.getJSONObject("list").getJSONArray("items");

        Map<String, String> levelsAndType= new HashMap<>();
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject item = itemsArray.getJSONObject(i);
            String name = item.optString("name");
            String applicantTypeFromJson = item.optString("applicantType");
            levelsAndType.put(name,applicantTypeFromJson);
        }

        List<String> levels = new ArrayList<>();
        for (Map.Entry<String, String> entry : levelsAndType.entrySet()) {
            if (applicantType.equals(entry.getValue())) {
                levels.add(entry.getKey());
            }
        }
        return levels;
    }
}



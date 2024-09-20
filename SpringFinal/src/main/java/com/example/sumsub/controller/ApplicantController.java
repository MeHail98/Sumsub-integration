package com.example.sumsub.controller;
import com.example.sumsub.service.ApplicantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class ApplicantController {
    private final ApplicantService applicantService;

    @GetMapping("/applicants")
    public String findAll(Model model) {
        model.addAttribute("applicants", applicantService.findAll());
        return "applicants";
    }

    @GetMapping("applicants/{id}")
    public String finById(Model model, @PathVariable String id) {
        model.addAttribute("applicant", applicantService.findById(id));
        return "applicant";
    }

    @GetMapping(value = "/createApplicant")
    public String createApplicant(Model model) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        model.addAttribute("listOfLevels",applicantService.getListOfLevelsIndividuals());
        return "createApplicant";
    }

    @PostMapping(value = "/createApplicant")
    public String processApplicant(@RequestParam("level_name") String levelName,
                                   @RequestParam("external_user_id") String externalUserId,
                                   Model model){
        model.addAttribute("applicant", applicantService.save(externalUserId,levelName));
        return "applicant";
    }

    @PostMapping(value = "/SDK")
    public String launchSDK(@RequestParam("level_name") String levelName,
                            @RequestParam("external_user_id") String externalUserId,
                            Model model) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        String token = applicantService.getAccessToken(externalUserId,levelName);
        model.addAttribute("accessToken", token);
        return "SDK";
    }

    @PostMapping(value = "applicants/link")
    public String generateLink(
            @RequestParam("level_name") String levelName,
            @RequestParam("external_user_id") String externalUserId,
            Model model) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        model.addAttribute("link", applicantService.getVerificationLink(externalUserId, levelName));
        return "link";
    }
}

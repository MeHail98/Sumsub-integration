package com.example.sumsub.request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import static com.example.sumsub.request.PreRequest.sendGet;
import static com.example.sumsub.request.PreRequest.sendPost;

public class Request {
    public static String getApplicantStatus(String applicantId)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = sendGet("/resources/applicants/" + applicantId + "/requiredIdDocsStatus");
        ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }

    public static String getAccessToken(String externalUserId, String levelName)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = sendPost("/resources/accessTokens?userId="
                + URLEncoder.encode(externalUserId, StandardCharsets.UTF_8.toString())
                + "&levelName="
                + URLEncoder.encode(levelName, StandardCharsets.UTF_8.toString()), RequestBody.create(new byte[0], null));
        ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }

    public static String getApplicantData(String externalUserId)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = sendGet("/resources/applicants/-;externalUserId=" + externalUserId + "/one");
        ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }

    public static String generateLink (String externalUserId, String levelName)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Response response = sendPost("/resources/sdkIntegrations/levels/"
                + levelName
                + "/websdkLink?externalUserId="
                + externalUserId, RequestBody.create(new byte[0], null));
        ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }

    public static String getListOfLevels() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Response response = sendGet("/resources/applicants/-/levels");
        ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }

    public static void getDocument (String inspectionId, String imageId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {

        Response response = sendGet("/resources/inspections/" + inspectionId + "/resources/"+imageId);
        InputStream inputStream = response.body().byteStream();
        BufferedImage image = ImageIO.read(inputStream);
        File outputfile = new File("image.jpg");
        ImageIO.write(image, "jpg", outputfile);
    }

    public static String blocklist (String applicantId, String note)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Response response = sendPost("/resources/applicants/"
                + applicantId
                + "/blacklist?note="
                + note, RequestBody.create(new byte[0], null));
        ResponseBody responseBody = response.body();
        return responseBody != null ? responseBody.string() : null;
    }
}

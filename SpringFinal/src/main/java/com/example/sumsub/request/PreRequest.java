package com.example.sumsub.request;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PreRequest {
    private static String SUMSUB_SECRET_KEY;
    private static String SUMSUB_APP_TOKEN;
    private static String SUMSUB_TEST_BASE_URL;

    static Response sendPost(String url, RequestBody requestBody) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        long ts = Instant.now().getEpochSecond();
        Request request = new Request.Builder()
                .url(SUMSUB_TEST_BASE_URL + url)
                .header("X-App-Token", SUMSUB_APP_TOKEN)
                .header("X-App-Access-Sig", createSignature(ts, HttpMethod.POST, url, requestBodyToBytes(requestBody)))
                .header("X-App-Access-Ts", String.valueOf(ts))
                .post(requestBody)
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        if (response.code() != 200 && response.code() != 201) {
        }
        return response;
    }

    static Response sendGet(String url) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        long ts = Instant.now().getEpochSecond();
        Request request = new Request.Builder()
                .url(SUMSUB_TEST_BASE_URL + url)
                .header("X-App-Token", SUMSUB_APP_TOKEN)
                .header("X-App-Access-Sig", createSignature(ts, HttpMethod.GET, url, null))
                .header("X-App-Access-Ts", String.valueOf(ts))
                .get()
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        if (response.code() != 200 && response.code() != 201) {
        }
        return response;
    }

    private static String createSignature(long ts, HttpMethod httpMethod, String path, byte[] body) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(new SecretKeySpec(SUMSUB_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        hmacSha256.update((ts + httpMethod.name() + path).getBytes(StandardCharsets.UTF_8));
        byte[] bytes = body == null ? hmacSha256.doFinal() : hmacSha256.doFinal(body);
        return Hex.encodeHexString(bytes);
    }

    private static byte[] requestBodyToBytes(RequestBody requestBody) throws IOException {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readByteArray();
    }

    @Value("${sumsub.secret_key}")
    public void setSumsubSecretKey(String key){
        SUMSUB_SECRET_KEY=key;
    }

    @Value("${sumsub.app_token}")
    public void setSumsubAppToken(String token){
        SUMSUB_APP_TOKEN=token;
    }

    @Value("${sumsub.url}")
    public void setSumsubTestBaseUrl(String url){
        SUMSUB_TEST_BASE_URL=url;
    }
}

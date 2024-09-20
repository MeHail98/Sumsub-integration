package com.example;
import com.example.sumsub.request.Request;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class ApplicationSumSub {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        var context = SpringApplication.run(ApplicationSumSub.class, args);
        Request.blocklist("66d898939e2675747ca7b82d","some");
    }
}

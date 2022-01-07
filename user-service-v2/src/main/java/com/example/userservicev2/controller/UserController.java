package com.example.userservicev2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@RestController
@EnableDiscoveryClient
public class UserController {


    @Autowired
    private EurekaClient eurekaClient;



    @PostMapping("/create-account")
    public Object createAccount(
            @RequestBody String body
    ) throws URISyntaxException {

        try {
            return send("/create-account", body).body();
        } catch (ResponseStatusException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return e.getMessage() + sw.toString();
        }

    }

    @GetMapping("/test")
    public Object test(){
        return ResponseEntity.ok("received");
    }

    private String getBaseUrl() {
        Application application = eurekaClient.getApplication("data-access");

        InstanceInfo instanceInfo = application.getInstances().get(0);
        String hostname = instanceInfo.getHostName();

        int port = instanceInfo.getPort();

        return "http://" + hostname + ":" + port;
    }

    private HttpResponse<String> send(String path, String body) {
        if (getBaseUrl() == null) {
            return null;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        } catch (IOException e) {
            System.out.println("IO exception");
        }
        return null;
    }
}

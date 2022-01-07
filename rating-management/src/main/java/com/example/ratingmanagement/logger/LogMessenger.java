package com.example.ratingmanagement.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
@Service
public class LogMessenger {

    @Autowired
    private EurekaClient eurekaClient;

    private static final String logManagementServiceName = "log-management";

    private String getBaseUrl(){
        Application application = eurekaClient.getApplication(logManagementServiceName);
        if(application == null || application.getInstances().isEmpty()){
            return null;
        }
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort();
    }

    public void send(List<LogCollector.Log> logList){

        String json = new Gson().toJson(logList);

        if(getBaseUrl() == null){
            throw new RuntimeException("Could not get Log Management url");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/add"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200){
                throw new RuntimeException("Error response from Log Management: " + response.body() + " for request: " + json);
            }

        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

}

package com.example.recommendationprocessingservice;

import com.example.recommendationprocessingservicecontroller.LogCollector;
import com.example.recommendationprocessingservicecontroller.RecommendationProcessingController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages={
        "com.example.recommendationprocessingservice", "com.example.recommendationprocessingservicecontroller"})
@RestController
@EnableDiscoveryClient
public class RecommendationProcessingServiceApplication {

    @Autowired
    RecommendationProcessingController controller;

    public static void main(String[] args) {
        SpringApplication.run(RecommendationProcessingServiceApplication.class, args);
    }

    @GetMapping("/checkrecommended")
    public Object checkRecommended(
            @RequestParam(value = "token") String token
    ) {
        try {
            HttpResponse<String> responseBody = controller.checkRecommended(token);
            return ResponseEntity.ok(responseBody.body());

        } catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return  ResponseEntity.ok(e.getMessage()+sw.toString());
        }
    }

    @PostMapping("/modifymultipliers")
    public Object modifyMultipliers(
            @RequestBody String body
    ){
        try{
            Map<String,Double> multipliers =
                    new ObjectMapper().readValue(body, HashMap.class);
            controller.modifyMultipliers(multipliers);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return e.getMessage()+sw.toString();
        }

    }

    @GetMapping("/getmultipliers")
    public Object getMultipliers(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(controller.getMultipliers());
            return ResponseEntity.ok(jsonResult);
        } catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return e.getMessage()+sw.toString();
        }
    }

}

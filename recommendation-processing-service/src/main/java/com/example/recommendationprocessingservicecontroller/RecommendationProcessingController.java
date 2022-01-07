package com.example.recommendationprocessingservicecontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Component
@Service
public class RecommendationProcessingController {

    @Autowired
    private EurekaClient eurekaClient;

    private static final Map<String, Double> multipliers = new HashMap<>();

    private static void setMultiplier(String key, Double value){
        multipliers.put(key, value);
    }

    public void modifyMultipliers(Map<String, Double> multipliers){
        for(String key : multipliers.keySet()){
            setMultiplier(key, multipliers.get(key));
        }
    }



    public HttpResponse<String> checkRecommended(String token) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException{

        String url = getURL(token);
        return send(url, null);

    }

    private String getURL(String token) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {

        ObjectMapper mapper = new ObjectMapper();
        HttpGet httpGet = new HttpGet(getBaseUrl()+"/check-recommendation");
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("id", token)
                .addParameter("amountOfMovies", "10")
                .addParameter("multipliers", mapper.writer()
                        .writeValueAsString(getMultipliers()))
                .build();

        return uri.toString();
    }

    private static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }



    public static Map<String, Double> getMultipliers(){
        return multipliers;
    }

    private static Double getMultiplier(String key){
        return multipliers.containsKey(key) ? multipliers.get(key) : 1;
    }

    private String getBaseUrl(){
        Application application = eurekaClient.getApplication("data-access");
        if(application == null || application.getInstances().isEmpty()){
            return null;
        }
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort();
    }

    private HttpResponse<String> send(String path, String body){

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (InterruptedException e){
            System.out.println("interrupted");
        } catch (IOException e){
            System.out.println("IO exception");
        }
        return null;
    }



}

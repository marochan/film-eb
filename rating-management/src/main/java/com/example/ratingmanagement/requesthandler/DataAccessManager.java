package com.example.ratingmanagement.requesthandler;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class DataAccessManager {

    @Autowired
    private EurekaClient eurekaClient;

    private static final String dataAccessServiceName = "data-access";

    private static final Map<String, String> serviceToPathMap = new HashMap<String, String>(){{
        put("search", "/search");
        put("add", "/add-movie");
        put("rating", "/getrating");
        put("rate", "/update-rating");
    }};

    private static final Map<String, String> serviceToMethodMap = new HashMap<String, String>(){{
        put("search", "GET");
        put("add", "POST");
        put("rating", "GET");
        put("rate", "POST");
    }};

    public HttpResponse<String> callDA(String service, String body) {
        return callDA(service, new HashMap<String, String>(), body);
    }

    public HttpResponse<String> callDA(String service, Map<String, String> uriParams, String body) {

        try {

            URI uri = generateURI(service, uriParams);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(uri);

            if (serviceToMethodMap.get(service) == "GET") {
                builder.GET();
            } else if (serviceToMethodMap.get(service) == "POST") {
                builder.POST(HttpRequest.BodyPublishers.ofString(body));
            } else {
                throw new IllegalArgumentException("Unknown service: " + service);
            }

            HttpClient client = HttpClient.newHttpClient();

            return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            //log exception
            throw new RuntimeException("Unable to reach data access. Try again later :^). " + e.getMessage());
        }

    }

    private URI generateURI(String service, Map<String, String> uriParams) throws URISyntaxException {

        URIBuilder builder = new URIBuilder(getBaseURI() + serviceToPathMap.get(service));

        for(String key : uriParams.keySet()){
            builder.addParameter(key, uriParams.get(key));
        }

        return builder.build();

    }

    private String getBaseURI(){
        Application application = eurekaClient.getApplication(dataAccessServiceName);
        if(application == null || application.getInstances().isEmpty()){
            throw new RuntimeException("Data access service could not be located.");
        }
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort();
    }

}

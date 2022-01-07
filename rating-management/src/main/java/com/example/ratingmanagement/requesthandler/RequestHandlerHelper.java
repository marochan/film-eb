package com.example.ratingmanagement.requesthandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class RequestHandlerHelper {

    public String generateAddMovieRequestBody(String name, String year){

        Map<String, String> bodyMap = new HashMap<>();

        bodyMap.put("name", name);
        bodyMap.put("year", year);

        bodyMap.put("createDate", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        for(int i = 1; i<=6; i++){
            bodyMap.put("cat"+i, "3");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Could not process JSON: " + bodyMap.toString() + e.getMessage());
        }
    }

}

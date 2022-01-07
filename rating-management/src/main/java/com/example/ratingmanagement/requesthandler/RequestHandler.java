package com.example.ratingmanagement.requesthandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class RequestHandler {

    @Autowired
    private DataAccessManager dataAccessManager;

    @Autowired
    private RequestHandlerHelper helper;

    public Object search(String searchPhrase){

        Map<String, String> params = new HashMap<>();

        params.put("searchPhrase", searchPhrase);

        HttpResponse<String> response = dataAccessManager.callDA("search", params, null);

        if(response.statusCode() != 200){
            throw new RuntimeException("Search request was made but the response was unsuccessful. " + response.body());
        }

        return response.body();
    }

    public void addMovie(String basicInfoJson) throws JsonProcessingException {

        Map<String,String> basicInfo =
                new ObjectMapper().readValue(basicInfoJson, HashMap.class);

        HttpResponse<String> response = dataAccessManager.callDA("add", helper.generateAddMovieRequestBody(basicInfo.get("name"), basicInfo.get("year")));

        if(response.statusCode() != 200){
            throw new RuntimeException("Add request was made but the response was unsuccessful. " + response.body());
        }

    }

    public Object getRating(String movieId){

        Map<String, String> params = new HashMap<>();

        params.put("movieId", movieId);

        HttpResponse<String> response = dataAccessManager.callDA("rating", params, null);

        if(response.statusCode() != 200){
            throw new RuntimeException("Get rating request was made but the response was unsuccessful. " + response.body());
        }

        return response.body();

    }

    public void updateRating(String body) throws JsonProcessingException {

        HttpResponse<String> response = dataAccessManager.callDA("rate", body);

        if(response.statusCode() != 200){
            throw new RuntimeException("Rate request was made but the response was unsuccessful. " + response.body());
        }
    }

}

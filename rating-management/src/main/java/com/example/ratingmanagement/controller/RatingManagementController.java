package com.example.ratingmanagement.controller;

import com.example.ratingmanagement.logger.LogCollector;
import com.example.ratingmanagement.requesthandler.RequestHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;


@RestController
@EnableDiscoveryClient
public class RatingManagementController {

    @Autowired
    private RequestHandler handler;

    @Autowired
    private LogCollector logCollector;

    @GetMapping("/search")
    public Object search(
            @RequestParam(value = "searchPhrase") String searchPhrase
    ) {
        try {

            logCollector.addLog("INFO", "Search requested for searchPhrase: " + searchPhrase);
            Object response = handler.search(searchPhrase);
            logCollector.addLog("INFO", "Search processed for searchPhrase: " + searchPhrase);
            logCollector.release();

            return response;

        } catch (Exception e){

            return handleExceptionResponse(e);

        }
    }

    @PostMapping("/add")
    public Object addMovie(
            @RequestBody String body
    ){
        try {

            logCollector.addLog("INFO", "Movie post request received.");
            handler.addMovie(body);
            logCollector.addLog("INFO", "Movie post request processed.");

            logCollector.release();
            return ResponseEntity.ok().build();

        } catch (Exception e){

            return handleExceptionResponse(e);

        }
    }

    @PostMapping("/rate")
    public Object rate(
            @RequestBody String body
    ){
        try {

            logCollector.addLog("INFO", "Rating update request received");
            handler.updateRating(body);
            logCollector.addLog("INFO", "Rating update request processed");

            logCollector.release();
            return ResponseEntity.ok().build();

        } catch (Exception e){

            return handleExceptionResponse(e);

        }
    }

    @GetMapping("/logtest")
    public Object logtest(){
        try {

            logCollector.addLog("INFO", "test log");
            logCollector.release();
            return ResponseEntity.ok().build();

        } catch (Exception e){

            return handleExceptionResponse(e);

        }
    }


    private Object handleExceptionResponse(Exception e){

        //logCollector.addLog(e);
        //logCollector.release();

        return "Exception occured and has been logged: " + e.getMessage();
    }

}

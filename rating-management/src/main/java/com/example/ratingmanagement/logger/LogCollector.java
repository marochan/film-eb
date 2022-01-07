package com.example.ratingmanagement.logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Scope("singleton")
public class LogCollector{

    @Autowired
    private LogMessenger messenger;

    @Value("${spring.application.name}")
    private String appName;

    public class Log{
        private String origin;
        private String type;
        private String stackTrace;
        private String message;
        private Timestamp timestamp;

        public Log(String type, String message, String stackTrace){
            this.type = type;
            this.stackTrace = stackTrace;
            this.message = message;
            this.timestamp = new Timestamp(System.currentTimeMillis());
            this.origin = appName;
        }

    }

    private List<Log> logs = new ArrayList<>();

    public LogCollector(){}

    public void addLog(Exception e){
        String stackTrace = Arrays.toString(e.getStackTrace());
        addLog("ERROR", e.getMessage(), stackTrace);
    }

    public void addLog(String type, String message){
        String stackTrace = Arrays.toString(Thread.currentThread().getStackTrace());
        addLog(type, message, stackTrace);
    }

    public void addLog(String type, String message, String stackTrace){
        logs.add(new Log(type, message, stackTrace.substring(0, Math.min(stackTrace.length(), 250))));
    }

    public void release(){

        if(logs.isEmpty()){
            return;
        }

        messenger.send(logs);

        logs.clear();
    }
}
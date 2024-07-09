package com.pgsintl.supplychaintracking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
public class TwilioConfig {


    private String accountSid="ACa4a43badb4ea62bd6a9173ce6ad36543";
    private String authToken="d466cbf27bf02624d8e0994705a8c85d";
    private String trialNumber="+14845467140";
}
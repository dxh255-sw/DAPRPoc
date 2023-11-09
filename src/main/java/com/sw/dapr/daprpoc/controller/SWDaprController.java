package com.sw.dapr.daprpoc.controller;


import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@RestController
public class SWDaprController
{

    private static final HttpClient httpClient = HttpClient.newBuilder()
                                                           .version(HttpClient.Version.HTTP_2)
                                                           .connectTimeout(Duration.ofSeconds(10))
                                                           .build();


    @PostMapping("/dapr/local/secretBySDK")
    @ResponseBody
    public static ResponseEntity showSecretBySDK(@RequestBody String body)
    {
        System.out.println("Creating DaprClient");
        DaprClient client = new DaprClientBuilder().build();
        System.out.println("Attempting to Connect ");
        Map<String, String> secret = client.getSecret("localsecretstore", "secret").block();
        System.out.println("Connected ");
        System.out.println("Fetched Secret: " + secret);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/dapr/local/secretByURL")
    @ResponseBody
    public static ResponseEntity showSecretByURL(@RequestBody String body) throws Exception
    {
        URI secretStoreURI = new URI("http://localhost:3005/v1.0/secrets/localsecretstore/secret");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(secretStoreURI)
                                         .build();
        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Fetched Secret: " + response.body().toString());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/dapr/azure/secretBySDK")
    @ResponseBody
    public static ResponseEntity showSecretBySDKAzure(@RequestBody String body)
    {
        System.out.println("Creating DaprClient");
        DaprClient client = new DaprClientBuilder().build();
        System.out.println("Attempting to Connect ");
        Map<String, String> secret = client.getSecret("azurekeyvault", "HEARS-DB-URL").block();
        System.out.println("Connected ");
        System.out.println("Fetched Secret: " + secret);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/dapr/azure/secretByURL")
    @ResponseBody
    public static ResponseEntity showSecretByURLAzure(@RequestBody String body) throws Exception
    {
        URI secretStoreURI = new URI("http://localhost:3005/v1.0/secrets/azurekeyvault/HEARS-DB-URL");
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(secretStoreURI)
                                         .build();
        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Fetched Secret: " + response.body().toString());
        return ResponseEntity.ok(HttpStatus.OK);
    }

}


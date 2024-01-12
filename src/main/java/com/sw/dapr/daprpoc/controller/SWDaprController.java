package com.sw.dapr.daprpoc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.dapr.daprpoc.beans.Root;
import com.sw.dapr.daprpoc.beans.VaultItem;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
public class SWDaprController
{
    @PostMapping("/secrets")
    @ResponseBody
    public static ResponseEntity findSecrets(@RequestBody Map<String, String> secretJson)
    {
        Root root = new Root();

        System.out.println("Searching vaults for secrets");
        String secretKey =  secretJson.get("secretKey");
        System.out.println("Looing for key " + secretKey);

        String hashiCorpVault = "hashicorpvault";
        String azureKeyVault = "azurekeyvault";

        DaprClient client = new DaprClientBuilder().build();

        System.out.println("Attempting to Connect to HashiCorp");
        try
        {
            Map<String, String> hashiCorpSecret = client.getSecret(hashiCorpVault, secretKey).block();

            if(hashiCorpSecret != null && !hashiCorpSecret.isEmpty())
            {
                buildJSONReposnse(root, hashiCorpSecret,hashiCorpVault);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }


        System.out.println("Attempting to Connect to AzureKey Vault");
        try
        {
            Map<String, String> azureSecretsecret = client.getSecret(azureKeyVault, secretKey).block();

            if(azureSecretsecret != null && !azureSecretsecret.isEmpty())
            {
                buildJSONReposnse(root, azureSecretsecret,azureKeyVault);
            }
        }
            catch (Exception e)
        {
            System.out.println(e);
        }

        String jsonResponse = null;

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            jsonResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        if(jsonResponse == null)
        {
            System.out.println("JSON Response was null");
            jsonResponse = "{ \"results\": [ }";
        }

        System.out.println("Returning value");
        return ResponseEntity.ok(jsonResponse);
    }

    public static void buildJSONReposnse(Root root,Map<String, String> secretMapResponse, String vault)
    {
        for (Map.Entry<String, String> entry : secretMapResponse.entrySet()) {
            VaultItem item = new VaultItem();
            item.setVault(vault);
            item.setKey(entry.getKey());
            item.setPassword(entry.getValue());
            root.getResults().add(item);
        }
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

    @PostMapping("/dapr/hashicorp/secretBySDK")
    @ResponseBody
    public static ResponseEntity showSecretBySDKHashicorp(@RequestBody Map<String, String> secretJson)
    {
        System.out.println("Creating DaprClient");
        DaprClient client = new DaprClientBuilder().build();
        System.out.println("Attempting to Connect ");
        System.out.println("vault key is " + secretJson.get("vaultKey"));
        System.out.println("secret key is " + secretJson.get("secretKey"));
        Map<String, String> secretValue = client.getSecret(secretJson.get("vaultKey"), secretJson.get("secretKey")).block();
        System.out.println("Connected ");
        System.out.println("Fetched Secret: " + secretValue);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/showHeaders")
    public void showHeaders(@RequestHeader Map<String, String> headers) {
        headers.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}

package be.uantwerpen.node.service;

import java.io.IOException;
import java.util.Scanner;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class Menu {

    @Autowired
    Lifecycle lifecycle;

    RestTemplate template;
    int serverPort = 8000;
    String serverIp = "127.0.0.1";

    @PostConstruct
    public void init() {

        this.template = new RestTemplate();

        boolean running = true;
        Scanner sc = new Scanner(System.in);

        while (running) {
            System.out.println("\nWhat file's ownership do you want to get? (or 'sh')");
            String input = sc.nextLine();
            if (input.equals("sh")) {
                try {
                    lifecycle.shutdown();
                } catch (IOException e) {
                    System.out.println("Could not send other nodes shutdown message!");
                } finally {
                    System.out.println("Shutting down...");
                    System.exit(0);
                }
            } else {
                try {
                    ResponseEntity<String> ownerIp = template
                            .getForEntity("http://" + serverIp + ":" + serverPort + "/getfile/" + input, String.class);
                    System.out.println("Owner's IP: " + ownerIp);
                } catch (RestClientException e) {
                    System.out.println(e.getMessage());
                    // e.printStackTrace();
                    System.out.println("Could not establish connection with server!");
                }
            }
        }
        sc.close();
    }
}
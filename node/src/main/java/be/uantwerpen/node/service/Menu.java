package be.uantwerpen.node.service;

import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class Menu {

    RestTemplate template;
    int serverPort = 8080;
    String serverIp = "127.0.0.1";

    @PostConstruct
    public void init() {
        this.template = new RestTemplate();
        boolean running = true;

        Scanner sc = new Scanner(System.in);
        while (running) {
            System.out.println("\nWhat file's ownership do you want to get?");
            String fileName = sc.nextLine();
            
            try {
                ResponseEntity<String> ownerIp = template
                        .getForEntity("http://" + serverIp + ":" + serverPort + "/getfile/" + fileName, String.class);
                System.out.println("Owner's IP: " + ownerIp);
            } catch (RestClientException e) {
                e.printStackTrace();
                System.out.println("Could not establish connection with server!");
            }
        }
        sc.close();
    }
}
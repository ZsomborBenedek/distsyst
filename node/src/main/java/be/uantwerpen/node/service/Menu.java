package be.uantwerpen.node.service;

import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Menu {

    RestTemplate template;

    @PostConstruct
    public void init() {
        this.template = new RestTemplate();
        boolean running = true;

        int serverPort = 8080;

            System.out.println("What file's ownership do you want to get?");
            Scanner sc = new Scanner(System.in);
            String fileName = sc.nextLine();

            ResponseEntity<String> ownerIp = template.getForEntity("http://127.0.0.1:" + serverPort + "/getfile/" + fileName,
                    String.class);
            System.out.println("Owner's IP: " + ownerIp);
            sc.close();
        
    }
}
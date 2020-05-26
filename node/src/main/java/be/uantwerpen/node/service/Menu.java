package be.uantwerpen.node.service;

import java.io.IOException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import be.uantwerpen.node.Node;

@Service
public class Menu implements Runnable {

    @Autowired
    Node node;

    RestTemplate template;

    public Menu(Node node) {
        this.template = new RestTemplate();
    }

    @Override
    public void run() {
        boolean running = true;
        Scanner sc = new Scanner(System.in);

        while (running) {
            System.out.println("\nWhat file's ownership do you want to get? (or 'sh')");
            String input = sc.nextLine();
            if (input.equals("sh")) {
                try {
                    node.shutdown();
                } catch (IOException e) {
                    System.out.println("Could not send other nodes shutdown message!");
                } finally {
                    System.out.println("Shutting down...");
                    System.exit(0);
                }
            } else {
                try {
                    ResponseEntity<String> ownerIp = template.getForEntity(
                            "http://" + node.getServerIp() + ":" + node.getServerPort() + "/getFile/" + input,
                            String.class);
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
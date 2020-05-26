package be.uantwerpen.node.controller;

import org.springframework.web.bind.annotation.RestController;
import be.uantwerpen.node.Node;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class NodeController {

    @Autowired
    Node node;

    @PostMapping(value = "/setNameServer")
    public ResponseEntity<String> setNameServer(@RequestBody String ip) {
        try {
            this.node.setServerIp(ip);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("\nName server's ip set to: " + this.node.getServerIp());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
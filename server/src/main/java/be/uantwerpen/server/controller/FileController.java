package be.uantwerpen.server.controller;

import org.springframework.web.bind.annotation.RestController;
import be.uantwerpen.server.service.NodeLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@ComponentScan
public class FileController {

    @Autowired
    NodeLogic logic;

    @GetMapping(value = "getfile/{fileName}")
    public ResponseEntity<String> getFileOwnerIp(@PathVariable String fileName) {
        System.out.println("Searched file's name: " + fileName);
        String ownerIp = this.logic.getFileOwnerIp(fileName);
        System.out.println("Searched owner's ip: " + ownerIp + "\n");
        return new ResponseEntity<String>(ownerIp, HttpStatus.OK);
    }
}
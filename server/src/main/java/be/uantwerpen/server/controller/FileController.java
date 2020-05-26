package be.uantwerpen.server.controller;

import org.springframework.web.bind.annotation.RestController;
import be.uantwerpen.server.service.ServerLogic;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@ComponentScan
public class FileController {

    @Autowired
    ServerLogic logic;

    @GetMapping(value = "LocateFile/{fileName}")
    public ResponseEntity<String> getFileOwnerIp(@PathVariable String fileName,
            @RequestParam(value = "remove", defaultValue = "false") String remove) {
        if (!fileName.isEmpty()) {
            System.out.println("Searched file's name: " + fileName);
            String ownerIp = this.logic.getFileOwnerIp(fileName);
            System.out.println("Searched owner's ip: " + ownerIp + "\n");
            return new ResponseEntity<>(ownerIp, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
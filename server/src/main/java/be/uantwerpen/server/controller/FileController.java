package be.uantwerpen.server.controller;

import org.springframework.web.bind.annotation.RestController;
import be.uantwerpen.server.service.NodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class FileController {

    NodeRepository nodeRepository;

    public FileController() {
        this.nodeRepository = new NodeRepository();
    }

    @GetMapping(value = "getfile/{fileName}")
    public ResponseEntity<String> getFileOwnerIp(@PathVariable String fileName) {
        String ownerIp = nodeRepository.getFileOwnerIp(fileName);
        return new ResponseEntity<String>(ownerIp, HttpStatus.OK);
    }
}
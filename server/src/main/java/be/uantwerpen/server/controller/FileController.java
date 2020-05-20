package be.uantwerpen.server.controller;

import org.springframework.web.bind.annotation.RestController;
import be.uantwerpen.server.service.NodeLogic;
// import be.uantwerpen.server.service.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class FileController {

    @Autowired
    NodeLogic logic;

    // public FileController(NodeLogic logic) {
    //     this.logic = new NodeLogic(new NodeRepository());
    // }

    @GetMapping(value = "getfile/{fileName}")
    public ResponseEntity<String> getFileOwnerIp(@PathVariable String fileName) {
        System.out.println("Searched file's name: " + fileName);
        String ownerIp = this.logic.getFileOwnerIp(fileName);
        System.out.println("Searched owner's ip: " + ownerIp + "\n");
        return new ResponseEntity<String>(ownerIp, HttpStatus.OK);
    }
}
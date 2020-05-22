package be.uantwerpen.server.service;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class NodeLogic {

    NodeRepository repository;

    public NodeLogic(NodeRepository repository) {
        this.repository = repository;
        System.out.println();
        System.out.println("Known nodes:");
        System.out.println(this.repository.getNodes().toString());
        System.out.println();
    }

    public String getFileOwnerIp(String fileName) {
        int hfn = hash(fileName, false);
        System.out.println("Hashed file name: " + hfn);

        String ownerIp = null;
        int minDiff = Integer.MAX_VALUE;
        for (Integer name : this.repository.getNodes().keySet()) {
            // Check if hashed node name is smaller than hashed file name
            if (name < hfn) {
                int diff = Math.abs(name - hfn);
                if (diff < minDiff) {
                    minDiff = diff;
                    ownerIp = this.repository.getNodes().get(name);
                }
            }
        }

        // If no node has a hash smaller than the file name's hash (= every node has a
        // bigger hash)
        if (ownerIp == null) {
            Integer name = Collections.max(this.repository.getNodes().keySet());
            return this.repository.getNodes().get(name);
        }
        return ownerIp;
    }

    private int hash(String name, boolean node) {
        int hash = 0;
        int temp = 0;
        int i;
        for (i = 0; i < name.length(); i++) {
            hash = 3 * hash + name.charAt(i);
            temp = temp + name.charAt(i);
        }
        hash = hash + temp;
        if (node) {
            // System.out.println("--Node hashed--");
        } else
            hash = hash / 53;
        return hash;
    }
}
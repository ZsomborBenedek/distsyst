package be.uantwerpen.server.service;

import java.util.Collections;
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

        Integer min = Collections.min(this.repository.getNodes().keySet());
        if (hfn < min) {
            int ownerName = Collections.max(this.repository.getNodes().keySet());
            return this.repository.getNodes().get(ownerName);
        }

        String ownerIp = null;
        int minDiff = Integer.MAX_VALUE;
        for (Integer name : this.repository.getNodes().keySet()) {
            String ip = this.repository.getNodes().get(name);
            int diff = Math.abs(name - hfn);
            if (diff < minDiff) {
                minDiff = diff;
                ownerIp = ip;
            }
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
            System.out.println("node");
        } else
            hash = hash / 53;
        return hash;
    }
}
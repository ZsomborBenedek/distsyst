package be.uantwerpen.server.service;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import be.uantwerpen.server.repository.NodeRepository;

@Service
public class ServerLogic {

    @Autowired
    NodeRepository repository;

    @Autowired
    TCPSender tpcSender;

    public int addNode(String name, String ip) {
        System.out.println("\nAdding new node...");
        System.out.println(name + "::" + ip);

        int hName = hash(name, true);

        // Adding node to database
        ConcurrentHashMap<Integer, String> nodes = this.repository.getNodes();
        nodes.put(hName, ip);
        this.repository.setNodes(nodes);

        setHighestLowest(hName);

        // Sending node this name server's ip address
        tpcSender.post(ip, "setNameServer", this.repository.getOwnIp());

        return this.repository.getNodes().size();
    }

    public int deleteNode(String name) {
        System.out.println("\nRemoving a node...");
        System.out.println(name);

        int hName = hash(name, true);

        // Removing node from database
        ConcurrentHashMap<Integer, String> nodes = this.repository.getNodes();
        nodes.remove(hName, true);
        this.repository.setNodes(nodes);

        setHighestLowest(hName);

        return this.repository.getNodes().size();
    }

    private void setHighestLowest(int hName) {
        if (hName > this.repository.getHighestHashedName()) {
            this.repository.setHighestHashedName(hName);
            System.out.println("This is now the highest hashed node!");
        }
        if (hName < this.repository.getLowestHashedName()) {
            this.repository.setLowestHashedName(hName);
            System.out.println("This is now the lowest hashed node!");
        }
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
        return hash > 327680 || hash < 0 ? 327680 : hash;
    }
}
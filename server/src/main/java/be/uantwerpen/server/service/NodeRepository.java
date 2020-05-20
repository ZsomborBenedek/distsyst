package be.uantwerpen.server.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class NodeRepository {

    private HashMap<Integer, String> nodes;

    public NodeRepository() {
        this.nodes = new HashMap<>();
        read();
    }

    public NodeRepository(HashMap<Integer, String> nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the nodes
     */
    public HashMap<Integer, String> getNodes() {
        read();
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(HashMap<Integer, String> nodes) {
        this.nodes = nodes;
        save();
    }

    private void save() {
        try {
            File nodes = new File("src/main/java/be/uantwerpen/server/database/nodes.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(nodes.getAbsolutePath(), false));

            for (Map.Entry<Integer, String> node : this.nodes.entrySet()) {
                Integer name = node.getKey();
                String ip = node.getValue();

                writer.write(name.toString());
                writer.write(":");
                writer.write(ip);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred writing the files!");
        }
    }

    private void read() {
        try {
            File nodes = new File("src/main/java/be/uantwerpen/server/database/nodes.txt");
            BufferedReader reader = new BufferedReader(new FileReader(nodes.getAbsolutePath()));

            this.nodes.clear();
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty())
                    this.nodes.put(Integer.parseInt(line.split(":")[0]), line.split(":")[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred reading the files!");
        }
    }

    public String getFileOwnerIp(String fileName) {
        int hfn = hash(fileName, false);

        System.out.println("itt keressuk a file ipját");
        Integer min = Collections.min(this.nodes.keySet());
        if (hfn < min) {
            int ownerName = Collections.max(this.nodes.keySet());
            return this.nodes.get(ownerName);
        }

        String ownerIp = null;
        int minDiff = Integer.MAX_VALUE;
        for (Integer name : this.nodes.keySet()) {
            String ip = this.nodes.get(name);
            int diff = Math.abs(name-hfn);
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
package be.uantwerpen.server.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class NodeRepository {

    private ConcurrentHashMap<Integer, String> nodes;

    public NodeRepository() {
        this.nodes = new ConcurrentHashMap<>();
        read();
    }

    public NodeRepository(ConcurrentHashMap<Integer, String> nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the nodes
     */
    public ConcurrentHashMap<Integer, String> getNodes() {
        read();
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(ConcurrentHashMap<Integer, String> nodes) {
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
                    this.nodes.put(hash(line.split(":")[0], true), line.split(":")[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred reading the files!");
        }
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
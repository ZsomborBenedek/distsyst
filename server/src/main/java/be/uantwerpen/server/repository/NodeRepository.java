package be.uantwerpen.server.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class NodeRepository {

    private ConcurrentHashMap<Integer, String> nodes;
    private int highestHashedName = 0;
    private int lowestHashedName = Integer.MAX_VALUE;

    private final InetAddress inetAddress = InetAddress.getLocalHost();
    private final String ownName = inetAddress.getHostName();
    private final String ownIp = inetAddress.getHostAddress();

    public String getOwnName() {
        return ownName;
    }

    public String getOwnIp() {
        return ownIp;
    }

    public NodeRepository() throws UnknownHostException {
        this.nodes = new ConcurrentHashMap<>();
        clear();
    }

    public NodeRepository(ConcurrentHashMap<Integer, String> nodes) throws UnknownHostException {
        this.nodes = nodes;
        save();
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

    public int getHighestHashedName() {
        return highestHashedName;
    }

    public void setHighestHashedName(int highestHashedName) {
        this.highestHashedName = highestHashedName;
    }

    public int getLowestHashedName() {
        return lowestHashedName;
    }

    public void setLowestHashedName(int lowestHashedName) {
        this.lowestHashedName = lowestHashedName;
    }

    private void clear() {
        try {
            File nodes = new File("src/main/java/be/uantwerpen/server/database/nodes.txt");
            if (nodes.exists() && nodes.isFile()) {
                nodes.delete();
            }
            nodes.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred making the files!");
        }
    }

    private void save() {
        try {
            File nodes = new File("src/main/java/be/uantwerpen/server/database/nodes.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(nodes.getAbsolutePath(), false));

            for (Map.Entry<Integer, String> node : this.nodes.entrySet()) {
                String line = node.getKey().toString() + "::" + node.getValue();
                writer.write(line);
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
                    this.nodes.put(Integer.parseInt(line.split("::")[0]), line.split("::")[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred reading the files!");
        }
    }
}
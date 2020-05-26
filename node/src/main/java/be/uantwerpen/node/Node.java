package be.uantwerpen.node;

import java.io.IOException;
import java.net.InetAddress;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.uantwerpen.node.service.Menu;
import be.uantwerpen.node.service.NodeMultiCastService;

@Component
public class Node {

    @Autowired
    NodeMultiCastService multiCastService;

    String ownName;
    String ownIp;

    String serverIp;
    final int serverPort = 10000;

    final String NEW_NODE_MESSAGE = "newNode";
    final String REMOVE_NODE_MESSAGE = "remNode";

    public String getOwnName() {
        return ownName;
    }

    public String getOwnIp() {
        return ownIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Node() throws IOException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        this.ownName = inetAddress.getHostName();
        this.ownIp = inetAddress.getHostAddress();
    }
    
    @PostConstruct
    public void bootstrap() throws IOException, InterruptedException {
        multiCastService.sendUDPMessage(NEW_NODE_MESSAGE + "::" + ownName + "::" + ownIp, "230.0.0.0", 10000);
        
        Menu menu = new Menu(this);
        new Thread(menu::run).start();
    }

    public void shutdown() throws IOException {
        multiCastService.sendUDPMessage(REMOVE_NODE_MESSAGE + "::" + ownName + "::" + ownIp, "230.0.0.0", 10000);
    }
}
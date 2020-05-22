package be.uantwerpen.node.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class Lifecycle {

    String ownName;
    String ownIp;

    final String NEW_NODE_MESSAGE = "newNode";
    final String REMOVE_NODE_MESSAGE = "remNode";

    public Lifecycle() throws IOException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        this.ownName = inetAddress.getHostName();
        this.ownIp = inetAddress.getHostAddress();
    }

    @PostConstruct
    public void bootstrap() throws IOException {
        sendUDPMessage(NEW_NODE_MESSAGE + "::" + ownName + "::" + ownIp, "230.0.0.0", 10000);
    }
    
    public void shutdown() throws IOException {
        sendUDPMessage(REMOVE_NODE_MESSAGE + "::" + ownName + "::" + ownIp, "230.0.0.0", 10000);
    }

    // Send UDP messages
    private void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName(ipAddress);
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
        socket.send(packet);
        socket.close();
    }
}
package be.uantwerpen.server.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerMultiCastService implements Runnable {

    @Autowired
    ServerLogic logic;

    final String udpIP = "230.0.0.0";
    final int udpPort = 10000;

    // Starts listening on a new thread
    @PostConstruct
    public void init() {
        new Thread(this::run).start();
    }

    // UDP message: [type]::[name]::[ip]

    private void switcher(String msg) throws IOException, InterruptedException {
        int nodeCount = 0;
        switch (msg.split("::")[0]) {
            case "newNode":
                nodeCount = this.logic.addNode(msg.split("::")[1], msg.split("::")[2]);
                sendUDPMessage("nodeCount::" + nodeCount, this.udpIP, this.udpPort);
                break;
            case "remNode":
                nodeCount = this.logic.deleteNode(msg.split("::")[1]);
                sendUDPMessage("nodeCount::" + nodeCount, this.udpIP, this.udpPort);
                break;
            default:
                break;
        }
    }

    private void listenForUDPMessage(String ip, int port) throws IOException, InterruptedException {
        byte[] buffer = new byte[4096];
        MulticastSocket socket = new MulticastSocket(port);
        InetAddress group = InetAddress.getByName(ip);
        socket.joinGroup(group);
        while (true) {
            System.out.println("\nNodes:\n" + this.logic.repository.getNodes().toString());

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

            System.out.println("\nThis UDP message received: " + msg);

            switcher(msg);

            if ("OK".equals(msg))
                break;
        }
        socket.leaveGroup(group);
        socket.close();
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

    @Override
    public void run() {
        try {
            listenForUDPMessage(this.udpIP, this.udpPort);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
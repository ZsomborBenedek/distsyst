package be.uantwerpen.node.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import org.springframework.stereotype.Service;

@Service
public class NodeMultiCastService implements Runnable {

    final String udpIP = "230.0.0.0";
    final int udpPort = 10000;

    // Starts listening on a new thread
    public NodeMultiCastService() {
        new Thread(this::run).start();
    }

    // UDP message: [type]::[name]::[ip]

    private void switcher(String msg) throws IOException {
        String type = msg.split("::")[0];
        switch (type) {
            case "nodeCount":
                System.out.println("\nThis UDP message received: " + msg);

                int nodeCount = Integer.parseInt(msg.split("::")[1]);
                System.out.println("\nNode(s) in the network: " + nodeCount);
                break;
            default:
                break;
        }
    }

    private void listenForUDPMessage(String ip, int port) throws IOException {
        byte[] buffer = new byte[4096];
        MulticastSocket socket = new MulticastSocket(port);
        InetAddress group = InetAddress.getByName(ip);
        socket.joinGroup(group);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

            switcher(msg);

            if ("OK".equals(msg))
                break;
        }
        socket.leaveGroup(group);
        socket.close();
    }

    // Send UDP messages
    public void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
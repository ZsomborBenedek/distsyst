package be.uantwerpen.server.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultiCastService implements Runnable {

    @Autowired
    NodeLogic logic;

    // Starts listening on a new thread
    @PostConstruct
    public void init(){
        new Thread(this::run).start();
    }

    private void listenForUDPMessage(String ip, int port) throws IOException {
        byte[] buffer = new byte[4096];
        MulticastSocket socket = new MulticastSocket(port);
        InetAddress group = InetAddress.getByName(ip);
        socket.joinGroup(group);
        while (true) {
            System.out.println("\nNodes:\n" + this.logic.repository.getNodes().toString() + "\n");

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

            System.out.println("This UDP message received: " + msg);

            if (msg.contains("newNode"))
                this.logic.addNode(msg);
            else if (msg.contains("remNode"))
                this.logic.deleteNode(msg);
            if ("OK".equals(msg)) {
                // System.out.println("No more message. Exiting : " + msg);
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();
    }

    @Override
    public void run() {
        try {
            listenForUDPMessage("230.0.0.0", 10000);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
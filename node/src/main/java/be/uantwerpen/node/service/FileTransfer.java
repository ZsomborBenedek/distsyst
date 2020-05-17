package be.uantwerpen.node.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class FileTransfer {

    File[] files;
    int receivePort = 8000;

    public FileTransfer() {
        readFiles();
    }

    /**
     * @return the files
     */
    public File[] getFiles() {
        return files;
    }

    private void readFiles() {
        File folder = new File("src/main/java/be/uantwerpen/node/ownFiles");
        this.files = folder.listFiles();
    }

    private void sendFile(String ip, int port, File file) throws IOException {
        readFiles();

        Socket socket = new Socket(ip, port);

        //creating input and output streams
        InputStream input = new DataInputStream(new FileInputStream(file.getAbsolutePath()));
        OutputStream output = new DataOutputStream(socket.getOutputStream());
        
        //sending file name
        DataOutputStream dataOutput = new DataOutputStream(output);
        dataOutput.writeUTF(file.getName());
        dataOutput.flush();

        //sending file
        int count;
        byte[] buffer = new byte[8192];
        while ((count = input.read(buffer)) > 0) {
            output.write(buffer, 0, count);
        }

        input.close();
        socket.close();
    }

    public void sendFiles(String ip, int port) throws IOException {
        for (File file : this.files) {
            sendFile(ip, port, file);
        }
    }

    @PostConstruct
    private void receiveFiles() throws IOException {
        File folder = new File("src/main/java/be/uantwerpen/node/receivedFiles");
        ServerSocket serverSocket = new ServerSocket(receivePort);
        while (true) {
            Socket socket = serverSocket.accept();

            //getting input stream
            InputStream input = socket.getInputStream();

            // receiving file name
            DataInputStream clientData = new DataInputStream(input);
            String fileName = clientData.readUTF();

            //creating file with file name and making it the output stream
            File file = new File(folder.getAbsolutePath() + "/" + fileName);
            OutputStream output = new FileOutputStream(file);

            // receiving file
            int count;
            byte[] buffer = new byte[8192];
            while ((count = input.read(buffer)) >= 0) {
                output.write(buffer, 0, count);
            }

            output.close();
        }
    }
}
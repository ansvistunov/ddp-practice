package tcp;

import java.io.*;
import java.net.*;

class ClientConnection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public ClientConnection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("tcp.ClientConnection:" + e.getMessage());
        }
    }

    public void run() { // an echo server
        try {
            String data = in.readUTF(); // read a line of data from the stream
            System.out.println("recieved:"+data);
            out.writeUTF(data); // write a line to the stream
            clientSocket.close();
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        }
    }
}

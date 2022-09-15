package udp;

import java.net.*;
import java.io.*;

public class UDPServer {
    public static void main(String args[]) {
        try (DatagramSocket aSocket = new DatagramSocket(8080)) {
            // create socket at agreed port
            byte[] buffer = new byte[1000];
            while (true) {

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                System.out.println(new String(request.getData()));
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
                        request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage()); // socket creation failed
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

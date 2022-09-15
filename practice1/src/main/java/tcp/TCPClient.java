package tcp;

import java.net.*;
import java.io.*;

public class TCPClient {
    public static void main(String args[]) {
        // arguments supply message and hostname
        int serverPort = 8080;
        String serverHost = "132.145.228.39";//args[1];
        String message = "Hello from tcp client!!!!";//args[0];
        try (Socket s = new Socket(serverHost, serverPort)) {
            System.out.println("Connected to "+serverHost);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF(message); // UTF is a string encoding
            String data = in.readUTF(); // read a line of data from the stream
            System.out.println("Received: " + data);
        } catch (UnknownHostException e) {
            System.out.println("Socket:" + e.getMessage()); // host cannot be resolved
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage()); // end of stream reached
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage()); // error in reading the stream
        }
    }
}

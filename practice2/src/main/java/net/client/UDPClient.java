package net.client;

import net.command.SerializableCommand;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author : Alex
 * @created : 24.03.2021, среда
 **/
public class UDPClient {
    private final DatagramSocket socket;
    private final String serverHost;
    private final int serverPort;
    private final static int packetLength = 1000;

    public UDPClient(String serverHost, int serverPort) throws Exception{
        this.socket = new DatagramSocket();
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public <T> T executeCommand(SerializableCommand command){
        //int[] buffer = new int[packetLength];

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(packetLength);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(command);
            oos.close();
            byte[] buffer = baos.toByteArray();
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverHost), serverPort);
            socket.send(request);


            DatagramPacket reply = new DatagramPacket(new byte[1000], 1000);
            //System.out.println("wait for reply "+command.carIndex);
            socket.receive(reply);
            ByteArrayInputStream bais = new ByteArrayInputStream(reply.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object ret = ois.readObject();
            return (T)ret;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

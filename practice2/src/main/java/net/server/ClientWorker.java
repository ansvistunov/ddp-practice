package net.server;

import car.BasicCarServer;
import car.command.Command;
import net.command.SerializableCommand;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author : Alex
 * @created : 23.03.2021, вторник
 **/
public class ClientWorker implements Runnable {
    private final DatagramPacket packet;
    private final BasicCarServer carServer;
    private final DatagramSocket socket;


    //private

    public ClientWorker(DatagramPacket packet, BasicCarServer carServer, DatagramSocket socket){
        this.packet = packet;
        this.carServer = carServer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            SerializableCommand o = (SerializableCommand)ois.readObject();
            Command command = Command.createCommand(carServer.getCar(o.carIndex), o.commandName+" "+o.commandparameter);
            Object ret = command.execute();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ret);
            oos.close();
            byte[] data = baos.toByteArray();
            DatagramPacket reply = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
            socket.send(reply);
            //System.out.println("reply sended "+command.getCar().getIndex());
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

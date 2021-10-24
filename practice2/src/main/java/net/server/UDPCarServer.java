package net.server;

import car.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author : Alex
 * @created : 23.03.2021, вторник
 **/
public class UDPCarServer extends BasicCarServer implements Runnable{
    private final int port;


    public UDPCarServer(FieldMatrix fieldMatrix, CarEventsListener carEventsListener, int port){
        super(fieldMatrix,carEventsListener);
        this.port = port;
        new Thread(this).start();
    }

    public static void main(String[] args) {
        InputStream is = CarPainter.class.getClassLoader().getResourceAsStream("Field10x10.txt");
        FieldMatrix fm = FieldMatrix.load(new InputStreamReader(is));
        CarPainter p = new CarPainter(fm);
        BasicCarServer carServer = new UDPCarServer(fm, p, 8080);
    }


    @Override
    public void run() {
        final int packetlength = 1000;
        try {

            DatagramSocket serverSocket = new DatagramSocket(port);
            while(true) {
                DatagramPacket packet = new DatagramPacket(new byte[packetlength],packetlength);
                serverSocket.receive(packet);
                //System.out.println("data="+packet.getData());
                new Thread(new ClientWorker(packet, this, serverSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

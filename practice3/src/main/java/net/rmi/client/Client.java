package net.rmi.client;

import net.command.SerializableCommand;
import net.rmi.RemoteCarServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author : Alex
 * @created : 10.04.2021, суббота
 **/
public class Client {

    static final String host = "localhost";//"132.145.228.39";
    static final int port = 8080;
    static final String name = "RMIServer";

    public static void main(String[] args) throws Exception {
        //1. Connect to name server;
        //2. lookup by name
        //3. call methods of proxy-object

        Registry registry = LocateRegistry.getRegistry(host,port);
        RemoteCarServer server = (RemoteCarServer) registry.lookup(name);
        System.out.println("server="+server);

        SerializableCommand command = new SerializableCommand(0,"CREATECAR","");
        int carIndex = server.executeCommand(command);
        System.out.println("carIndex="+carIndex);

        command = new SerializableCommand(carIndex,"SETNAME","Alex");
        server.executeCommand(command);

        command = new SerializableCommand(carIndex, "DOWN", "6");
        boolean ret = server.executeCommand(command);
        System.out.println("ret="+ret);

    }

}

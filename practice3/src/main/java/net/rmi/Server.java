package net.rmi;

import car.BasicCarServer;
import car.CarEventsListener;
import car.CarPainter;
import car.FieldMatrix;
import car.command.Command;
import net.command.SerializableCommand;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends BasicCarServer implements RemoteCarServer {
    public static String name="RMIServer";

    public static final int port=8080;
    public Server(FieldMatrix fieldMatrix, CarEventsListener carEventsListener){
        super(fieldMatrix,carEventsListener);
    }

    @Override
    public <T> T executeCommand(SerializableCommand command) throws RemoteException {
        Command command_tmp=Command.createCommand(getCar(command.carIndex),command.commandName+" "+command.commandparameter);
        return (T) command_tmp.execute();
    }
    public static void main(String args[]) throws Exception{
        Registry registry= LocateRegistry.createRegistry(port);
        InputStream is=Server.class.getClassLoader().getResourceAsStream("Field10x10.txt");
        FieldMatrix fm=FieldMatrix.load(new InputStreamReader(is));
        CarPainter p=new CarPainter(fm);
        Server server=new Server(fm,p);
        RemoteCarServer proxy=(RemoteCarServer) UnicastRemoteObject.exportObject(server,port);
        registry.rebind(name,proxy);
    }
}

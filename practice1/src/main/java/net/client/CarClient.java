package net.client;

import car.command.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author : Alex
 * @created : 18.03.2021, четверг
 **/
public class CarClient {
    private final Socket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;
    private static final HashMap<String,String> commands;
    static{
        commands = new HashMap<>();
        commands.put(UpCommand.class.getName(),"UP");
        commands.put(DownCommand.class.getName(),"DOWN");
        commands.put(RightCommand.class.getName(),"RIGHT");
        commands.put(LeftCommand.class.getName(),"LEFT");
        commands.put(ChangeColorCommand.class.getName(),"CHANGECOLOR");
    }

    public CarClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

    }

    public boolean executeCommand(Command command){
        try {
            dos.writeUTF(commands.get(command.getClass().getName()));
            dos.writeUTF(command.getParameter().toString());
            return dis.readBoolean();
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }
}

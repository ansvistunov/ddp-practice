package net.client;

import car.CarServer;
import car.command.Command;
import car.command.CreateCarCommand;
import car.command.DownCommand;
import net.command.SerializableCommand;

import java.util.Random;

/**
 * @author : Alex
 * @created : 24.03.2021, среда
 **/
public class Main {
    private static int index = 0;
    public static void main(String[] args) throws Exception{
        
        class CarMover implements Runnable{

            private final UDPClient carClient;
            CarMover(UDPClient carClient){
                this.carClient = carClient;
            }
            @Override
            public void run() {
                CarServer.Direction direction = CarServer.Direction.RIGHT;
                Random random = new Random();
                SerializableCommand command = new SerializableCommand(0, "CREATECAR","");
                final Integer carIndex = carClient.executeCommand(command);
                command = new SerializableCommand(carIndex,"SETNAME", "car"+index++);
                carClient.executeCommand(command);

                while (true) {
                    try {
                        command = new SerializableCommand(carIndex,direction.name(), "1");
                        boolean moved = carClient.executeCommand(command);
                        if (!moved) {
                            direction = CarServer.Direction.values()[random.nextInt(4)];
                        }
                    }catch (RuntimeException e){
                        return;
                    } catch (Exception e) {
                        //e.printStackTrace();
                        direction = CarServer.Direction.values()[random.nextInt(4)];
                    }
                }
            }
        }


        String server = "132.145.228.39";
        new Thread(new CarMover(new UDPClient(server,8080))).start();
        new Thread(new CarMover(new UDPClient(server,8080))).start();
        new Thread(new CarMover(new UDPClient(server,8080))).start();
        new Thread(new CarMover(new UDPClient(server,8080))).start();





    }
}

package net.client;

import car.CarServer;
import car.command.ChangeColorCommand;
import car.command.Command;
import car.command.DownCommand;

import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * @author : Alex
 * @created : 18.03.2021, четверг
 **/
public class Main {
    public static void main(String[] args) throws Exception{
        //CarClient client = new CarClient("localhost",8080);



        class CarMover implements Runnable{
            private final CarClient carClient;
            CarMover(CarClient carClient){
                this.carClient = carClient;
            }
            @Override
            public void run() {
                CarServer.Direction direction = CarServer.Direction.RIGHT;
                Random random = new Random();
                while (true) {
                    try {
                        Command command = Command.createCommand(null, direction.name() + " 1");
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


        new Thread(new CarMover(new CarClient("localhost",8080))).start();
        new Thread(new CarMover(new CarClient("localhost",8080))).start();
        new Thread(new CarMover(new CarClient("localhost",8080))).start();
        new Thread(new CarMover(new CarClient("localhost",8080))).start();

    }
}

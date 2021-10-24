package net.command;

import java.io.Serializable;

/**
 * @author : Alex
 * @created : 23.03.2021, вторник
 **/
public class SerializableCommand implements Serializable {
    public final int carIndex;
    public final String commandName;
    public final String commandparameter;

    public SerializableCommand(int carIndex, String commandName, String commandparameter){
        this.carIndex = carIndex;
        this.commandName = commandName;
        this.commandparameter = commandparameter;

    }
}

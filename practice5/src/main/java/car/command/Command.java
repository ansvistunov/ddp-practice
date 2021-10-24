package car.command;

import car.Car;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.BiFunction;


/**
 * @author : Alex
 * @created : 11.03.2021, четверг
 **/
public abstract class Command<T, V> {
    protected final T parameter;
    protected final Car car;
    protected static Properties commandClasses;
    protected static HashMap<Class, BiFunction<String,Car,Command>> factory;

    static{
        try {
            InputStream is = Command.class.getClassLoader().getResourceAsStream("commands.txt");
            commandClasses = new Properties();
            commandClasses.load(is);
            factory = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Command(T parameter, Car car){
        this.parameter = parameter;
        this.car = car;
    }

    public T getParameter(){return parameter;}

    private static Command createCommand(Car car, Class<? extends Command> commandClass, String parameter){
            return factory.get(commandClass).apply(parameter,car);
    }


    public static Command createCommand(Car car, String nextLine) {
        String[] tokens = nextLine.split("\\s+");
        try {
            String className = commandClasses.getProperty(tokens[0].toUpperCase());
            Class c = Class.forName(className);
            String parameter = "";
            try{
                parameter = tokens[1];
            }catch(ArrayIndexOutOfBoundsException e){}
            return createCommand(car, c, parameter);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getException());
        }
    }

    public Car getCar(){return car;}

    @Override
    public String toString(){
        return this.getClass().getName() + " " + parameter;
    };

    public abstract V execute();
}

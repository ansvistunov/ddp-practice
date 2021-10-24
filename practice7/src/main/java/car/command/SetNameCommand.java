package car.command;

import car.Car;

/**
 * @author : Alex
 * @created : 23.03.2021, вторник
 **/
public class SetNameCommand extends Command<String, Boolean> {

    static{
        factory.put(SetNameCommand.class, (param, car)->new SetNameCommand(param,car));
    }

    public SetNameCommand(String parameter, Car car){
        super(parameter,car);
    }

    @Override
    public Boolean execute() {
        car.setName(parameter);
        return true;
    }
}

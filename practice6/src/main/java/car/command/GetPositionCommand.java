package car.command;

import car.Car;
import car.Position;

/**
 * @author : Alex
 * @created : 01.05.2021, суббота
 **/
public class GetPositionCommand extends Command<String, Position> {

    static{
        factory.put(GetPositionCommand.class, (param, car)->new GetPositionCommand(param,car));
    }
    public GetPositionCommand(String parameter, Car car){
        super(parameter, car);
    }

    @Override
    public Position execute() {
        return car.getPosition();
    }
}

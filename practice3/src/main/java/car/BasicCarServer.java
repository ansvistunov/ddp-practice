package car;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Alex
 * @created : 10.03.2021, среда
 **/
public class BasicCarServer implements CarServer {
    protected final FieldMatrix fieldMatrix;
    protected final List<Car> cars;
    protected final CarEventsListener carEventsListener;
    protected static BasicCarServer carServer;

    public static BasicCarServer getServer(){return carServer;}

    protected BasicCarServer(FieldMatrix fieldMatrix, CarEventsListener carEventsListener){
        cars = new ArrayList<>();
        this.fieldMatrix = fieldMatrix;
        this.carEventsListener = carEventsListener;
        carServer = this;
    }

    public Car getCar(int index){
        return cars.stream()
                .filter((car)->car.getIndex()==index)
                .findAny()
                .orElse(null);
    }

    @Override
    public synchronized Car createCar() {
        Position freeCell = fieldMatrix.occupyFirstFreeCellByCar();
        Car car = new Car(this, freeCell);
        cars.add(car);
        carEventsListener.carCreated(car);
        return car;
    }

    @Override
    public void destroyCar(Car car) {
        cars.remove(car);
        fieldMatrix.freeCellFromCar(car.getPosition());
        carEventsListener.carDestroyed(car);
    }

    @Override
    public boolean moveCarTo(Car car, Direction direction) {
        Position from = car.getPosition();
        Position to = from.move(direction);
        boolean ret = fieldMatrix.moveCarTo(from.row, from.col, to.row, to.col);
        carEventsListener.carMoved(car,from,to,ret);
        return ret;
    }

}

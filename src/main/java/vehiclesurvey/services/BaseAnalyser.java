package vehiclesurvey.services;

import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.Models.VehiclesPerTimeInterval;
import vehiclesurvey.constants.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by Abby on 12/05/2017.
 */
public abstract class BaseAnalyser {
    abstract List<FactItem> createFactDistribution(List<VehiclesPerTimeInterval> timeIntervalList, Direction direction, Integer days);
    protected Supplier<Stream<Vehicle>> getDirectionWiseFilteredStream(List<Vehicle> vehicleList, Direction direction) {
        if(vehicleList == null) {
            return () -> new ArrayList<Vehicle>().stream();
        }
        return () -> vehicleList.stream().filter(vehicle -> vehicle.getDirection() == direction);
    }
}

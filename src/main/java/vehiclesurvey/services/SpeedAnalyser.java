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
public class SpeedAnalyser extends BaseAnalyser {
    List<FactItem> createFactDistribution(List<VehiclesPerTimeInterval> timeIntervalList, Direction direction, Integer days) {
        List<FactItem> result = new ArrayList<>();
        timeIntervalList.forEach(vehiclePerInterval -> {
            Supplier<Stream<Vehicle>> filteredStream = getDirectionWiseFilteredStream(vehiclePerInterval.getVehicleList(), direction);
            double speed = filteredStream.get().mapToDouble(Vehicle::getSpeed_MPS).sum();
            double avgSpeed = (double) (Math.round((speed / filteredStream.get().count()) * 100)) / 100;
            result.add(new FactItem(avgSpeed, vehiclePerInterval.getStartTime(), vehiclePerInterval.getEndTime()));
        });
        return result;
    }
}

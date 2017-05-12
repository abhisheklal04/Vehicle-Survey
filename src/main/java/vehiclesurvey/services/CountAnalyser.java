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
public class CountAnalyser extends BaseAnalyser {
    public List<FactItem> createFactDistribution(List<VehiclesPerTimeInterval> timeIntervalList, Direction direction, Integer days) {
        List<FactItem> result = new ArrayList<>();
        timeIntervalList.forEach(vehiclePerInterval -> {
            Supplier<Stream<Vehicle>> filteredStream = getDirectionWiseFilteredStream(vehiclePerInterval.getVehicleList(), direction);
            // getting average over days
            double count = (double) (filteredStream.get().count()) / days;
            result.add(new FactItem(count, vehiclePerInterval.getStartTime(), vehiclePerInterval.getEndTime()));
        });
        return result;
    }
}

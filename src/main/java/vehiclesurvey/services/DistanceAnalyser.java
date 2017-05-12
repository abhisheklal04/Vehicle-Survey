package vehiclesurvey.services;

import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.Models.VehiclesPerTimeInterval;
import vehiclesurvey.constants.AppConstants;
import vehiclesurvey.constants.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Abby on 12/05/2017.
 */
public class DistanceAnalyser extends BaseAnalyser {
    public List<FactItem> createFactDistribution(List<VehiclesPerTimeInterval> timeIntervalList, Direction direction, Integer days) {
        List<FactItem> result = new ArrayList<>();
        timeIntervalList.forEach(vehiclePerInterval -> {
            Supplier<Stream<Vehicle>> filteredStream = getDirectionWiseFilteredStream(vehiclePerInterval.getVehicleList(), direction);
            List<Vehicle> filteredList = filteredStream.get().collect(Collectors.toList());
            double avgDistance = 0d;
            if(filteredList.size() > 0) {
                for(Integer currentDay=1; currentDay<=days; currentDay++) {
                    final Integer dayConst = currentDay;
                    List<Vehicle> dailyList = filteredList.stream()
                      .filter(vehicle -> vehicle.getDay().equals(dayConst)).collect(Collectors.toList());
                    if(dailyList.size()>0) {
                        long timeOfLastVehicle = dailyList.get(dailyList.size() - 1).getTimeFront().getTime();
                        long timeOfFirstVehicle = dailyList.get(0).getTimeFront().getTime();

                        double averageTimeGapBetweenVehicles = ((double) timeOfLastVehicle - (double) timeOfFirstVehicle) / (double) filteredList.size();
                        avgDistance += (averageTimeGapBetweenVehicles * AppConstants.VEHICLE_SPEED_MTR_P_S) / AppConstants.MILLISECONDS_IN_A_SECOND;
                    }
                }
            }

            result.add(new FactItem((double) Math.round((avgDistance/days) * 100) / 100, vehiclePerInterval.getStartTime(),
              vehiclePerInterval.getEndTime()));
        });
        return result;
    }
}

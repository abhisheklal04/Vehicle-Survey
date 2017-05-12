package vehiclesurvey.utils;

import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.constants.AppConstants;
import vehiclesurvey.constants.Direction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Abby on 6/05/2017.
 */
public class VehicleParser {

    private List<String> vehicleReadings;
    private Integer day;

    public void setVehicleReadings(List<String> vehicleReadings) {
        this.vehicleReadings = vehicleReadings;
    }

    public Integer getDay() {
        return day;
    }

    private void setDay(Integer day) {
        this.day = day;
    }

    public List<Vehicle> generateVehicleData() throws Exception {
        if (vehicleReadings == null) {
            throw new AppException("Vehicle Readings are not set");
        }

        List<Vehicle> vehicleList = new ArrayList<>();
        int currentIndex = 0;
        int day = 1;
        try {
            while (currentIndex < vehicleReadings.size()) {
                if (isNextDayReading(currentIndex)) {
                    day++;
                }
                if (isDirectionNorth(currentIndex)) {
                    vehicleList.add(new Vehicle(Direction.NORTH, getReadingTime(vehicleReadings.get(currentIndex)),
                      getReadingTime(vehicleReadings.get(currentIndex + 1)), day));
                    currentIndex += AppConstants.NORTH_SENSOR_READINGS;
                } else if (isDirectionSouth(currentIndex)) {
                    vehicleList.add(new Vehicle(Direction.SOUTH, getReadingTime(vehicleReadings.get(currentIndex)),
                      getReadingTime(vehicleReadings.get(currentIndex + 2)), day));
                    currentIndex += AppConstants.SOUTH_SENSOR_READINGS;
                } else {
                    throw new AppException("Invalid vehicle traffic Data");
                }
            }
        } catch (Exception e) {
            if (e.getClass() == AppException.class) {
                throw e;
            } else {
                throw new AppException("Invalid vehicle traffic Data");
            }
        }

        setDay(day);
        return vehicleList;
    }

    private boolean isNextDayReading(int currentIndex) {
        if (currentIndex > 0) {
            Long currentTimeReading = Long.valueOf(vehicleReadings.get(currentIndex).substring(1));
            Long previousTimeReading = Long.valueOf(vehicleReadings.get(currentIndex - 1).substring(1));
            return currentTimeReading < previousTimeReading;
        } else {
            return false;
        }
    }

    private Date getReadingTime(String reading) {
        return new Date(Long.valueOf(reading.substring(1)));
    }

    private boolean isDirectionNorth(int currentIndex) {
        int subIndex = 0;
        for (subIndex = 0; subIndex < AppConstants.NORTH_SENSOR_READINGS; subIndex++) {
            if (!vehicleReadings.get(subIndex + currentIndex).startsWith(AppConstants.SENSOR_A_PREFIX)) {
                break;
            }
        }
        return subIndex == AppConstants.NORTH_SENSOR_READINGS;
    }

    private boolean isDirectionSouth(int currentIndex) {
        int subIndex = 0;
        for (subIndex = 0; subIndex < AppConstants.SOUTH_SENSOR_READINGS; subIndex += 2) {
            if (!(vehicleReadings.get(subIndex + currentIndex).startsWith(AppConstants.SENSOR_A_PREFIX)
              && vehicleReadings.get(subIndex + currentIndex + 1).startsWith(AppConstants.SENSOR_B_PREFIX))) {
                break;
            }
        }
        return subIndex == AppConstants.SOUTH_SENSOR_READINGS;
    }
}

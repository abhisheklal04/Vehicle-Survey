package vehiclesurvey.services;

import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.Models.VehiclesPerTimeInterval;
import vehiclesurvey.constants.AnalyserTypes;
import vehiclesurvey.constants.AppConstants;
import vehiclesurvey.constants.Direction;
import vehiclesurvey.utils.AppException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Abby on 7/05/2017.
 */
public class SurveyService {

    List<Vehicle> dataList;
    List<Integer> timeIntervals;
    Integer days;

    SpeedAnalyser speedAnalyser;
    CountAnalyser countAnalyser;
    DistanceAnalyser distanceAnalyser;

    /*public SurveyService() {
        speedAnalyser = new SpeedAnalyser();
        countAnalyser = new CountAnalyser();
        distanceAnalyser = new DistanceAnalyser();
    }*/

    public SurveyService(SpeedAnalyser speedAnalyser, CountAnalyser countAnalyser, DistanceAnalyser distanceAnalyser) {
        this.speedAnalyser = new SpeedAnalyser();
        this.countAnalyser = new CountAnalyser();
        this.distanceAnalyser = new DistanceAnalyser();
    }

    public void setDataList(List<Vehicle> dataList) {
        this.dataList = dataList;
    }

    public void setTimeIntervals(List<Integer> timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Map<Integer, List<FactItem>> analyseData(AnalyserTypes surveyType, Direction direction) throws Exception {
        Map<Integer, List<FactItem>> resultMap = new LinkedHashMap<>();

        if (dataList == null) {
            throw new AppException("Survey Data Not available");
        } else if (timeIntervals == null) {
            throw new AppException("Time Intervals Not available");
        } else if (days == null || days <= 0) {
            throw new AppException("Days should be > 0");
        }

        timeIntervals.forEach(interval -> {
            List<VehiclesPerTimeInterval> timeIntervalList = createVehiclePerTimeIntervalList(interval);
            if (surveyType == AnalyserTypes.SPEED) {
                resultMap.put(interval, speedAnalyser.createFactDistribution(timeIntervalList, direction, days));
            } else if (surveyType == AnalyserTypes.COUNT) {
                resultMap.put(interval, countAnalyser.createFactDistribution(timeIntervalList, direction, days));
            }
            else if(surveyType == AnalyserTypes.DISTANCE) {
                resultMap.put(interval, distanceAnalyser.createFactDistribution(timeIntervalList, direction, days));
            }
        });

        return resultMap;
    }

    private List<VehiclesPerTimeInterval> createVehiclePerTimeIntervalList(int interval) {
        List<VehiclesPerTimeInterval> timeIntervalList = new ArrayList<>();
        for (int i = 0; i < AppConstants.MINUTES_IN_A_DAY / interval; i++) {
            Date startTime = new Date(i * interval * AppConstants.SECONDS_IN_A_MINUTE * AppConstants.MILLISECONDS_IN_A_SECOND);
            Date endTime = new Date((i + 1) * interval * AppConstants.SECONDS_IN_A_MINUTE * AppConstants.MILLISECONDS_IN_A_SECOND);
            List<Vehicle> vehiclesInIntervals = dataList.stream()
              .filter(vehicle -> startTime.getTime() <= vehicle.getTimeFront().getTime()
                && vehicle.getTimeFront().getTime() < endTime.getTime()).collect(Collectors.toList());

            timeIntervalList.add(new VehiclesPerTimeInterval(startTime, endTime, vehiclesInIntervals));
        }

        return timeIntervalList;
    }
}

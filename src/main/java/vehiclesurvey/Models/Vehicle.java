package vehiclesurvey.Models;

import vehiclesurvey.constants.AppConstants;
import vehiclesurvey.constants.Direction;

import java.util.Date;

/**
 * Created by Abby on 6/05/2017.
 */
public class Vehicle {
    Direction direction;
    Date timeFront;
    Date timeRear;
    Integer day;

    public Vehicle(Direction direction, Date timeFront, Date timeRear, Integer day) {
        this.direction = direction;
        this.timeFront = timeFront;
        this.timeRear = timeRear;
        this.day = day;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Date getTimeFront() {
        return timeFront;
    }

    public void setTimeFront(Date timeFront) {
        this.timeFront = timeFront;
    }

    public Date getTimeRear() {
        return timeRear;
    }

    public void setTimeRear(Date timeRear) {
        this.timeRear = timeRear;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Double getSpeed_MPS() {
        return (double)Math.round((AppConstants.VEHICLE_LENGTH_MTR * 1000 / (timeRear.getTime() - timeFront.getTime())) * 100) / 100;
    }
}

package vehiclesurvey.Models;

/**
 * Created by Abby on 10/05/2017.
 */
public class SurveyFormat {
    String type;
    String interval;
    String direction;
    String startTime;
    String endTime;

    public SurveyFormat(String type, String interval, String direction, String startTime, String endTime) {
        this.type = type;
        this.interval = interval;
        this.direction = direction;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

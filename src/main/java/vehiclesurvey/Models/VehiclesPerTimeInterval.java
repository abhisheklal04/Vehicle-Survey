package vehiclesurvey.Models;

import java.util.Date;
import java.util.List;

/**
 * Created by Abby on 8/05/2017.
 */
public class VehiclesPerTimeInterval {
    private Date startTime;
    private Date endTime;
    private List<Vehicle> vehicleList;

    public VehiclesPerTimeInterval(Date startTime, Date endTime, List<Vehicle> vehicleList) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.vehicleList = vehicleList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}

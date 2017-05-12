package vehiclesurvey.Models;

import org.junit.Assert;
import org.junit.Test;
import vehiclesurvey.constants.Direction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Abby on 12/05/2017.
 */
public class VehiclesPerTimeIntervalTest {

    @Test
    public void shouldCreateVehicleListPassedInATimeIntervalInAGivenDay(){
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(Direction.NORTH, new Date(100), new Date(250), 1));
        vehicles.add(new Vehicle(Direction.SOUTH, new Date(255), new Date(400), 2));
        VehiclesPerTimeInterval vehiclesPerTimeInterval = new VehiclesPerTimeInterval(new Date(60000), new Date(120000), vehicles);

        Assert.assertEquals(60000, vehiclesPerTimeInterval.getStartTime().getTime());
        Assert.assertEquals(120000, vehiclesPerTimeInterval.getEndTime().getTime());
        Assert.assertEquals(vehicles.size(), vehiclesPerTimeInterval.getVehicleList().size());

    }

}
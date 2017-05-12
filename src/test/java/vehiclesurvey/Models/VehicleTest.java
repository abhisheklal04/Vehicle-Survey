package vehiclesurvey.Models;

import org.junit.Assert;
import org.junit.Test;
import vehiclesurvey.constants.Direction;

import java.util.Date;

/**
 * Created by Abby on 12/05/2017.
 */
public class VehicleTest {

    @Test
    public void shouldCreateVehicleWithDirection_EntryTimes_Day(){
        Vehicle vehicle = new Vehicle(Direction.NORTH,new Date(100), new Date(250), 1);
        Assert.assertEquals(Direction.NORTH, vehicle.getDirection());
        Assert.assertEquals(100, vehicle.getTimeFront().getTime());
        Assert.assertEquals(250, vehicle.getTimeRear().getTime());
        Assert.assertEquals(Integer.valueOf(1), vehicle.getDay());
    }

    @Test
    public void shouldCalculateSpeedOfVehicleInMtrPerSecRoundedTwoDecimals(){
        Vehicle vehicle = new Vehicle(Direction.NORTH,new Date(100), new Date(250), 1);
        Assert.assertEquals(Double.valueOf(16.67), vehicle.getSpeed_MPS());
    }
}
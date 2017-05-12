package vehiclesurvey.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.Models.VehiclesPerTimeInterval;
import vehiclesurvey.constants.Direction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Abby on 12/05/2017.
 */
public class DistanceAnalyserTest {

    DistanceAnalyser distanceAnalyser;
    @Before
    public void setUp() throws Exception {
        distanceAnalyser = new DistanceAnalyser();
    }

    @After
    public void tearDown() throws Exception {
        distanceAnalyser = null;
    }

    @Test
    public void shouldCalcAverageDistanceBtwVehiclesForEachIntervalSession() throws Exception {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(Direction.NORTH, new Date(100), new Date(250), 1));
        vehicles.add(new Vehicle(Direction.NORTH, new Date(1000), new Date(1150), 1));
        VehiclesPerTimeInterval session = new VehiclesPerTimeInterval(new Date(1), new Date(60000), vehicles);

        List<Vehicle> vehicles2 = new ArrayList<>();
        vehicles2.add(new Vehicle(Direction.NORTH, new Date(60001), new Date(60150), 1));
        vehicles2.add(new Vehicle(Direction.NORTH, new Date(90001), new Date(90150), 1));
        vehicles2.add(new Vehicle(Direction.NORTH, new Date(80000), new Date(80150), 2));
        vehicles2.add(new Vehicle(Direction.NORTH, new Date(100150), new Date(100150), 2));
        VehiclesPerTimeInterval session2 = new VehiclesPerTimeInterval(new Date(60001), new Date(120000), vehicles2);
        List<VehiclesPerTimeInterval> sessionList = new ArrayList<>();
        sessionList.add(session);
        sessionList.add(session2);
        List<FactItem> factItems = distanceAnalyser.createFactDistribution(sessionList, Direction.NORTH, 2);
        Assert.assertEquals(Double.valueOf(3.75), factItems.get(0).getFact());
        Assert.assertEquals(1, factItems.get(0).getStartTime().getTime());
        Assert.assertEquals(60000, factItems.get(0).getEndTime().getTime());

        Assert.assertEquals(Double.valueOf(104.5), factItems.get(1).getFact());
        Assert.assertEquals(60001, factItems.get(1).getStartTime().getTime());
        Assert.assertEquals(120000, factItems.get(1).getEndTime().getTime());
    }

    @Test
    public void shouldReturnDistanceAsZeroForNullOrNoVehicleList(){
        List<Vehicle> vehicles = new ArrayList<>();
        VehiclesPerTimeInterval session = new VehiclesPerTimeInterval(new Date(1), new Date(60000), vehicles);
        List<VehiclesPerTimeInterval> sessionList = new ArrayList<>();
        sessionList.add(session);

        List<FactItem> factItems = distanceAnalyser.createFactDistribution(sessionList, Direction.NORTH, 2);
        Assert.assertEquals(Double.valueOf(0), factItems.get(0).getFact());

        sessionList.add(new VehiclesPerTimeInterval(new Date(60001), new Date(120000), null));
        List<FactItem> factItems2 = distanceAnalyser.createFactDistribution(sessionList, Direction.NORTH, 2);
        Assert.assertEquals(Double.valueOf(0), factItems2.get(1).getFact());
    }

    @Test
    public void shouldReturnEmptyWhenEmptyIntervalListIsPassed(){
        List<FactItem> factItems = distanceAnalyser.createFactDistribution(new ArrayList<>(), Direction.SOUTH, 2);
        Assert.assertTrue(factItems.isEmpty());
    }

}
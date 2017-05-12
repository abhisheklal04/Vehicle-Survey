package vehiclesurvey.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.constants.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Abby on 6/05/2017.
 */
public class VehicleParserTest {

    VehicleParser vehicleParser;

    @Before
    public void setUp() throws Exception {
        vehicleParser = new VehicleParser();
    }

    @After
    public void tearDown() throws Exception {
        vehicleParser = null;
    }

    private List<String> createVehicleReadingsStub() {
        String input = "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n";
        return Arrays.stream(input.split("\n")).collect(Collectors.toList());
    }

    @Test
    public void shouldParseAndReturnVehicleModelList() throws Exception {

        vehicleParser.setVehicleReadings(createVehicleReadingsStub());
        List<Vehicle> vehicleList = vehicleParser.generateVehicleData();

        Assert.assertEquals(3, vehicleList.size());
        Assert.assertNotNull("Parses North Direction Vehicle", vehicleList.get(0));
        Assert.assertEquals(Direction.NORTH, vehicleList.get(0).getDirection());
        Assert.assertEquals(98186, vehicleList.get(0).getTimeFront().getTime());
        Assert.assertEquals(98333, vehicleList.get(0).getTimeRear().getTime());
        Assert.assertEquals((Integer) 1, vehicleList.get(0).getDay());

        Assert.assertNotNull("Parses South Direction Vehicle", vehicleList.get(2));
        Assert.assertEquals(Direction.SOUTH, vehicleList.get(2).getDirection());
        Assert.assertEquals(638379, vehicleList.get(2).getTimeFront().getTime());
        Assert.assertEquals(638520, vehicleList.get(2).getTimeRear().getTime());
        Assert.assertEquals((Integer) 1, vehicleList.get(2).getDay());
    }

    @Test
    public void shouldParseVehiclesDayWise() throws Exception {
        String input = "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n"
          + "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n"
          + "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n"
          + "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n";
        List<String> inputStub = Arrays.stream(input.split("\n")).collect(Collectors.toList());

        vehicleParser.setVehicleReadings(inputStub);
        List<Vehicle> vehicleList = vehicleParser.generateVehicleData();

        Assert.assertEquals(12, vehicleList.size());
        Assert.assertEquals(Integer.valueOf(1), vehicleList.get(0).getDay());
        Assert.assertEquals(Integer.valueOf(2), vehicleList.get(3).getDay());
        Assert.assertEquals(Integer.valueOf(3), vehicleList.get(6).getDay());
        Assert.assertEquals(Integer.valueOf(4), vehicleList.get(9).getDay());
    }

    @Test
    public void shouldCalculateTheNoOfDaysAfterParsingVehicleData() throws Exception {
        String input = "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n"
          + "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n"
          + "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n"
          + "A98186\nA98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n";
        List<String> inputStub = Arrays.stream(input.split("\n")).collect(Collectors.toList());

        vehicleParser.setVehicleReadings(inputStub);
        vehicleParser.generateVehicleData();
        Assert.assertEquals(Integer.valueOf(4), vehicleParser.getDay());
    }

    @Test
    public void shouldThrowExceptionWhenVehicleReadingsNotSet() throws Exception {
        try {
            vehicleParser.generateVehicleData();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Vehicle Readings are not set", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenCannotParseInvalidReadings() throws Exception {
        try {
            String input = "A98186\nA98333\nB499718\nA499886\nA638379\nB638382\nA638520\nB638523\n";
            List<String> inputStub = Arrays.stream(input.split("\n")).collect(Collectors.toList());
            vehicleParser.setVehicleReadings(inputStub);
            List<Vehicle> result = vehicleParser.generateVehicleData();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Invalid vehicle traffic Data", e.getMessage());
        }
        try {
            String input2 = "A98186\nB98333\nA499718\nA499886\nA638379\nB638382\nA638520\nB638523\n";
            List<String> inputStub2 = Arrays.stream(input2.split("\n")).collect(Collectors.toList());
            vehicleParser.setVehicleReadings(inputStub2);
            List<Vehicle> result = vehicleParser.generateVehicleData();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Invalid vehicle traffic Data", e.getMessage());
        }
        try {
            String input3 = "A98186\nA98333\nA499718\nA499886\nA638379\nA638382\nA638520\nB638523\n";
            List<String> inputStub3 = Arrays.stream(input3.split("\n")).collect(Collectors.toList());
            vehicleParser.setVehicleReadings(inputStub3);
            vehicleParser.generateVehicleData();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Invalid vehicle traffic Data", e.getMessage());
        }
    }
}
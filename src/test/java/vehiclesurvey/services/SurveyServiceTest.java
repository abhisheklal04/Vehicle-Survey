package vehiclesurvey.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.constants.AnalyserTypes;
import vehiclesurvey.constants.Direction;

import java.util.*;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Abby on 7/05/2017.
 */
public class SurveyServiceTest {
    SurveyService surveyService;
    SpeedAnalyser speedAnalyser;
    CountAnalyser countAnalyser;
    DistanceAnalyser distanceAnalyser;
    @Before
    public void setUp() throws Exception {
        speedAnalyser = spy(new SpeedAnalyser());
        countAnalyser = spy(new CountAnalyser());
        distanceAnalyser = spy(new DistanceAnalyser());
        surveyService = spy(new SurveyService(speedAnalyser,countAnalyser,distanceAnalyser));
    }

    @After
    public void tearDown() throws Exception {
    }

    private List<Vehicle> simpleVehicleListStub() {
        List<Vehicle> list = new ArrayList<>();
        list.add(new Vehicle(Direction.NORTH, new Date(1), new Date(150), 1));
        list.add(new Vehicle(Direction.SOUTH, new Date(154), new Date(300), 1));
        list.add(new Vehicle(Direction.NORTH, new Date(70000), new Date(70150), 1));
        list.add(new Vehicle(Direction.SOUTH, new Date(70154), new Date(70304), 1));
        return list;
    }

    private List<Vehicle> multiDayVehicleListStub() {
        List<Vehicle> list = new ArrayList<>();
        list.add(new Vehicle(Direction.NORTH, new Date(1), new Date(150), 1));
        list.add(new Vehicle(Direction.SOUTH, new Date(154), new Date(300), 1));
        list.add(new Vehicle(Direction.NORTH, new Date(70000), new Date(70150), 1));
        list.add(new Vehicle(Direction.SOUTH, new Date(70154), new Date(70304), 1));
        list.add(new Vehicle(Direction.NORTH, new Date(3), new Date(150), 2));
        list.add(new Vehicle(Direction.SOUTH, new Date(154), new Date(300), 2));
        list.add(new Vehicle(Direction.NORTH, new Date(3), new Date(150), 3));
        list.add(new Vehicle(Direction.SOUTH, new Date(154), new Date(300), 3));
        list.add(new Vehicle(Direction.NORTH, new Date(3), new Date(150), 4));
        list.add(new Vehicle(Direction.SOUTH, new Date(154), new Date(300), 4));
        return list;
    }

    private List<Vehicle> multiDayMultiIntervalVehicleStub() {
        List<Vehicle> list = new ArrayList<>();
        list.add(new Vehicle(Direction.NORTH, new Date(1), new Date(150), 1));
        list.add(new Vehicle(Direction.SOUTH, new Date(154), new Date(300), 1));
        list.add(new Vehicle(Direction.NORTH, new Date(70000), new Date(70150), 1));
        list.add(new Vehicle(Direction.SOUTH, new Date(70154), new Date(70304), 1));
        list.add(new Vehicle(Direction.NORTH, new Date(900005), new Date(900150), 2));
        list.add(new Vehicle(Direction.SOUTH, new Date(900154), new Date(900155), 2));
        list.add(new Vehicle(Direction.NORTH, new Date(360005), new Date(3600150), 3));
        list.add(new Vehicle(Direction.NORTH, new Date(3600153), new Date(3600300), 3));
        list.add(new Vehicle(Direction.SOUTH, new Date(43200100), new Date(43200250), 4));
        list.add(new Vehicle(Direction.NORTH, new Date(43200255), new Date(43200405), 4));
        list.add(new Vehicle(Direction.SOUTH, new Date(43200408), new Date(43200565), 4));
        return list;
    }

    private List<Integer> createIntervalsListStub() {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        return list;
    }

    private List<Integer> multipleIntervalsListStub() {
        List<Integer> list = new ArrayList<>();
        list.add(720);  // 12 hours
        list.add(60);   // 1 hour
        list.add(30);   // 30 mins
        list.add(20);  // 20 mins
        list.add(15);  // 15 mins
        return list;
    }

    @Test
    public void shouldAcceptVehicleListTimeIntervals() {
        List<Vehicle> list = simpleVehicleListStub();
        surveyService.setDataList(list);
        verify(surveyService).setDataList(list);
    }

    @Test
    public void shouldAcceptTimeIntervalList() {
        List<Integer> list = new ArrayList<>();
        list.add(30);
        surveyService.setTimeIntervals(list);
        verify(surveyService).setTimeIntervals(list);
    }

    @Test
    public void shouldAnalyseDataForEachSessionOfInterval() throws Exception{
        surveyService.setDataList(new ArrayList<>());
        surveyService.setTimeIntervals( Arrays.asList(30,60,720));
        surveyService.setDays(1);
        Map<Integer,List<FactItem>> surveyDataNORTH = surveyService.analyseData(AnalyserTypes.SPEED, Direction.NORTH);
        Assert.assertEquals(48, surveyDataNORTH.get(30).size());
        Assert.assertEquals(24, surveyDataNORTH.get(60).size());
        Assert.assertEquals(2, surveyDataNORTH.get(720).size());
    }

    @Test
    public void shouldAnalyseSpeedInAParticularDirection() throws Exception{
        List<Vehicle> list = simpleVehicleListStub();
        surveyService.setDataList(list);
        surveyService.setTimeIntervals(createIntervalsListStub());
        surveyService.setDays(1);
        Map<Integer,List<FactItem>> surveyDataNORTH = surveyService.analyseData(AnalyserTypes.SPEED, Direction.NORTH);
        Assert.assertEquals(Double.valueOf(16.73), surveyDataNORTH.get(10).get(0).getFact());

        Map<Integer,List<FactItem>> surveyDataSOUTH = surveyService.analyseData(AnalyserTypes.SPEED, Direction.SOUTH);
        Assert.assertEquals(Double.valueOf(16.9), surveyDataSOUTH.get(10).get(0).getFact());
    }

    @Test
    public void shouldAnalyzeVehicleCountsInEitherDirection() throws Exception {
        List<Vehicle> list = simpleVehicleListStub();
        surveyService.setDataList(list);
        surveyService.setDays(1);
        surveyService.setTimeIntervals(createIntervalsListStub());
        Map<Integer,List<FactItem>> surveyDataNorth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
        Assert.assertEquals(Double.valueOf(2),surveyDataNorth.get(10).get(0).getFact());

        Map<Integer,List<FactItem>> surveyDataSouth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.SOUTH);
        Assert.assertEquals(Double.valueOf(2), surveyDataSouth.get(10).get(0).getFact());
    }

    @Test
    public void shouldAnalyzeVehicleCountsAverageOverDays() throws Exception {
        List<Vehicle> list = multiDayVehicleListStub();
        surveyService.setDataList(list);
        surveyService.setDays(4);
        surveyService.setTimeIntervals(createIntervalsListStub());
        Map<Integer,List<FactItem>> surveyDataNorth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
        Assert.assertEquals(Double.valueOf(1.25),surveyDataNorth.get(10).get(0).getFact());

        Map<Integer,List<FactItem>> surveyDataSouth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.SOUTH);
        Assert.assertEquals(Double.valueOf(1.25), surveyDataSouth.get(10).get(0).getFact());
    }

    @Test
    public void shouldThrowExceptionWhenDaysNotSet() throws Exception{
        List<Vehicle> list = multiDayVehicleListStub();
        surveyService.setDataList(list);
        surveyService.setTimeIntervals(createIntervalsListStub());
        try {
            surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
            Assert.fail();
        }
        catch(Exception e) {
            Assert.assertEquals("Days should be > 0", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenSurveyDataNotSet() throws Exception{
        surveyService.setDays(4);
        surveyService.setTimeIntervals(createIntervalsListStub());
        try {
            surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
            Assert.fail();
        }
        catch(Exception e) {
            Assert.assertEquals("Survey Data Not available", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenTimeIntervalNotSet() throws Exception{
        List<Vehicle> list = multiDayVehicleListStub();
        surveyService.setDataList(list);
        surveyService.setDays(4);
        try {
            surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
            Assert.fail();
        }
        catch(Exception e) {
            Assert.assertEquals("Time Intervals Not available", e.getMessage());
        }
    }

    @Test
    public void shouldAnalyseSpeedAndCountForMultipleTimeIntervals() throws Exception{
        List<Vehicle> list = multiDayMultiIntervalVehicleStub();
        surveyService.setDataList(list);
        surveyService.setDays(4);
        surveyService.setTimeIntervals(multipleIntervalsListStub());
        Map<Integer,List<FactItem>> surveyDataNorth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
        Assert.assertEquals(Double.valueOf(1.25),surveyDataNorth.get(720).get(0).getFact());

        Map<Integer,List<FactItem>> surveyDataSouth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.SOUTH);
        Assert.assertEquals(Double.valueOf(0.75), surveyDataSouth.get(720).get(0).getFact());
    }

    @Test
    public void shouldHaveCountSpeedAsZeroWhenNoVehicleDuringIntervals() throws Exception{
        List<Vehicle> list = multiDayMultiIntervalVehicleStub();
        surveyService.setDataList(list);
        surveyService.setDays(4);
        surveyService.setTimeIntervals(multipleIntervalsListStub());
        Map<Integer,List<FactItem>> surveyDataNorth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
        Assert.assertEquals(Double.valueOf(0),surveyDataNorth.get(60).get(2).getFact());

        Map<Integer,List<FactItem>> surveyDataSouth = surveyService.analyseData(AnalyserTypes.SPEED, Direction.SOUTH);
        Assert.assertEquals(Double.valueOf(0), surveyDataSouth.get(60).get(2).getFact());
    }

    @Test
    public void shouldAnalyseAvgDistanceForMultipleTimeIntervals() throws Exception{
        List<Vehicle> list = multiDayMultiIntervalVehicleStub();
        surveyService.setDataList(list);
        surveyService.setDays(4);
        surveyService.setTimeIntervals(multipleIntervalsListStub());
        Map<Integer,List<FactItem>> surveyDataNorth = surveyService.analyseData(AnalyserTypes.DISTANCE, Direction.NORTH);
        Assert.assertEquals(Double.valueOf(2759.01),surveyDataNorth.get(720).get(0).getFact());

        Map<Integer,List<FactItem>> surveyDataSouth = surveyService.analyseData(AnalyserTypes.DISTANCE, Direction.SOUTH);
        Assert.assertEquals(Double.valueOf(97.24), surveyDataSouth.get(60).get(0).getFact());
    }
}
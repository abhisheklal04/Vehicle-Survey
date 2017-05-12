package vehiclesurvey;

import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.SurveyFormat;
import vehiclesurvey.Models.Vehicle;
import vehiclesurvey.constants.AnalyserTypes;
import vehiclesurvey.constants.AppConstants;
import vehiclesurvey.constants.Direction;
import vehiclesurvey.services.CountAnalyser;
import vehiclesurvey.services.DistanceAnalyser;
import vehiclesurvey.services.SpeedAnalyser;
import vehiclesurvey.services.SurveyService;
import vehiclesurvey.utils.FileUtils;
import vehiclesurvey.utils.VehicleParser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Abby on 5/05/2017.
 */

public class App {

    private final FileUtils fileUtils;
    private final VehicleParser vehicleParser;
    private final SurveyService surveyService;

    public App(FileUtils fileUtils, VehicleParser vehicleParser, SurveyService surveyService) {
        this.fileUtils = fileUtils;
        this.vehicleParser = vehicleParser;
        this.surveyService = surveyService;
    }

    void processArgs(String[] args) {
        Reader reader;
        try {
            if (args.length == 0) {
                reader = fileUtils.resourceReader(AppConstants.DEFAULT_INPUT);
            } else {
                reader = fileUtils.fileReader(args[0]);
            }
            List<String> parsedData = fileUtils.validateInputStream(reader, AppConstants.INPUT_VALIDATION_REGEX);
            run(parsedData);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void run(List<String> vehicleReadings) throws Exception {
        vehicleParser.setVehicleReadings(vehicleReadings);
        List<Vehicle> vehicleList = vehicleParser.generateVehicleData();
        surveyService.setDataList(vehicleList);
        List<Integer> timeIntervals = new ArrayList<>();
        timeIntervals.add(720);
        timeIntervals.add(60);
        timeIntervals.add(30);
        timeIntervals.add(20);
        timeIntervals.add(15);
        surveyService.setTimeIntervals(timeIntervals);
        surveyService.setDays(vehicleParser.getDay());
        generateSurvey();
    }

    private void generateSurvey() throws Exception{

        /*SurveyFormat countDistributionFormatNorth = new SurveyFormat("Vehicle Counts",
          "Minutes Session","North", "Start Time", "End Time");
        Map<Integer,List<FactItem>> countSurveyNorth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.NORTH);
        fileUtils.writeTextFile(fileUtils.getFilePath("Vehicle_Counts_North")
          , fileUtils.convertFactToFileFormat(countSurveyNorth, countDistributionFormatNorth));*/

        for( AnalyserTypes analyserType : AnalyserTypes.values()) {
            for(Direction direction : Direction.values()) {
                SurveyFormat surveyFormat = new SurveyFormat("Vehicle " + analyserType + " Distribution",
                  "Minutes Session", direction.toString(), "Start Time", "End Time");
                Map<Integer,List<FactItem>> surveyData = surveyService.analyseData(analyserType, direction);
                fileUtils.writeTextFile(fileUtils.getFilePath("Vehicle_"+analyserType+"_"+direction)
                  , fileUtils.convertFactToFileFormat(surveyData, surveyFormat));
            }
        }
/*
        Map<Integer,List<FactItem>> countSurveySouth = surveyService.analyseData(AnalyserTypes.COUNT, Direction.SOUTH);
        fileUtils.writeTextFile(fileUtils.getFilePath("Vehicle_Counts_South") ,
          fileUtils.convertFactToFileFormat(countSurveySouth, countDistributionFormat));

        SurveyFormat speedDistributionFormat = new SurveyFormat("Vehicle Speed(Mtr Per Sec) Distribution",
          "Minutes Session","Direction", "Start Time", "End Time");

        SurveyFormat distanceDistributionFormat = new SurveyFormat("Vehicle Speed(Mtr Per Sec) Distribution",
          "Minutes Session","Direction", "Start Time", "End Time");

        Map<Integer,List<FactItem>> speedSurveyNorth = surveyService.analyseData(AnalyserTypes.SPEED, Direction.NORTH);
        fileUtils.writeTextFile(fileUtils.getFilePath("Vehicle_Speeds_North") ,
          fileUtils.convertFactToFileFormat(speedSurveyNorth, speedDistributionFormat));

        Map<Integer,List<FactItem>> speedSurveySouth = surveyService.analyseData(AnalyserTypes.SPEED, Direction.SOUTH);
        fileUtils.writeTextFile(fileUtils.getFilePath("Vehicle_Speeds_South") ,
          fileUtils.convertFactToFileFormat(speedSurveySouth, speedDistributionFormat));

        Map<Integer,List<FactItem>> distanceSurveyNorth = surveyService.analyseData(AnalyserTypes.DISTANCE, Direction.NORTH);
        fileUtils.writeTextFile(fileUtils.getFilePath("Vehicle_Distance_North") ,
          fileUtils.convertFactToFileFormat(distanceSurveyNorth, distanceDistributionFormat));

        Map<Integer,List<FactItem>> distanceSurveySouth = surveyService.analyseData(AnalyserTypes.DISTANCE, Direction.SOUTH);
        fileUtils.writeTextFile(fileUtils.getFilePath("Vehicle_Distance_South") ,
          fileUtils.convertFactToFileFormat(distanceSurveySouth, distanceDistributionFormat));*/
    }

    public static void main(String[] args) {
        new App(new FileUtils()
          , new VehicleParser()
          , new SurveyService(new SpeedAnalyser(), new CountAnalyser(), new DistanceAnalyser()))
          .processArgs(args);
    }
}

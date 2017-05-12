package vehiclesurvey;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import vehiclesurvey.services.CountAnalyser;
import vehiclesurvey.services.DistanceAnalyser;
import vehiclesurvey.services.SpeedAnalyser;
import vehiclesurvey.services.SurveyService;
import vehiclesurvey.utils.FileUtils;
import vehiclesurvey.utils.VehicleParser;

import java.io.File;

/**
 * Created by Abby on 11/05/2017.
 */
public class AppTest_Integration {

    App app;
    @Before
    public void setUp() throws Exception {
        app = new App(new FileUtils(), new VehicleParser(),
          new SurveyService(new SpeedAnalyser(), new CountAnalyser(), new DistanceAnalyser()));
        emptyDirectory("out/survey");
    }

    @After
    public void tearDown() throws Exception {
        app = null;
    }

    private void emptyDirectory(String dirPath) {
        try {
            File directory = new File(dirPath);
            if (directory.exists()) {
                for (File file : directory.listFiles())
                    if (!file.isDirectory())
                        file.delete();
            }
        } catch (Exception e) {}
    }

    @Test
    public void shouldAnalyzeVehiclesData_Full() throws Exception {
        String[] args1 = new String[]{"src/test/resources/sample-data.txt"};
        app.processArgs(args1);
        Assert.assertTrue(new File("out/survey/Vehicle_COUNT_NORTH.txt").exists());
        Assert.assertTrue(new File("out/survey/Vehicle_COUNT_SOUTH.txt").exists());
        Assert.assertTrue(new File("out/survey/Vehicle_SPEED_NORTH.txt").exists());
        Assert.assertTrue(new File("out/survey/Vehicle_SPEED_SOUTH.txt").exists());
        Assert.assertTrue(new File("out/survey/Vehicle_DISTANCE_NORTH.txt").exists());
        Assert.assertTrue(new File("out/survey/Vehicle_DISTANCE_SOUTH.txt").exists());

    }

}
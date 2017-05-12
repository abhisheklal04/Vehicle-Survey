package vehiclesurvey.Models;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Abby on 12/05/2017.
 */
public class SurveyFormatTest {

    @Test
    public void shouldCreateASurveyFormatFieldsObject(){
        SurveyFormat surveyFormat = new SurveyFormat("Count", "Minutes",
          "North", "Start", "End");

        Assert.assertEquals("Count", surveyFormat.getType());
        Assert.assertEquals("Minutes", surveyFormat.getInterval());
        Assert.assertEquals("North", surveyFormat.getDirection());
        Assert.assertEquals("Start", surveyFormat.getStartTime());
        Assert.assertEquals("End", surveyFormat.getEndTime());
    }
}
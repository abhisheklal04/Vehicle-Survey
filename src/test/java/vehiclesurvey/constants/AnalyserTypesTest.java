package vehiclesurvey.constants;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Abby on 12/05/2017.
 */
public class AnalyserTypesTest {

    @Test
    public void shouldHaveThreeAnalyserTypes(){
        Assert.assertEquals(3,AnalyserTypes.values().length);
    }

    @Test
    public void shouldHaveCountAnalyserType(){
        Assert.assertEquals("COUNT",AnalyserTypes.COUNT.toString());
    }
    @Test
    public void shouldHaveSpeedAnalyserType(){
        Assert.assertEquals("SPEED",AnalyserTypes.SPEED.toString());
    }
    @Test
    public void shouldHaveDistanceAnalyserType(){
        Assert.assertEquals("DISTANCE",AnalyserTypes.DISTANCE.toString());
    }
}
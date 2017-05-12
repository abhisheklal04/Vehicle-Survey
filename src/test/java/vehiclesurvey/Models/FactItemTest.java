package vehiclesurvey.Models;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Abby on 12/05/2017.
 */
public class FactItemTest {

    @Test
    public void shouldCreateFactOfTypeDoubleAndStartEndTime(){
        FactItem factItem = new FactItem(10d,new Date(100), new Date(250));
        Assert.assertEquals( Double.valueOf(10), factItem.getFact());
        Assert.assertEquals(100,factItem.getStartTime().getTime());
        Assert.assertEquals(250,factItem.getEndTime().getTime());
    }

    @Test
    public void shouldReturn24HourFormatTimeIntervalString(){
        FactItem factItem = new FactItem(10d,new Date(60000), new Date(120000));
        Assert.assertEquals("00:01:00 - 00:02:00", factItem.getTimeIntervalString());
    }

}
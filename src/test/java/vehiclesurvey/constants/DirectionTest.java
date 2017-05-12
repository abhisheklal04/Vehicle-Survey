package vehiclesurvey.constants;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Abby on 12/05/2017.
 */
public class DirectionTest {
    @Test
    public void shouldHaveTwoDirections(){
        Assert.assertEquals(2,Direction.values().length);
    }

    @Test
    public void shouldHaveNorthDirection(){
        Assert.assertEquals("NORTH",Direction.NORTH.toString());
    }
    @Test
    public void shouldHaveSouthDirection(){
        Assert.assertEquals("SOUTH",Direction.SOUTH.toString());
    }
}
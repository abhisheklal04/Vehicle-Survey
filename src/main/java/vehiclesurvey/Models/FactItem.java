package vehiclesurvey.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Created by Abby on 8/05/2017.
 */
public class FactItem {
    Double Fact;
    Date startTime;
    Date endTime;

    public FactItem(Double fact, Date startTime, Date endTime) {
        Fact = fact;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Double getFact() {
        return Fact;
    }

    public void setFact(Double fact) {
        Fact = fact;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTimeIntervalString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

        return sdf.format(startTime) + " - " + sdf.format(endTime);
    }
}

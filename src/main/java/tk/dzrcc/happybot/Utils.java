package tk.dzrcc.happybot;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mazh0416 on 3/15/2017.
 */
public class Utils {
    public static Integer getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY) + 1;
    }

    public static Float calculateNewAvgValue(Float avgValue, Float value, Integer count) {
        return (avgValue*count+value)/(count+1);
    }

    public static Integer calculateNewAvgValue(Integer avgValue, Integer value, Integer count) {
        return (avgValue*count+value)/(count+1);
    }
}

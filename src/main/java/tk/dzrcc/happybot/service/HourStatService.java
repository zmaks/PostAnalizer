package tk.dzrcc.happybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import tk.dzrcc.happybot.entity.HourStat;
import tk.dzrcc.happybot.repository.HourStatRepository;

import java.util.List;

/**
 * Created by Maksim on 15.03.2017.
 */
public class HourStatService {
    @Autowired
    HourStatRepository hourStatRepository;


    public HourStat getByGroupIdAndHour(Integer groupId, Integer hour) {
        List<HourStat> list = hourStatRepository.findByGroupIdAndHour(groupId, hour);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public HourStat getDefaultHourStat(Integer hour) {
        return hourStatRepository.findOne(hour == 0 ? -25 : hour*-1);
    }
}

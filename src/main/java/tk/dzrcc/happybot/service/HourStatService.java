package tk.dzrcc.happybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.Utils;
import tk.dzrcc.happybot.entity.HourStat;
import tk.dzrcc.happybot.repository.HourStatRepository;

import java.util.List;

/**
 * Created by Maksim on 15.03.2017.
 */
public class HourStatService {
    @Autowired
    HourStatRepository hourStatRepository;

    @Transactional
    public HourStat getByGroupIdAndHour(Integer groupId, Integer hour) {
        List<HourStat> list = hourStatRepository.findByGroupIdAndHour(groupId, hour);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Transactional
    public HourStat updateHourStat(HourStat hourStat, Float likesRatio, Float repostsRatio) {
        Integer count = hourStat.getCount();
        hourStat.setAvgLikesRatio(Utils.calculateNewAvgValue(
                hourStat.getAvgLikesRatio(),
                likesRatio,
                count
        ));
        hourStat.setAvgRepostsRatio(Utils.calculateNewAvgValue(
                hourStat.getAvgRepostsRatio(),
                repostsRatio,
                count
        ));
        hourStat.setCount(count+1);
        return hourStatRepository.save(hourStat);
    }

    @Transactional
    public HourStat init(Integer groupId, Integer hour, Float likesRatio, Float repostsRatio) {
        HourStat hourStat = new HourStat();
        hourStat.setGroupId(groupId);
        hourStat.setHour(hour);
        hourStat.setAvgLikesRatio(likesRatio);
        hourStat.setMaxLikesRatio(likesRatio);
        hourStat.setAvgRepostsRatio(repostsRatio);
        hourStat.setMaxRepostsRatio(repostsRatio);
        hourStat.setCount(1);
        return hourStatRepository.save(hourStat);
    }
}

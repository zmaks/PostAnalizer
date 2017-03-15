package tk.dzrcc.happybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.Utils;
import tk.dzrcc.happybot.entity.HourStat;

/**
 * Created by mazh0416 on 3/15/2017.
 */
@Service
public class PostsAnalyzeService {

    @Autowired
    HourStatService hourStatService;

    @Transactional
    public Integer calculateMark(Integer likes, Integer reposts, Integer views, Integer groupId) {
        final Short repostsMarkWeight = 5;
        final Short likesMarkWeight = 3;

        Integer hour = Utils.getCurrentHour();
        HourStat hourStat = hourStatService.getByGroupIdAndHour(groupId, hour);

        Float likesRatio = likes.floatValue()/views;
        Float repostsRatio = reposts.floatValue()/views;
        if (hourStat == null){
            hourStatService.init(groupId, hour, likesRatio, repostsRatio);
            return 100;
        }
        Float markByLikes = (likesRatio/hourStat.getMaxLikesRatio())*100;
        if (markByLikes > 100){
            hourStat.setMaxLikesRatio(likesRatio);
            markByLikes = 100F;
        }
        Float markByReposts = (repostsRatio/hourStat.getMaxRepostsRatio())*100;
        if (markByReposts > 100) {
            hourStat.setMaxRepostsRatio(repostsRatio);
            markByReposts = 100F;
        }
        hourStatService.updateHourStat(hourStat, likesRatio, repostsRatio);
        return (int)(markByLikes*likesMarkWeight + markByReposts*repostsMarkWeight) / (repostsMarkWeight + likesMarkWeight);
    }
}

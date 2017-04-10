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

    private static final Short REPOSTS_MARK_WEIGHT = 5;
    private static final Short LIKES_MARK_WEIGHT = 3;
    private static final Integer MAX_MARK = 100;

    @Autowired
    HourStatService hourStatService;

    @Transactional
    public Integer calculateMark(Integer likes, Integer reposts, Integer views, Integer groupId, Integer hour) {

        HourStat hourStat = hourStatService.getByGroupIdAndHour(groupId, hour);

        Float likesRatio = likes.floatValue()/views;
        Float repostsRatio = reposts.floatValue()/views;
        if (hourStat == null){
            hourStatService.init(groupId, hour, likesRatio, repostsRatio);
            return MAX_MARK;
        }
        Float markByLikes = (likesRatio/hourStat.getMaxLikesRatio())*MAX_MARK;
        if (markByLikes > MAX_MARK){
            hourStat.setMaxLikesRatio(likesRatio);
            markByLikes = MAX_MARK.floatValue();
        }
        Float markByReposts = (repostsRatio/hourStat.getMaxRepostsRatio())*MAX_MARK;
        if (markByReposts > MAX_MARK) {
            hourStat.setMaxRepostsRatio(repostsRatio);
            markByReposts = MAX_MARK.floatValue();
        }
        hourStatService.updateHourStat(hourStat, likesRatio, repostsRatio);
        return (int)(markByLikes * LIKES_MARK_WEIGHT + markByReposts * REPOSTS_MARK_WEIGHT) / (REPOSTS_MARK_WEIGHT + LIKES_MARK_WEIGHT);
    }
}

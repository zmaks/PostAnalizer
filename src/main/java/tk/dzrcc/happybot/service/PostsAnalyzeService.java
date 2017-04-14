package tk.dzrcc.happybot.service;

import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.Utils;
import tk.dzrcc.happybot.entity.HourStat;

import java.util.List;

/**
 * Created by mazh0416 on 3/15/2017.
 */
@Service
public class PostsAnalyzeService {
    private static final Integer MIN_LIKES_COUNT = 300;
    private static Logger LOGGER = LoggerFactory.getLogger(PostsAnalyzeService.class);

    private static final Short REPOSTS_MARK_WEIGHT = 5;
    private static final Short LIKES_MARK_WEIGHT = 3;
    private static final Integer MAX_MARK = 100;
    private static final Float REP_LIKE_CF = 0.3f;

    @Autowired
    HourStatService hourStatService;

    @Transactional
    public Integer calculateMark(Integer likes, Integer reposts, Integer views, Integer groupId, Integer hour) {

        //HourStat hourStat = hourStatService.getByGroupIdAndHour(groupId, hour);
        HourStat hourStat = hourStatService.getByHour(hour);

        Float likesRatio = likes.floatValue()/views;
        Float repostsRatio = reposts.floatValue()/views;

        if (hourStat == null){
            hourStatService.init(groupId, hour, likesRatio, repostsRatio);
            return MAX_MARK;
        }

        Float markByLikes = (likesRatio/hourStat.getMaxLikesRatio())*MAX_MARK;
        if (markByLikes > MAX_MARK){
            hourStat.setMaxLikesRatio(likesRatio);
            //markByLikes = MAX_MARK.floatValue();
        }

        Float markByReposts = (repostsRatio/hourStat.getMaxRepostsRatio())*MAX_MARK;
        if (markByReposts > MAX_MARK){
            hourStat.setMaxRepostsRatio(repostsRatio);
            //markByReposts = MAX_MARK.floatValue();
        }

//        Integer mark = (int)(markByLikes * LIKES_MARK_WEIGHT + markByReposts * REPOSTS_MARK_WEIGHT) / (REPOSTS_MARK_WEIGHT + LIKES_MARK_WEIGHT);


        hourStatService.updateHourStat(hourStat, likesRatio, repostsRatio);
        return (int)(markByLikes * LIKES_MARK_WEIGHT + markByReposts * REPOSTS_MARK_WEIGHT) / (REPOSTS_MARK_WEIGHT + LIKES_MARK_WEIGHT);
    }

    public boolean isCorrectPost(WallpostFull wallPost) {
        // Has repost
        if (wallPost.getLikes() == null || wallPost.getLikes().getCount() < MIN_LIKES_COUNT) {
            LOGGER.info("Post is incorrect. Little amount of likes: {}", wallPost.getLikes().getCount());
            return false;
        }
        if (wallPost.getCopyHistory() != null) {
            LOGGER.info("Post is incorrect. It's a repost.");
            return false;
        }

        // Contest
        Float repLikeCF = (float)wallPost.getReposts().getCount()/(float)wallPost.getLikes().getCount();
        if (repLikeCF>REP_LIKE_CF) {
            LOGGER.info("Post is incorrect. Very big repost-like CF: {}", repLikeCF);
            return false;
        }

        // Wrong attachments
        List<WallpostAttachment> attachments = wallPost.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (WallpostAttachment attachment : attachments){
                if (!(attachment.getType().equals(WallpostAttachmentType.PHOTO) ||
                        attachment.getType().equals(WallpostAttachmentType.POSTED_PHOTO) ||
                        attachment.getType().equals(WallpostAttachmentType.DOC) ||
                        attachment.getType().equals(WallpostAttachmentType.LINK))){
                    LOGGER.info("Post is incorrect. Wrong attachment found: {}", attachment.getType().getValue());
                    return false;
                }
            }
        }
        return true;
    }

    public Integer getMaxMark(){
        return MAX_MARK;
    }
}

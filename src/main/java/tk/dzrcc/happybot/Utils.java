package tk.dzrcc.happybot;

import com.vk.api.sdk.objects.wall.WallpostFull;
import tk.dzrcc.happybot.entity.Post;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mazh0416 on 3/15/2017.
 */
public class Utils {
    private static final String HREF_PATTERN = "https://vk.com/public%s?w=wall-%s_%s";


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

    public static String buildPostLink(Post post) {
        return buildPostLink(post.getGroup().getGroupId(), post.getPostId());
    }

    public static String buildPostLink(WallpostFull wallPost) {
        return buildPostLink(wallPost.getOwnerId(), wallPost.getId());
    }

    private static String buildPostLink(Integer groupId, Integer postId){
        groupId = Math.abs(groupId);
        return String.format(HREF_PATTERN, groupId, groupId, postId);
    }

    public static boolean isLessThenHourAgo(Integer date) {
        Long a  = (new Date().getTime() - ((long)date)*1000);
        return  a < 59*60*1000;
    }
}

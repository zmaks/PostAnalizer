package tk.dzrcc.happybot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallpostFull;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import tk.dzrcc.happybot.entity.HourStat;
import tk.dzrcc.happybot.entity.Post;
import tk.dzrcc.happybot.entity.VkGroup;
import tk.dzrcc.happybot.service.GroupService;
import tk.dzrcc.happybot.service.HourStatService;
import tk.dzrcc.happybot.service.PostService;
import tk.dzrcc.happybot.vk.VKService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maksim on 28.02.2017.
 */
public class Main {

    @Autowired
    static VKService vkService;

    @Autowired
    static PostService postService;

    @Autowired
    static GroupService groupService;

    @Autowired
    static HourStatService hourStatService;

    public static void main(String[] args) {
        SpringApplication.run(Config.class);

    }

    private static void loadNewPosts() throws ClientException, ApiException, InterruptedException {
        List<Integer> groupIds = groupService.getAllGroupsIds();
        for (Integer groupId : groupIds){
            WallpostFull wallPost = vkService.getLastPostInGroup(groupId);
            if (wallPost != null)
                postService.savePostByWallPost(wallPost);
        }
    }

    private static void updateLoadedPosts() throws ClientException, ApiException, InterruptedException {
        List<Post> notUpdatedPosts = postService.getNotUpdatedPosts();
        List<String> idList = notUpdatedPosts.stream()
                .map(x -> x.getGroup().getGroupId()+"_"+x.getPostId())
                .collect(Collectors.toList());
        List<WallpostFull> wallPosts = vkService.getPostsByIdList(idList);
        if (wallPosts != null) {
            for (WallpostFull wallPost : wallPosts){
                Post post = notUpdatedPosts.stream()
                        .filter(x -> Math.abs(wallPost.getOwnerId()) == Math.abs(x.getGroup().getGroupId())
                            && wallPost.getId().equals(x.getPostId()))
                        .findFirst()
                        .orElse(null);
                updatePostAndGroup(wallPost, post);
            }
        }

        for (Post post : notUpdatedPosts){

        }
    }

    private static void updatePostAndGroup(WallpostFull wallPost, Post post) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        VkGroup group = post.getGroup();
        Integer likes = wallPost.getLikes().getCount();
        Integer reposts = wallPost.getReposts().getCount();

        HourStat hourStat = hourStatService.getByGroupIdAndHour(group.getGroupId(), hour);
        HourStat defaultHourStat = hourStatService.getDefaultHourStat(hour);
        if (hourStat != null){
            Float likesRatio = likes.floatValue()/hourStat.getLikes();
        }

    }



}

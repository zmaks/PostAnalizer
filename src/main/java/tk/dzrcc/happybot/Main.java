package tk.dzrcc.happybot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.queries.groups.GroupField;
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

import java.util.ArrayList;
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

    public static void main(String[] args) {
        SpringApplication.run(Config.class);

    }

    private void loadGroups() throws ClientException, ApiException {
       /* List<VkGroup> groups = new ArrayList<>();
        groupRepository.findAll().forEach(groups::add);
        List<String> groupIds = groups.stream()
                .filter(x -> x.getName() != null)
                .map(x -> (x.getGroupId()*-1)+"")
                .collect(Collectors.toList());
        List<GroupFull> response = vk.groups().getById(serviceActor).groupIds(groupIds).fields(GroupField.SCREEN_NAME).execute();
        for (GroupFull group : response) {
            VkGroup vkGroup = groupRepository.findOne(Integer.parseInt(group.getId()));
            vkGroup.setName(group.getName());
            groupRepository.save(vkGroup);
        }*/
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
                postService.updatePost(post, wallPost);
            }
        }
    }

}

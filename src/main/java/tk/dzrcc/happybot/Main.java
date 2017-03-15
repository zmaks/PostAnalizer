package tk.dzrcc.happybot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import tk.dzrcc.happybot.entity.Post;
import tk.dzrcc.happybot.service.GroupService;
import tk.dzrcc.happybot.service.PostService;
import tk.dzrcc.happybot.vk.VKService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<String> groupIds = null;
        try (Stream<String> stream = Files.lines(Paths.get("groups.txt"))) {
            groupIds = stream
                    .map(x -> x.substring(15))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<GroupFull> groups = vkService.loadGroups(groupIds);
        groupService.saveGroups(groups);

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
            if (wallPost != null) {
                if (!postService.exists(wallPost.getId(), wallPost.getOwnerId()))
                    postService.savePostByWallPost(wallPost);
            }
        }
    }

    private static void updateLoadedPosts() throws ClientException, ApiException, InterruptedException {
        List<Post> notUpdatedPosts = postService.getNotUpdatedPosts();
        if (notUpdatedPosts.isEmpty()){
            return;
        }
        List<String> idList = notUpdatedPosts.stream()
                .map(x -> x.getGroup().getGroupId()+"_"+x.getPostId())
                .collect(Collectors.toList());
        List<WallpostFull> wallPosts = vkService.getPostsByIdList(idList);
        if (wallPosts.isEmpty()) {
            return;
        }
        for (WallpostFull wallPost : wallPosts){
            Post post = notUpdatedPosts.stream()
                    .filter(x -> Math.abs(wallPost.getOwnerId()) == Math.abs(x.getGroup().getGroupId())
                        && wallPost.getId().equals(x.getPostId()))
                    .findFirst()
                    .orElse(null);
            if (post != null) {
                postService.updatePost(post, wallPost);
            }
        }
    }

}

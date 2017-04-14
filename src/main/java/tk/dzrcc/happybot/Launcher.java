package tk.dzrcc.happybot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;

import tk.dzrcc.happybot.entity.Post;
import tk.dzrcc.happybot.exceptions.UpdaterException;
import tk.dzrcc.happybot.service.GroupService;
import tk.dzrcc.happybot.service.PostService;
import tk.dzrcc.happybot.vk.VKService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;

/**
 * Created by Maksim on 01.03.2017.
 */
@Component
public class Launcher {

    private static Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    @Autowired
    private VKService vkService;

    @Autowired
    private PostService postService;

    @Autowired
    private GroupService groupService;


    @PostConstruct
    public void launch(){
        LOGGER.info("--------- LOADER IS STARTED ----------");
        try {
            loadGroups();
            LOGGER.info("Groups are loaded");
            TimeUnit.MICROSECONDS.sleep(500);
            while (true) {
                loadNewPosts();
                TimeUnit.SECONDS.sleep(30);
                updateLoadedPosts();
                TimeUnit.SECONDS.sleep(30);
            }
        } catch (InterruptedException | ApiException | ClientException | UpdaterException e) {
            e.printStackTrace();
        }
        //postRepository.save(new Post(123,177,342535));
    }

    private void loadGroups() throws ClientException, ApiException {
        List<String> groupIds = new ArrayList<>();
        File dir = new File(".");

        try  {
            File fin = new File(dir.getCanonicalPath() + File.separator + "groups.txt");
            BufferedReader br = new BufferedReader(new FileReader(fin));

            String line = null;
            while ((line = br.readLine()) != null) {
                groupIds.add(line.substring(15));
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupIds.forEach(System.out::println);
        /*List<String> groupIds = new ArrayList<>();
        groupIds.add("mudakoff");*/
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

    private void loadNewPosts() throws ClientException, ApiException, InterruptedException {
        LOGGER.info("-------------------------------------- Start loading new posts -------------");
        List<Integer> groupIds = groupService.getAllGroupsIds();
        LOGGER.info(String.format("There are %s group in handling",  groupIds.size()));
        for (Integer groupId : groupIds){
            WallpostFull wallPost = null;
            try {
                wallPost = vkService.getLastPostInGroup(groupId);
            } catch (UpdaterException e) {
                LOGGER.error(e.getMessage(), e);
                continue;
            }
            if (wallPost != null) {
                if (!postService.exists(wallPost.getId(), wallPost.getOwnerId())) {
                    postService.savePostByWallPost(wallPost);
                    LOGGER.info("New post is added: {}", Utils.buildPostLink(wallPost));
                }
            }
        }
        LOGGER.info("-------------------------------------- Stop loading new posts -------------\n\n\n");
    }

    private void updateLoadedPosts() throws ClientException, ApiException, InterruptedException, UpdaterException {
        LOGGER.info("-------------------------------------- Start updating posts -------------");
        List<Post> notUpdatedPosts = postService.getNotUpdatedPosts();
        if (notUpdatedPosts.isEmpty()){
            LOGGER.info("No posts for update. Stop updating.\n\n\n");
            return;
        }
        LOGGER.info("{} posts for update: ", notUpdatedPosts.size());
        notUpdatedPosts.stream()
                .map(x -> "\n\t"+Utils.buildPostLink(x))
                .forEach(LOGGER::info);
        List<String> idList = notUpdatedPosts.stream()
                .map(x -> x.getGroup().getGroupId()+"_"+x.getPostId())
                .collect(Collectors.toList());
        List<WallpostFull> wallPosts = vkService.getPostsByIdList(idList);
        if (wallPosts.isEmpty()) {
            LOGGER.info("No posts loaded from VK. Stop updating.\n\n\n");
            return;
        }
        List<Post> updatedPosts = new ArrayList<>();
        LOGGER.info("There are {} posts loaded from VK", wallPosts.size());
        for (WallpostFull wallPost : wallPosts){
            /*Post post = notUpdatedPosts.stream()
                    .filter(x -> Math.abs(wallPost.getOwnerId()) == Math.abs(x.getGroup().getGroupId())
                        && wallPost.getId().equals(x.getPostId()))
                    .findFirst()
                    .orElse(null);*/
            Post updated = postService.updatePost(wallPost);
            if (updated != null) updatedPosts.add(updated);
        }
        List<Post> postsForDelete = notUpdatedPosts.stream()
                .filter(x -> !updatedPosts.contains(x))
                .collect(Collectors.toList());
        postsForDelete.forEach(x -> LOGGER.info("Deleted post: {}", Utils.buildPostLink(x)));
        if (postsForDelete != null && !postsForDelete.isEmpty()) {
            postService.deletePosts(postsForDelete);
        }
        LOGGER.info("-------------------------------------- Stop updating posts -------------\n\n\n");
    }



}

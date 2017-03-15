package tk.dzrcc.happybot.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.groups.GroupField;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import tk.dzrcc.happybot.Main;
import tk.dzrcc.happybot.entity.VkGroup;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Maksim on 01.03.2017.
 */
@Service
public class VKService {

    private TransportClient transportClient;
    private VkApiClient vk;
    private static final String HREF_PATTERN = "https://vk.com/public%s?w=wall-%s_%s";
    private ServiceActor serviceActor = null;

    @PostConstruct
    private void initVk(){
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        Properties properties = loadConfiguration();
        Integer appId = Integer.parseInt(properties.getProperty("appId"));
        String clientSecret = properties.getProperty("clientSecret");
        String accessToken = properties.getProperty("accessToken");
        serviceActor = new ServiceActor(appId, clientSecret, accessToken);

    }

    private static Properties loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream is = Main.class.getResourceAsStream("/config.properties")) {
            properties.load(is);
        } catch (IOException e) {
            //LOG.error("Can't load properties file", e);
            throw new IllegalStateException(e);
        }

        return properties;
    }


    public WallpostFull getLastPostInGroup(Integer groupId) throws ClientException, ApiException, InterruptedException {
        for (int i = 0; i <= 3; i++) {
            try {
                WallpostFull newPost = null;
                GetResponse response = vk.wall().get().ownerId(groupId).count(2).execute();
                if (response.getItems().size() == 2) {
                    newPost = response.getItems().get(0);
                    if (newPost.getIsPinned() == 1)
                        newPost = response.getItems().get(1);
                } else {
                    TimeUnit.SECONDS.sleep(1);
                    continue;
                }
                TimeUnit.MICROSECONDS.sleep(330);
                return newPost;
            } catch (ClientException e) {
                TimeUnit.SECONDS.sleep(1);
                if (i == 3) throw e;
            }
        }
        return null;
    }

    public List<WallpostFull> getPostsByIdList(List<String> idList) throws ClientException, ApiException, InterruptedException {
        List<WallpostFull> wallPosts;
        for (int i = 0; i <= 3; i++) {
            try {
                wallPosts = vk.wall().getById(idList).execute();
                TimeUnit.MICROSECONDS.sleep(500);
                return wallPosts;
            } catch (ClientException e) {
                if (i == 3) throw e;
                TimeUnit.SECONDS.sleep(1);
            }
        }
        return null;
    }

    public List<GroupFull> loadGroups(List<String> groupIds) throws ClientException, ApiException {

        List<GroupFull> response = vk.groups()
                .getById(serviceActor)
                .groupIds(groupIds)
                .fields(GroupField.MEMBERS_COUNT).execute();
        return response;

    }

    /*private void vkUpdater() {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("Started");
                Integer i = 0;
                while (true) {

                    loadNewPosts();
                    updateLoadedPosts();
                    //List<Post> savedPosts = ;
                    final Date currentDate = new Date();
//                    System.out.println((int) currentDate.getTime() - postService.findAll().get(0).getStartDate()*1000);
                    List<String> requiredPosts = postService.findAll()
                            .stream()
                            .filter(x ->  (int) currentDate.getTime() - x.getStartDate()*1000 < 1000*60*60*12)
                            .map(x -> x.getGroupId()+"_"+x.getPostId())
                            .collect(Collectors.toList());
                    System.out.println(requiredPosts.toString());
                    if (!requiredPosts.isEmpty()) {
                        vk.wall()
                                .getById(requiredPosts)
                                .execute()
                                .stream()
                                .map(x -> new PostInfo(
                                        postService.findByPostAndGroup(x.getId(), x.getOwnerId()),
                                        x.getLikes().getCount(),
                                        x.getReposts().getCount(),
                                        currentDate
                                ))
                                .collect(Collectors.toList())
                                .forEach(x -> postService.savePostInfo(x));
                        // Collection.forEach(x -> postService.savePostInfo(x));
                    }

                    //vk.execute().batch(n)

                    Thread.sleep(30000L);
                    //for (Integer id : publics) {

                        GetResponse response = vk.wall()
                                .get()
                                .ownerId(PUBLIC_ID)
                                .count(2)
                                .execute();

                        if (response.getItems().size() == 2) {
                            WallpostFull post = response.getItems().get(0);
                            if (post.getIsPinned() == 1)
                                post = response.getItems().get(1);
                            if (!postService.exists(post.getId(), post.getOwnerId())) {
                                System.out.println("NEW POST:" + String.format(HREF_PATTERN, PUBLIC_ID * -1, PUBLIC_ID * -1, post.getId()));

                                Post postEntity = postService.findByPostAndGroup(post.getId(), post.getOwnerId());
                                if (postEntity == null)
                                    postEntity = postService.savePost(new Post(
                                            post.getId(),
                                            post.getOwnerId(),
                                            post.getDate()
                                    ));
                                PostInfo postInfo = new PostInfo(
                                        postEntity,
                                        post.getLikes().getCount(),
                                        post.getReposts().getCount(),
                                        new Date()
                                );
                                postService.savePostInfo(postInfo);
                            }
                            // Thread.sleep(1000L);
                            //}
                        }

                    i++;
                    Thread.sleep(30000L);
                }

            } catch (ApiException | ClientException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }*/


}

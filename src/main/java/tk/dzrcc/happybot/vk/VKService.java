package tk.dzrcc.happybot.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.wall.WallGetByIdQuery;
import com.vk.api.sdk.queries.wall.WallGetByIdQueryWithExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.dzrcc.happybot.entity.Post;
import tk.dzrcc.happybot.entity.PostInfo;
import tk.dzrcc.happybot.service.PostService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maksim on 01.03.2017.
 */
@Service
public class VKService {

    @Autowired
    private PostService postService;

    private TransportClient transportClient;
    private VkApiClient vk;
    private static final Integer PUBLIC_ID = -57846937;
    List<Integer> publics;
    private static final String HREF_PATTERN = "https://vk.com/public%s?w=wall-%s_%s";

    @PostConstruct
    void init() {
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        publics = new ArrayList<>();
        publics.add(-57846937);
        publics.add(-45745333);
        publics.add(-45441631);
        publics.add(-460389);
        publics.add(-31480508);

        vkUpdater();

    }

    private void vkUpdater() {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("Started");
                Integer i = 0;
                while (true) {

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
    }
}

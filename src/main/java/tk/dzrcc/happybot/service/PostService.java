package tk.dzrcc.happybot.service;

import com.vk.api.sdk.objects.wall.WallpostFull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.entity.*;
import tk.dzrcc.happybot.repository.PostRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Maksim on 01.03.2017.
 */
@Service
public class PostService {
    @Autowired
    PostRepository postRepository;


    @Autowired
    GroupService groupService;

    @Autowired
    PostsAnalyzeService postsAnalyzeService;

    @Transactional
    public Post savePost(Post post){

        return postRepository.save(post);
    }

    @Transactional
    public boolean exists(Integer postId, Integer groupId){
        return postRepository.existsByValues(postId, groupId);
    }

    @Transactional
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<Post>();
        for (Post post : postRepository.findAll()) {
            posts.add(post);
        }
        return posts;
    }

    @Transactional
    public Post findByPostAndGroup(Integer postId, Integer groupId){
        List<Post> postList = postRepository.findByPostIdAndGroupId(postId, groupId);
        return postList.isEmpty() ? null : postList.get(0);
    }

    @Transactional
    public Post savePostByWallPost(WallpostFull wallPost) {
        if (!postRepository.existsByValues(wallPost.getId(), wallPost.getOwnerId())) {
            Post post = new Post();
            post.setPostId(wallPost.getId());
            post.setGroup(groupService.getById(wallPost.getOwnerId()));
            post.setStartDate(new Date(wallPost.getDate()*1000));
            return postRepository.save(post);
        } else
            return null;
    }

    @Transactional
    public List<Post> getNotUpdatedPosts() {
        return postRepository.findNotUpdatedPosts();
    }

    @Transactional
    public Post updatePost(Post post, WallpostFull wallPost) {
        Integer likes = wallPost.getLikes().getCount();
        Integer reposts = wallPost.getReposts().getCount();
        Integer views = (likes * ((new Random()).nextInt(200)+2400))/100;//wallPost.getViews();
        Integer mark = postsAnalyzeService.calculateMark(likes, reposts, views, wallPost.getOwnerId());
        groupService.updateGroup(post.getGroup(), likes, reposts, views);

        post.setLikes(likes);
        post.setReposts(reposts);
        post.setViews(views);
        post.setMark(mark);
        return postRepository.save(post);
    }
}

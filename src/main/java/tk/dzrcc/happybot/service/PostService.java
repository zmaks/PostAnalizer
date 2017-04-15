package tk.dzrcc.happybot.service;

import com.vk.api.sdk.objects.wall.WallpostFull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.Utils;
import tk.dzrcc.happybot.entity.*;
import tk.dzrcc.happybot.exceptions.UpdaterException;
import tk.dzrcc.happybot.repository.PostRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maksim on 01.03.2017.
 */
@Service
public class PostService {
    private static Logger LOGGER = LoggerFactory.getLogger(PostService.class);


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
        List<Post> postList = postRepository.findByPostIdAndGroupGroupId(postId, groupId);
        return postList.isEmpty() ? null : postList.get(0);
    }

    @Transactional
    public Post savePostByWallPost(WallpostFull wallPost) {
        if (wallPost != null && !postRepository.existsByValues(wallPost.getId(), wallPost.getOwnerId())) {
            Post post = new Post();
            post.setPostId(wallPost.getId());
            post.setGroup(groupService.getById(wallPost.getOwnerId()));
            post.setStartDate(new Date((long) wallPost.getDate()*1000));
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
        if(wallPost == null || !postsAnalyzeService.isCorrectPost(wallPost)){
            LOGGER.debug("Deleting post {}", post.getPostId());
            //postRepository.delete(post.getId());
            return null;
        }
        if (wallPost.getLikes() == null || wallPost.getReposts() == null || wallPost.getViews() == null) {
            return null;
        }
        Integer likes = wallPost.getLikes().getCount();
        Integer reposts = wallPost.getReposts().getCount();
        Integer views = wallPost.getViews().getCount();
        Integer hour = Utils.getCurrentHour();
        Integer mark = postsAnalyzeService.calculateMark(likes, reposts, views, wallPost.getOwnerId(), hour);
        Integer maxMark = postsAnalyzeService.getMaxMark();
        groupService.updateGroup(post.getGroup(), likes, reposts, views);

        if (mark > maxMark){
            postRepository.updateOldMarksInHour(hour, mark, maxMark);
            mark = maxMark;
        }

        post.setLikes(likes);
        post.setReposts(reposts);
        post.setViews(views);
        post.setMark(mark);
        post.setHour(hour);
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(WallpostFull wallPost) throws UpdaterException {
        if (wallPost == null) {
            return null;
        }
        List<Post> posts = postRepository.findByPostIdAndGroupGroupId(wallPost.getId(), wallPost.getOwnerId());
        if (posts.isEmpty()) {
            throw new UpdaterException("Post " + wallPost.getOwnerId()+ "_" + wallPost.getId() + "doesn't exist");
        }
        return updatePost(posts.get(0), wallPost);
    }

    public void deletePosts(List<Post> postsForDelete) {
        if (postsForDelete == null || postsForDelete.isEmpty()) return;
        for (Post post : postsForDelete){
            LOGGER.debug("Deleting post {}", post.getPostId());
            postRepository.delete(post.getId());

        }
    }
}

package tk.dzrcc.happybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.dzrcc.happybot.entity.Post;
import tk.dzrcc.happybot.entity.PostInfo;
import tk.dzrcc.happybot.entity.PostPK;
import tk.dzrcc.happybot.repository.PostInfoRepository;
import tk.dzrcc.happybot.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maksim on 01.03.2017.
 */
@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostInfoRepository postInfoRepository;

    @Transactional
    public Post savePost(Post post){

        return postRepository.save(post);
    }

    public boolean exists(Integer postId, Integer groupId){
        return postRepository.existsByValues(postId, groupId);
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<Post>();
        for (Post post : postRepository.findAll()) {
            posts.add(post);
        }
        return posts;
    }

    public PostInfo savePostInfo(PostInfo postInfo) {
        return postInfoRepository.save(postInfo);
    }

    public Post findByPostAndGroup(Integer postId, Integer groupId){
        List<Post> postList = postRepository.findByPostIdAndGroupId(postId, groupId);
        return postList.isEmpty() ? null : postList.get(0);
    }

}

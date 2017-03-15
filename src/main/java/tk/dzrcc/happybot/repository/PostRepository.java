package tk.dzrcc.happybot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tk.dzrcc.happybot.entity.Post;

import java.util.List;


/**
 * Created by Maksim on 28.02.2017.
 */
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM Post u WHERE u.postId = ?1 and u.groupId = ?2")
    Boolean existsByValues(Integer postId, Integer groupId);

    List<Post> findByPostIdAndGroupId(Integer postId, Integer groupId);

    @Query("SELECT id FROM Post WHERE  startDate > (CURRENT_TIMESTAMP - INTERVAL '1 hour') and mark = null")
    List<Post> findNotUpdatedPosts();
}

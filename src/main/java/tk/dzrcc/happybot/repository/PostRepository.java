package tk.dzrcc.happybot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tk.dzrcc.happybot.entity.Post;

import java.util.List;


/**
 * Created by Maksim on 28.02.2017.
 */
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM Post u WHERE u.postId = ?1 and u.group.groupId = ?2")
    Boolean existsByValues(Integer postId, Integer groupId);

    List<Post> findByPostIdAndGroupGroupId(Integer postId, Integer groupId);

    @Query(value = "SELECT * FROM Post WHERE start_date < (CURRENT_TIMESTAMP - INTERVAL '1 hour') and mark is null", nativeQuery = true)
    List<Post> findNotUpdatedPosts();
    List<Post> findByMarkIsNull();
}

package tk.dzrcc.happybot.repository;

import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = "SELECT * FROM Post WHERE start_date < (CURRENT_TIMESTAMP - INTERVAL '1 hour') and start_date > (CURRENT_TIMESTAMP - INTERVAL '1 hour 5 minutes') and mark is null", nativeQuery = true)
    List<Post> findNotUpdatedPosts();

    @Modifying
    @Query(value = "UPDATE post SET mark = p2.new_mark FROM (SELECT mark* (?3) *1.0/ (?2) as new_mark, id FROM post where hour = (?1) and mark is not null) p2 WHERE p2.id = post.id" , nativeQuery = true)
    void updateOldMarksInHour(Integer hour, Integer mark, Integer maxMark);

    List<Post> findByMarkIsNull();

    Post findByHourAndMarkIsNotNull(Integer hour);
}

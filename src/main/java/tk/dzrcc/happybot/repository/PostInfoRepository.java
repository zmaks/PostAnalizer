package tk.dzrcc.happybot.repository;

import org.springframework.data.repository.CrudRepository;
import tk.dzrcc.happybot.entity.PostInfo;

/**
 * Created by Maksim on 01.03.2017.
 */
public interface PostInfoRepository extends CrudRepository<PostInfo, Long> {
}

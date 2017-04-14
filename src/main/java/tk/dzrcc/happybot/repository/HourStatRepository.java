package tk.dzrcc.happybot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tk.dzrcc.happybot.entity.HourStat;

import java.util.List;

/**
 * Created by Maksim on 15.03.2017.
 */
public interface HourStatRepository extends CrudRepository<HourStat, Integer> {
    List<HourStat> findByGroupIdAndHour(Integer groupId, Integer hour);

    List<HourStat> findByHour(Integer hour);
}

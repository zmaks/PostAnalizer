package tk.dzrcc.happybot.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Maksim on 15.03.2017.
 */
public class HourStat {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private Integer likes;

    private Integer reposts;

    private Float maxLikesRatio;

    private Float maxRepostsRatio;

    private Integer groupId;

    private Integer hour;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getReposts() {
        return reposts;
    }

    public void setReposts(Integer reposts) {
        this.reposts = reposts;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}

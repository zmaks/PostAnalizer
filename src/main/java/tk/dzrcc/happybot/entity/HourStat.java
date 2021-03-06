package tk.dzrcc.happybot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Maksim on 15.03.2017.
 */
@Entity
public class HourStat {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column
    private Float avgLikesRatio;

    @Column
    private Float avgRepostsRatio;

    @Column
    private Float maxLikesRatio;

    @Column
    private Float maxRepostsRatio;

    @Column
    private Integer groupId;

    @Column
    private Integer hour;

    @Column
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Float getAvgLikesRatio() {
        return avgLikesRatio;
    }

    public void setAvgLikesRatio(Float avgLikesRatio) {
        this.avgLikesRatio = avgLikesRatio;
    }

    public Float getAvgRepostsRatio() {
        return avgRepostsRatio;
    }

    public void setAvgRepostsRatio(Float avgRepostsRatio) {
        this.avgRepostsRatio = avgRepostsRatio;
    }

    public Float getMaxLikesRatio() {
        return maxLikesRatio;
    }

    public void setMaxLikesRatio(Float maxLikesRatio) {
        this.maxLikesRatio = maxLikesRatio;
    }

    public Float getMaxRepostsRatio() {
        return maxRepostsRatio;
    }

    public void setMaxRepostsRatio(Float maxRepostsRatio) {
        this.maxRepostsRatio = maxRepostsRatio;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

package tk.dzrcc.happybot.entity;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by mazh0416 on 3/14/2017.
 */
public class VkGroup {

    @Id
    private Integer groupId;

    @Column
    private String name;

    @Column
    private Integer count;

    @Column
    private Integer avgLikes;

    @Column
    private Integer avgShares;

    @Column
    private Integer hour;

    public VkGroup() {
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getAvgLikes() {
        return avgLikes;
    }

    public void setAvgLikes(Integer avgLikes) {
        this.avgLikes = avgLikes;
    }

    public Integer getAvgShares() {
        return avgShares;
    }

    public void setAvgShares(Integer avgShares) {
        this.avgShares = avgShares;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}

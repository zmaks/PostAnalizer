package tk.dzrcc.happybot.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

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
    private Integer avgViews;

    @Column
    private Date addingDate;

    @Column
    private Integer membersCount;

    @Column
    private Boolean active = true;

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

    public Integer getAvgViews() {
        return avgViews;
    }

    public void setAvgViews(Integer avgViews) {
        this.avgViews = avgViews;
    }

    public Date getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(Date addingDate) {
        this.addingDate = addingDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }
}

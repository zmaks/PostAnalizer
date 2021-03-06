package tk.dzrcc.happybot.entity;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Maksim on 28.02.2017.
 */
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Integer postId;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private VkGroup group;

    @Column
    private Date startDate;

    @Column
    private Integer likes;

    @Column
    private Integer reposts;

    @Column
    private Integer views;

    @Column
    private Integer mark;

    @Column
    private Integer hour;


    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public VkGroup getGroup() {
        return group;
    }

    public void setGroup(VkGroup group) {
        this.group = group;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
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

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (getPostId() != null ? !getPostId().equals(post.getPostId()) : post.getPostId() != null) return false;
        return getGroup() != null ? getGroup().equals(post.getGroup()) : post.getGroup() == null;
    }

    @Override
    public int hashCode() {
        int result = getPostId() != null ? getPostId().hashCode() : 0;
        result = 31 * result + (getGroup() != null ? getGroup().hashCode() : 0);
        return result;
    }
}

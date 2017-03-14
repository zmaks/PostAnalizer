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

    @Column
    private Integer groupId;

    @Column
    private Integer startDate;


    public Post() {
    }

    public Post(Integer postId, Integer groupId, Integer startDate) {
        this.postId = postId;
        this.groupId = groupId;
        this.startDate = startDate;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }
}

package tk.dzrcc.happybot.entity;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Maksim on 01.03.2017.
 */
@Entity
@Deprecated
public class PostInfo {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @Column
    private Integer likes;

    @Column
    private Integer reposts;

    @Column
    private Date date;

    public PostInfo() {
    }

    public PostInfo(Post post, Integer likes, Integer reposts, Date date) {
        this.post = post;
        this.likes = likes;
        this.reposts = reposts;
        this.date = date;
        //this.num = num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}

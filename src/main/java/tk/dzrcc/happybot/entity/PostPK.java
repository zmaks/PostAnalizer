package tk.dzrcc.happybot.entity;

import java.io.Serializable;

/**
 * Created by Maksim on 01.03.2017.
 */
public class PostPK implements Serializable {
    private Integer postId;
    private Integer groupId;

    public PostPK() {
    }

    public PostPK(Integer postId, Integer groupId) {
        this.postId = postId;
        this.groupId = groupId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostPK)) return false;

        PostPK postPK = (PostPK) o;

        if (getPostId() != null ? !getPostId().equals(postPK.getPostId()) : postPK.getPostId() != null) return false;
        return getGroupId() != null ? getGroupId().equals(postPK.getGroupId()) : postPK.getGroupId() == null;
    }

    @Override
    public int hashCode() {
        int result = getPostId() != null ? getPostId().hashCode() : 0;
        result = 31 * result + (getGroupId() != null ? getGroupId().hashCode() : 0);
        return result;
    }
}

package hue.edu.xiong.model;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "user_like")
public class UserLike {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;
    @Column(name = "item_id")
    private String itemId;
    @Column(name = "item_type")
    private Integer itemType;
    @Column(name = "create_time")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

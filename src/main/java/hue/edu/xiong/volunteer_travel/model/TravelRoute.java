package hue.edu.xiong.volunteer_travel.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "travel_route")
public class TravelRoute {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "routeName")
    private String name;

    @Column(name = "routeDescribe")
    private String describe;

    @Column(name = "routeAddress")
    private String address;

    @Column(name = "routeStatus")
    private Integer status;

    @Column(name = "collect_number")
    private Integer collectNumber;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "error_message")
    private String errorMessage;

    @Transient
    private Integer likeNum;
    @Transient
    private Integer preNum;
    @Transient
    private List<UserComment> commentList;

    public List<UserComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<UserComment> commentList) {
        this.commentList = commentList;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getPreNum() {
        return preNum;
    }

    public void setPreNum(Integer preNum) {
        this.preNum = preNum;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescribe() { return describe; }

    public void setDescribe(String describe) { this.describe = describe; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public Integer getCollectNumber() { return collectNumber; }

    public void setCollectNumber(Integer collectNumber) { this.collectNumber = collectNumber; }

    public Date getCreateDate() { return createDate; }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public Date getUpdateDate() { return updateDate; }

    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

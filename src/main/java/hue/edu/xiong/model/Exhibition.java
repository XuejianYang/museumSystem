package hue.edu.xiong.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exhibition")
public class Exhibition {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "image")
    private String image;
    @Column(name = "images")
    private String images;
    @Column(name = "exhibitionName")
    private String name;
    @Column(name = "exhibitionAddress")
    private String address;
    @Column(name = "exhibitionDescribe")
    private String describe;
    @Column(name = "exhibitionStatus")
    private Integer status;
    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "startDate")
    private String startDate;
    @Column(name = "endDate")
    private Date endDate;
    @Transient
    private Integer likeNum;
    @Transient
    private Integer preNum;
    @Transient
    private List<UserComment> commentList;
    @Transient
    private List<String> imageList;

    public List<UserComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<UserComment> commentList) {
        this.commentList = commentList;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
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

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getDescribe() { return describe; }

    public void setDescribe(String describe) { this.describe = describe; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateDate() { return createDate; }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public Date getEndDate() {return endDate;}

    public void setEndDate(Date endDate) {this.endDate = endDate;}

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}

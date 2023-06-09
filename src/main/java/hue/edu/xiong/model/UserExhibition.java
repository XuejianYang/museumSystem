package hue.edu.xiong.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_exhibition")
public class UserExhibition {
    @Id
    @Column(name = "id")
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @Column(name = "user_exhibition_describe")
    private String describe;

    @Column(name = "createDate")
    private Date createDate;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public String getDescribe() { return describe; }

    public void setDescribe(String describe) { this.describe = describe; }

    public Date getCreateDate() { return createDate; }

    public void setCreateDate(Date createDate) { this.createDate = createDate;}
}

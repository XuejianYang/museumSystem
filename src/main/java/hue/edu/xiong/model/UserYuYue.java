package hue.edu.xiong.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author yxj
 * @Description //TODO $
 * @Date $ $
 * @Version 1.0
 **/
@Entity
@Table(name = "user_yuyue")
public class UserYuYue {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;
    @Column(name = "card")
    private String card;
    @Column(name = "phone")
    private String phone;
    @Column(name = "date")
    private String date;
    @Column(name = "during")
    private String during;
    @Column(name = "yuyueer")
    private String yuyueer;

    public String getDuring() {
        return during;
    }

    public void setDuring(String during) {
        this.during = during;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYuyueer() {
        return yuyueer;
    }

    public void setYuyueer(String yuyueer) {
        this.yuyueer = yuyueer;
    }
}

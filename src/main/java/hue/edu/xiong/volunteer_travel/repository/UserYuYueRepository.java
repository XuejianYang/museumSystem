package hue.edu.xiong.volunteer_travel.repository;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.model.UserYuYue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface UserYuYueRepository extends JpaRepository<UserYuYue, String>, JpaSpecificationExecutor<UserYuYue> {



    List<UserYuYue> findUserYuYueByDate(String date);
    List<UserYuYue> findUserYuYueByCard(String card);
    List<UserYuYue> findUserYuYueByName(String name);
    List<UserYuYue> findUserYuYueByYuyueer(String name);

    void deleteById(String id);
}

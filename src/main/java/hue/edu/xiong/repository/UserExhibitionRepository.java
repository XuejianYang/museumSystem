package hue.edu.xiong.repository;

import hue.edu.xiong.model.Exhibition;
import hue.edu.xiong.model.User;
import hue.edu.xiong.model.UserExhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserExhibitionRepository extends JpaRepository<UserExhibition, String>, JpaSpecificationExecutor<UserExhibition> {

    List<UserExhibition> findUserExhibitionByUser(User user);
    List<UserExhibition> findUserExhibitionByExhibition(Exhibition exhibition);


    UserExhibition findUserExhibitionByExhibitionAndUser(Exhibition exhibition, User user);
}

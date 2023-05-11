package hue.edu.xiong.volunteer_travel.repository;

import hue.edu.xiong.volunteer_travel.model.User;
import hue.edu.xiong.volunteer_travel.model.Information;
import hue.edu.xiong.volunteer_travel.model.UserStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserStrategyRepository extends JpaRepository<UserStrategy, String>, JpaSpecificationExecutor<UserStrategy> {
    // 用户收藏的
    List<UserStrategy> findUserStrategyByUser(User user);

    UserStrategy findUserStrategyByInformationAndUser(Information information, User user);
}

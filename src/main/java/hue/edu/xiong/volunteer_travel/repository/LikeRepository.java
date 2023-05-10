package hue.edu.xiong.volunteer_travel.repository;

import hue.edu.xiong.volunteer_travel.model.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LikeRepository extends JpaRepository<UserLike, String>, JpaSpecificationExecutor<UserLike> {

    List<UserLike> findLikeByUserIdAndItemId(String userId, String id);

    List<UserLike> findLikeByItemId(String id);
}

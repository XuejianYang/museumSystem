package hue.edu.xiong.repository;

import hue.edu.xiong.model.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LikeRepository extends JpaRepository<UserLike, String>, JpaSpecificationExecutor<UserLike> {

    List<UserLike> findLikeByUserIdAndItemId(String userId, String id);

    List<UserLike> findLikeByItemId(String id);
}

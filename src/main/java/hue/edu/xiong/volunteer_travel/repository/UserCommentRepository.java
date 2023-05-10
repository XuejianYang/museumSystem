package hue.edu.xiong.volunteer_travel.repository;

import hue.edu.xiong.volunteer_travel.model.UserComment;
import hue.edu.xiong.volunteer_travel.model.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserCommentRepository extends JpaRepository<UserComment, String>, JpaSpecificationExecutor<UserComment> {

    List<UserComment> findUserCommentByUserIdAndItemId(String userId, String id);

    List<UserComment> findUserCommentByItemId(String id);
}

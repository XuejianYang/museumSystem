package hue.edu.xiong.repository;

import hue.edu.xiong.model.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserCommentRepository extends JpaRepository<UserComment, String>, JpaSpecificationExecutor<UserComment> {

    List<UserComment> findUserCommentByUserIdAndItemId(String userId, String id);

    List<UserComment> findUserCommentByItemId(String id);
}

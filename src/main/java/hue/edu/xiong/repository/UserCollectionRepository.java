package hue.edu.xiong.repository;

import hue.edu.xiong.model.Collection;
import hue.edu.xiong.model.UserCollection;
import hue.edu.xiong.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCollectionRepository extends JpaRepository<UserCollection, String>, JpaSpecificationExecutor<UserCollection> {
    List<UserCollection> findUserCollectionsByUser(User user);

    UserCollection findUserCollectionByCollectionAndUser(Collection collection, User user);


}

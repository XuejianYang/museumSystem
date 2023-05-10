package hue.edu.xiong.volunteer_travel.repository;

import hue.edu.xiong.volunteer_travel.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, String>, JpaSpecificationExecutor<Collection> {

}

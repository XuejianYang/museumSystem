package hue.edu.xiong.volunteer_travel.repository;

import hue.edu.xiong.volunteer_travel.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, String>, JpaSpecificationExecutor<Exhibition> {

}
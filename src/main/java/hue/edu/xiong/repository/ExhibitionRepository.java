package hue.edu.xiong.repository;

import hue.edu.xiong.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, String>, JpaSpecificationExecutor<Exhibition> {

}
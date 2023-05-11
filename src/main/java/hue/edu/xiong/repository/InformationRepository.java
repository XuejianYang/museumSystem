package hue.edu.xiong.repository;

import hue.edu.xiong.model.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InformationRepository extends JpaRepository<Information, String>, JpaSpecificationExecutor<Information> {



}

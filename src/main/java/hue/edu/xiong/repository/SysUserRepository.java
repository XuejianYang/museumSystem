package hue.edu.xiong.repository;

import hue.edu.xiong.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Integer>, JpaSpecificationExecutor<SysUser> {
    SysUser findSysUserByUsername(String username);
}
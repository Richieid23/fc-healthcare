package id.web.fitrarizki.healthcare.repository;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import id.web.fitrarizki.healthcare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);

    @Query(value = """
        SELECT r.* FROM roles r 
        JOIN user_roles ur ON ur.role_id = r.id
        JOIN users u ON u.id = ur.user_id
        WHERE u.id = :userId
        """, nativeQuery = true)
    List<Role> findByUserId(Long userId);
}

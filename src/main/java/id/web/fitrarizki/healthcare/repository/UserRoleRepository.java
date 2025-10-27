package id.web.fitrarizki.healthcare.repository;

import id.web.fitrarizki.healthcare.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
    void deleteByIdUserId(Long userId);

    @Query(value = """
    SELECT * FROM user_roles
    WHERE user_id = :userId
    AND role_id = :roleId
    LIMIT 1
    """, nativeQuery = true)
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
}

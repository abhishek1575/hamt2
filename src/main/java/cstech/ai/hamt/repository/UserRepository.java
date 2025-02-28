package cstech.ai.hamt.repository;

import cstech.ai.hamt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM user WHERE email =?1;", nativeQuery = true)
    Optional<User> findByEmailId(String email);

    Optional<User> findById(Integer id);

    @Query(value = "SELECT * FROM user;", nativeQuery = true)
    List<User> getAll();

    @Query(value = "SELECT * FROM user WHERE role = 'USER';", nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = "SELECT * FROM user WHERE role = 'ADMIN';", nativeQuery = true)
    List<User> getAllAdmins();

    @Query(value = "SELECT * FROM user WHERE role = 'SUPER_ADMIN';", nativeQuery = true)
    List<User> getAllSuperAdmins();
}


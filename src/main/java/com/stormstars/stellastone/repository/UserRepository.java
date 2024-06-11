package com.stormstars.stellastone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stormstars.stellastone.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    // @Query("Select u from User u where CONCAT (u.firstname,' ',u.lastname,'
    // ',u.username) LIKE %?1%")
    // List<User> search(String keyword);
    @Query("SELECT u FROM User u WHERE u.username LIKE %?1% OR u.firstname LIKE %?1% OR u.lastname LIKE %?1%")
    List<User> search(String keyword);
    // public Fusee findFuseeByUserId(Long id);

    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email);

    public User findByResetPasswordToken(String token);

    // ArrayList<Long> findIdFuseeByUser(Long userId);

}

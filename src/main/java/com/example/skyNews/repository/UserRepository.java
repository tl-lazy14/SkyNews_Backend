package com.example.skyNews.repository;

import com.example.skyNews.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndRole(String email, String role);
    boolean existsByEmail(String email);
    boolean existsByEmailAndRole(String email, String role);
    Optional<User> findByEditorCategory(String category);
    List<User> findByRole(String role);
    @Query("SELECT u FROM User u WHERE u.role = :role " +
            "AND (u.username LIKE %:searchQuery% OR u.email LIKE %:searchQuery%)")
    List<User> findByRoleAndNameContainingOrEmailContaining(@Param("role") String role,
                                                            @Param("searchQuery") String searchQuery,
                                                            Pageable pageable);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role " +
            "AND (u.username LIKE %:searchQuery% OR u.email LIKE %:searchQuery%)")
    int countByRoleAndNameContainingOrEmailContaining(@Param("role") String role,
                                                            @Param("searchQuery") String searchQuery);
}

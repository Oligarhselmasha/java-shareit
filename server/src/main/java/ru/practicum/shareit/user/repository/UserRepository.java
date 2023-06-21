package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where upper(u.email) = upper(?1) order by u.id")
    Optional<User> findByEmailIgnoreCaseOrderByIdAsc(String email);

    boolean existsByEmail(String email);
}

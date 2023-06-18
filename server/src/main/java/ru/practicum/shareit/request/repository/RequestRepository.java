package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("select r from Request r where r.user.id = ?1 order by r.created DESC")
    List<Request> findByUser_IdOrderByCreatedDesc(int id);

    @Query("select r from Request r where r.user.id <> ?1 order by r.created DESC")
    List<Request> findByUser_IdNotOrderByCreatedDesc(int id);

    @Query("select r from Request r order by r.created DESC")
    List<Request> findByOrderByCreatedDesc();
}

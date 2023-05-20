package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("select i from Item i where i.user.id = ?1")
    List<Item> findByUser_Id(int id);

    @Query("select i from Item i where upper(i.description) like upper(concat('%', ?1, '%')) and i.isFree = true")
    List<Item> findByDescriptionContainsIgnoreCaseAndIsFreeTrue(String description);



}

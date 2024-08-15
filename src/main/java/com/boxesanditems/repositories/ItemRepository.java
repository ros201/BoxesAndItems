package com.boxesanditems.repositories;

import com.boxesanditems.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Integer> {
    @Query(
            value = "select id from item where color = :color and contained_in in (:containedInList)",
            nativeQuery = true)
    List<Integer> findContainedItemsByColor(
            @Param("color") String color,
            @Param("containedInList") List<Integer> containedInList);
}

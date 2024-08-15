package com.boxesanditems.repositories;

import com.boxesanditems.entities.BoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxRepository extends JpaRepository<BoxEntity,Integer> {
    @Query(
            value = "select id from box where contained_in = :containedIn",
            nativeQuery = true)
    List<Integer> findContainedBoxIds(
            @Param("containedIn") Integer containedIn);
}

package com.boxesanditems.service;

import com.boxesanditems.entities.BoxEntity;
import com.boxesanditems.repositories.BoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxService {
    private final BoxRepository boxRepository;

    public void saveAll(List<BoxEntity> boxEntities) {
        boxRepository.saveAll(boxEntities);
    }

    public List<Integer> findContainedBoxIds(Integer containedIn) {
        return boxRepository.findContainedBoxIds(containedIn);
    }
}

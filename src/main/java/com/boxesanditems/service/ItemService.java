package com.boxesanditems.service;

import com.boxesanditems.entities.ItemEntity;
import com.boxesanditems.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public void saveAll(List<ItemEntity> itemEntities) {
        itemRepository.saveAll(itemEntities);
    }

    public List<Integer> findContainedItemIdsByColor(String color, List<Integer> resultBoxIdList) {
        return itemRepository.findContainedItemsByColor(color, resultBoxIdList);
    }
}

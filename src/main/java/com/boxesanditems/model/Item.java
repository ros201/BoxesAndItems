package com.boxesanditems.model;

import com.boxesanditems.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private Integer id;
    private String color;
    private Integer containedIn;

    public Item(ItemEntity itemEntity) {
        this.id = itemEntity.getId();
        this.color = itemEntity.getColor();
        this.containedIn = itemEntity.getContainedIn().getId();
    }
}

package com.boxesanditems.model;

import com.boxesanditems.entities.BoxEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Box {
    private Integer id;
    private Integer containedIn;

    public Box(BoxEntity boxEntity) {
        this.id = boxEntity.getId();
        this.containedIn = boxEntity.getContainedIn();
    }
}

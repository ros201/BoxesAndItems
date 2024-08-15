package com.boxesanditems.model;

import com.boxesanditems.entities.BoxEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Box {
    private Integer id;
    private Integer containedIn;
}

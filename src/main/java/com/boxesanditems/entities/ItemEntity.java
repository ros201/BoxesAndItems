package com.boxesanditems.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Item")
public class ItemEntity {
    @Id
    @Column
    private Integer id;

    @Column(name = "color")
    private String color;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "contained_in")
    private BoxEntity containedIn;
}

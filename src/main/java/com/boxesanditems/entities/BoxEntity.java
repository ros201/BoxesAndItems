package com.boxesanditems.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "box")
public class BoxEntity {
    @Id
    @Column
    private Integer id;

    @Column(name = "contained_in")
    private Integer containedIn;
}

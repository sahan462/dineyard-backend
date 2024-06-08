package com.Damitha.Online.Food.Ordering.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderItemId;

    @ManyToOne
    private Food food;

    private int quantity;
    private long totalPrice;


    private List<String> ingredients;




}

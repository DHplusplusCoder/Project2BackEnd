package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private int quantity;
    @Column
    private double price;
    @Column
    private String description;
    @Column
    private String image;
    @Column
    private String name;
    @Column
    private boolean featured;
    @Column
    private Double sale;
}

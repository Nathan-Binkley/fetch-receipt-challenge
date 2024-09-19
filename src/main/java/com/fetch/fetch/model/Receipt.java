package com.fetch.fetch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receipt")
public class Receipt {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    private String points;
    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;
    private String total;
}

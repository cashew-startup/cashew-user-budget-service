package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String name;

    private String description;

    private String currency;

    private BigDecimal pricePerUnit;

    private Double quantity;

    private BigDecimal totalPrice;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductShare> shares;


//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
//    @CreatedDate
//    private LocalDateTime date;
}

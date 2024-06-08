package com.Damitha.Online.Food.Ordering.model;

import com.Damitha.Online.Food.Ordering.dto.RestaurantDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Integer userId;
    private String name;
    private String email;
    private String password;

    @OneToOne
    private userRole role;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<Orders> orders = new ArrayList<>();

    @ElementCollection
    private List<RestaurantDto>  favourites = new ArrayList<RestaurantDto>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // one user has multiple addresses, and a user object is deleted the addresses are also deleted, orphanremoval ensures that the child entities are removed whenever parent entities are removed
    private List<Address> addresses = new ArrayList<>();
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}

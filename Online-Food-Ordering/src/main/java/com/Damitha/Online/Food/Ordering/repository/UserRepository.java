package com.Damitha.Online.Food.Ordering.repository;

import com.Damitha.Online.Food.Ordering.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String username);
}

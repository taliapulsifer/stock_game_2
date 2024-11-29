package com.example.demo.Repositories;

import com.example.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public class UserRepo {
    public interface UserRepository extends JpaRepository<User, Long> {}

}

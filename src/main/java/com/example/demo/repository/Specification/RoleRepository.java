package com.example.demo.repository.Specification;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository  extends JpaRepository<Role, String> {
    List<Role> findByName(String name);
}

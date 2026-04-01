package com.db.database.repository;

import com.db.database.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface RolesRepository extends JpaRepository<Role,Long> {
   Role findByName(String name);
}

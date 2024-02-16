package com.Spring.SpringBoot.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Spring.SpringBoot.Models.Authority;
@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    
}

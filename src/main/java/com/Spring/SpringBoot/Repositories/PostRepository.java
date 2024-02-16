package com.Spring.SpringBoot.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Spring.SpringBoot.Models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post , Long>{
    
}

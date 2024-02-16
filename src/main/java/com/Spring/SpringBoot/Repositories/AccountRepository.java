package com.Spring.SpringBoot.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Spring.SpringBoot.Models.Account;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{

    Optional<Account> findOneByEmailIgnoreCase(String email);
    
    Optional<Account> findByPasswordResetToken(String passwordresettoken); 
}

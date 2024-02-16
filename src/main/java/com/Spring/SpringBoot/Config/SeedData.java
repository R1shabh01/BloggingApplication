package com.Spring.SpringBoot.Config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Spring.SpringBoot.Constants.Privilege;
import com.Spring.SpringBoot.Constants.Roles;
import com.Spring.SpringBoot.Models.Account;
import com.Spring.SpringBoot.Models.Authority;
import com.Spring.SpringBoot.Models.Post;
import com.Spring.SpringBoot.Services.AccountService;
import com.Spring.SpringBoot.Services.AuthorityService;
import com.Spring.SpringBoot.Services.PostService;

@Component
public class SeedData implements CommandLineRunner {
    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthorityService authorityService;
    @Override
    public void run(String... args) throws Exception {
        
        for(Privilege priv : Privilege.values()){
            Authority authority = new Authority();
            authority.setId(priv.getAuthId());
            authority.setName(priv.getAuthString());
            authorityService.save(authority);

        }
        
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();
        Account account4 = new Account();

        account1.setEmail("rishabh2000singh@gmail.com");
        account1.setPassword("password");
        account1.setFirstname("user1");
        account1.setLastname("lastname1");
        account1.setAge(20);
        account1.setDate_of_birth(LocalDate.parse("2003-10-10"));
        account1.setGender("Male");

        account2.setEmail("account2@gmail.com");
        account2.setPassword("password");
        account2.setFirstname("user2");
        account2.setLastname("lastname2");
        account2.setRole(Roles.ADMIN.getRole());
        account2.setGender("Male ");
        account2.setAge(21);
        account2.setDate_of_birth(LocalDate.parse("2002-10-10"));

        account3.setEmail("account3@gmail.com");
        account3.setPassword("password");
        account3.setFirstname("user3");
        account3.setLastname("lastname3");
        account3.setRole(Roles.EDITOR.getRole());
        account3.setGender("Male");
        account3.setAge(22);
        account3.setDate_of_birth(LocalDate.parse("2001-10-10"));

        account4.setEmail("account4@gmail.com");
        account4.setPassword("password");
        account4.setFirstname("user4");
        account4.setLastname("lastname4");
        account4.setRole(Roles.EDITOR.getRole());
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privilege.RESET_ANY_USER_PASSWORD.getAuthId()).ifPresent(authorities::add);
        authorityService.findById(Privilege.ACCESS_ADMIN_PANEL.getAuthId()).ifPresent(authorities::add);
        account4.setAuthorities(authorities); 
        account4.setGender("Female");
        account4.setAge(23);
        account4.setDate_of_birth(LocalDate.parse("2000-10-10"));

        accountService.save(account1);
        accountService.save(account2);
        accountService.save(account3);
        accountService.save(account4);

        List<Post> posts = postService.getAll();
        if (posts.size() == 0){
            Post post1 = new Post();
            post1.setTitle("Post 1");
            post1.setBody("post 1 Body is here .................");
            post1.setAccount(account1);
            postService.save(post1);

            Post post2 = new Post();
            post2.setTitle("Post 2");
            post2.setBody("post 2 Body is here .................");
            post2.setAccount(account2);
            postService.save(post2);
        }
    }
    
}

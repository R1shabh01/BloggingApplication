package com.Spring.SpringBoot.Models;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Email(message = "Invalid Email")
    @NotEmpty(message = "Email is missing")
    private String email;

    @NotEmpty(message = "Password is missing")
    private String password;
    @NotEmpty(message = "First Name is missing")
    private String firstname;
    @NotEmpty(message = "Last name is missing")
    private String lastname;

    private String role;

    private String photo;
    
    @Min(value = 18)
    @Max(value = 100)
    private int age;

    private String gender;

    private String passwordResetToken;

    private LocalDateTime password_reset_token_expiry;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;

    @OneToMany(mappedBy = "account")
    private List<Post> posts;
    @ManyToMany
    @JoinTable(
        name= "account_authority",
        joinColumns = {
            @JoinColumn(name ="account_id",referencedColumnName = "id" )
        },
        inverseJoinColumns = {
            @JoinColumn(name = "authority_id",referencedColumnName = "id")
        }
    )
    private Set<Authority> authorities = new HashSet<>();
}

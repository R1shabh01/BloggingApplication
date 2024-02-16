package com.Spring.SpringBoot.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.Spring.SpringBoot.Constants.AppUtil;
import com.Spring.SpringBoot.Constants.EmailDetails;
import com.Spring.SpringBoot.Models.Account;
import com.Spring.SpringBoot.Services.AccountService;
import com.Spring.SpringBoot.Services.EmailService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;





@Controller
public class AccountController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private AccountService accountService;
    //get is used to read from the server
    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account",account);
        return "register";
    }
    //post is used to create in the server
    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        accountService.save(account);
        return "redirect:/";
    }
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model , Principal principal ) {
        String authUser = "email";
        if(principal != null){
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account",account);
            model.addAttribute("photo", account.getPhoto());
            return "profile";
        }else{
            return "/?error";
        }
    }
    @PostMapping("/profile")
    public String post_profile(@Valid @ModelAttribute Account account , BindingResult bindingResult ,Principal principal) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Account account_by_id = accountService.findById(account.getId()).get();
            account_by_id.setAge(account.getAge());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            account_by_id.setFirstname(account.getFirstname());
            account_by_id.setLastname(account.getLastname());
            account_by_id.setGender(account.getGender());
            account_by_id.setPassword(account.getPassword());

            //to logout after setting new details
            SecurityContextHolder.clearContext();
            accountService.save(account_by_id);
            return "redirect:/";

        }else{
            return "redirect:/?error";
        }
        
    }
    @PostMapping("/update_photo")
    public String update_photo(@RequestParam("file") MultipartFile file , RedirectAttributes attributes , Principal principal) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error","No file uploaded");
            return "redirect:/profile";
        }else{
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                int lenght = 10;
                boolean useNumbers = true;
                boolean useLetters = true;
                String generatedString = RandomStringUtils.random(lenght, useLetters, useNumbers);
                String final_photo_name = generatedString + filename;
                String Absolute_FileLocation = AppUtil.get_upload_path(final_photo_name);
                Path path = Paths.get(Absolute_FileLocation);
                Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);

                String authUser = "email";
                if (principal != null) {
                    authUser = principal.getName();
                }
                Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
                if(optionalAccount.isPresent()){
                    Account account = optionalAccount.get();
                    Account account_by_id = accountService.findById(account.getId()).get();
                    String Relative_Filelocation = "resources//static//uploads" + final_photo_name;
                    account_by_id.setPhoto(Relative_Filelocation);
                    accountService.save(account_by_id);
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
                return "redirect:/profile";

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return "redirect:/profile?error";
    }

    @GetMapping("/forgot_password")
    public String forgot_password(Model model) {
        return "forgot_password";
    }
    @PostMapping("/reset-password")
    public String reset_password(@RequestParam("email") String _email ,RedirectAttributes attributes , Model model) {
        Optional<Account> optionalAccount = accountService.findOneByEmail(_email);
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            String reset_token = UUID.randomUUID().toString();
            account.setPasswordResetToken(reset_token);
            account.setPassword_reset_token_expiry(LocalDateTime.now().plusMinutes(600));
            accountService.save(account);
            String reset_message = "This is the reset password link : http://localhost:8080/change-password?token="+reset_token;
            EmailDetails emailDetails = new EmailDetails(account.getEmail(),reset_message,"reset password mail");
            emailService.sendSimpleMail(emailDetails);
            attributes.addFlashAttribute("message","Password reset email sent");
            return "redirect:/login";

        }else{
            attributes.addFlashAttribute("error","No User found with this email supplied");
            return "redirect:/forgot_password";
        }
    }
    @GetMapping("/change-password")
    public String change_password(Model model , @RequestParam("token") String token , RedirectAttributes attributes) {
        Optional<Account> optionalAccount = accountService.findByToken(token);
        if (optionalAccount.isPresent()){
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(optionalAccount.get().getPassword_reset_token_expiry())){
                attributes.addFlashAttribute("error","Token Expired");
                return "redirect:/forgot_password";  
            }
            model.addAttribute("account", account);
            return "change_password";
        }
        attributes.addFlashAttribute("error","Invalid Token");
        return "redirect:/forgot_password";
    }
    @PostMapping("/change-password")
    public String post_change_password(@ModelAttribute Account account , RedirectAttributes attributes) {
        Account account_by_id = accountService.findById(account.getId()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setPasswordResetToken("");
        accountService.save(account_by_id);
        attributes.addFlashAttribute("message","Password Updated");
        return "redirect:/login";
    }
    
    
    
    
    
    
    
    
}

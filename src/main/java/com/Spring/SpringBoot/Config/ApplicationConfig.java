package com.Spring.SpringBoot.Config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class ApplicationConfig {
    
    @Value("${mail.transport.protocol}")
    private String M_T_P;
    @Value("${spring.mail.host}")
    private String S_M_H;
    @Value("${spring.mail.port}")
    private String S_M_P;
    @Value("${spring.mail.username}")
    private String S_M_U;
    @Value("${spring.mail.password}")
    private String S_M_Pa;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String S_M_P_M_S_A;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String S_M_P_M_S_S_E;
    @Value("${spring.mail.smtp.ssl.trust}")
    private String S_M_S_S_T;

    @Bean
    public JavaMailSender gtJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(S_M_H);
        mailSender.setPort(Integer.parseInt(S_M_P));

        mailSender.setUsername(S_M_U);
        mailSender.setPassword(S_M_Pa);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", S_M_P_M_S_A);
        props.put("mail.debug", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.ssl.trust", S_M_S_S_T);
        props.put("mail.smtp.starttls.enable", S_M_P_M_S_S_E);
        
        return mailSender;
    }
}

package com.example.challengefinal.growshop.configurations;


import ch.qos.logback.core.joran.spi.ElementSelector;
import com.example.challengefinal.growshop.Repositorios.ClienteRepositorio;
import com.example.challengefinal.growshop.models.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.ExecutionException;

@Configuration
public class AutenticaciónWeb extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClienteRepositorio clienteRepositorio;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputEmail -> {

            Cliente cliente = clienteRepositorio.findByEmail(inputEmail);

            if (cliente != null) {
                if (cliente.getEmail().contains("-gozogrowshop")) {
                    return new User(cliente.getEmail(), cliente.getContraseña(), AuthorityUtils.createAuthorityList("ADMIN"));
                } else {

                    return new User(cliente.getEmail(), cliente.getContraseña(), AuthorityUtils.createAuthorityList("CLIENTE"));

                }
            } else {

                throw new UsernameNotFoundException("Usuario desconocido" + inputEmail);
            }

        });
    }
}

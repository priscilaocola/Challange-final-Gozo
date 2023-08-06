package com.example.challengefinal.growshop.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Configuration
@EnableWebSecurity
public class AutorizaciónWeb {


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/api/login", "/assets/pages/**", "/assets/script/**", "/assets/images/**","/assets/style/**" , "/api/productos").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login","/api/clientes").permitAll()
                //ADMIN
        .antMatchers("/h2-console/**","/rest/**","/api/clientes", "/manager.html" , "/api/pagos", "/api/ordenes" ).hasAuthority("ADMIN")
               .antMatchers(HttpMethod.POST, "/api/productos/agregar").hasAuthority("ADMIN")
                .antMatchers( HttpMethod.PATCH, "/api/productos/{id}/deactivate").hasAuthority("ADMIN")
                .antMatchers( HttpMethod.POST, "/api/productos/modificar").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.GET,"/api/cliente/actual").hasAnyAuthority("CLIENTE", "ADMIN")
                //CLIENTES

                .antMatchers(HttpMethod.PUT, "/cliente/actual/editar").hasAuthority("CLIENTE")
                .antMatchers(HttpMethod.POST, "/api/ordenes/crear-orden").hasAuthority("CLIENTE");







        //DAR AUTORIZACION

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("contraseña")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout").deleteCookies("JSSESIONID");

        //un tipo de seguridad que desabhilitamos porque vamos a utilizar tokens
        http.csrf().disable();

        http.cors(); //habilitar el cors en spring security

        //deshabilitar frameOptions para que se pueda acceder a h2-console
        http.headers().frameOptions().disable();

        // la autorizacion que tiene no es suficiente para, acceder a la ruta que esta tratando de ingresar
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // inicio de session exitoso, borramos las flags de la autenticacion
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // respuesta de error al fallo de inicio, 401
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // logout exitoso, 200
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }

}

package vde.dev.garage.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vde.dev.garage.filter.JwtFilter;
import vde.dev.garage.service.CustomerUserDetailsService;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomerUserDetailsService customerUserDetailsService;
    private final JwtUtils jwtUtils;

//    @Bean
//    public JwtUtils jwtUtils(@Lazy SecurityConfig securityConfig) {
//        return new JwtUtils();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedOrigin("http://localhost:3000"); // autorise Angular
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE, etc.
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // car on utilise des cookies ou Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /* 1- Version Non securisée de l'application
       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
           http
                   .csrf(csrf -> csrf.disable()) // Désactiver CSRF
                   .cors(cors -> cors.configurationSource(corsConfigurationSource))
                   .authorizeHttpRequests(auth -> auth
                           .anyRequest().permitAll() // Autoriser toutes les requêtes
                   );

           return http.build();
       }

   */

   // 2- Securisé : Activation/Desactivation de la securité temporairement


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http

     /* 2.1 Sans Permissions
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/garage/register", "/garage/login", "/garage/refresh-token").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider()) // Utilise notre `authenticationProvider`
                .addFilterBefore(new JwtFilter(customerUserDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
*/



                // 2.2 Avec permissions
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/garage/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/garage/read").hasAnyRole("USER", "ADMIN","MANAGER")
                        .requestMatchers(HttpMethod.GET,"/garage/update").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/garage/delete").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,"/garage/register", "/garage/login","/garage/refresh-token").permitAll()

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JwtFilter(customerUserDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}




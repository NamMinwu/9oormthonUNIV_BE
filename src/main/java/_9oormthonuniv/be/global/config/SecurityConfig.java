package _9oormthonuniv.be.global.config;

import _9oormthonuniv.be.domain.user.repository.UserRepository;
import _9oormthonuniv.be.global.security.jwt.JWTAuthenticationFilter;
import _9oormthonuniv.be.global.security.jwt.JWTTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


  private final JWTTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  public SecurityConfig(
      JWTTokenProvider jwtTokenProvider, UserRepository userRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userRepository = userRepository;

  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

          @Override
          public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(Collections.singletonList("Authorization"));
            return configuration;
          }
        })));

    http
        .csrf(AbstractHttpConfigurer::disable);

    //From 로그인 방식 disable
    http
        .formLogin(AbstractHttpConfigurer::disable);

    //http basic 인증 방식 disable
    http
        .httpBasic(AbstractHttpConfigurer::disable);

    //경로별 인가 작업
    http
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/", "/v3/api-docs/**",
                "/api/v1/auth/login",
                "/api/v1/auth/token/refresh",
                "/api/v1/users/**",
                "/api/v1/posts/**",
                "/api/v1/test/**",
                "/swagger-ui/**", "/swagger-ui.html").permitAll() // 회원가입과 관련된 모든 요청 허용
            .requestMatchers("/admin").hasRole("ADMIN") // /admin 경로는 ADMIN만
            .anyRequest().authenticated() // 그 외 나머지 요청은 인증 필요
        );

    http.addFilterBefore(
        new JWTAuthenticationFilter(userRepository, jwtTokenProvider),
        UsernamePasswordAuthenticationFilter.class
    );

    //세션 설정
    http
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }


}

package com.mechanic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ══════════════════════════════════════════════════════
 *  فلتر JWT — يتحقق من التوكن في كل طلب
 * ══════════════════════════════════════════════════════
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // استخراج الـ Header
        final String authHeader = request.getHeader("Authorization");

        // إذا لم يوجد توكن أو الصيغة غير صحيحة
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // استخراج التوكن
        final String jwt = authHeader.substring(7);
        final String email = jwtTokenProvider.extractEmail(jwt);

        // التحقق وإضافة المستخدم للـ SecurityContext
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}


/**
 * ══════════════════════════════════════════════════════
 *  إعداد Spring Security
 * ══════════════════════════════════════════════════════
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // تعطيل CSRF (غير مطلوب مع JWT)
                .csrf(AbstractHttpConfigurer::disable)

                // إعداد الصلاحيات
                .authorizeHttpRequests(auth -> auth

                        // مسارات مفتوحة للجميع
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // عرض البيانات لأي مستخدم مسجّل
                        .requestMatchers(HttpMethod.GET, "/cars/**").hasAnyRole("ADMIN", "TECHNICIAN", "VIEWER")
                        .requestMatchers(HttpMethod.GET, "/dtc/**").hasAnyRole("ADMIN", "TECHNICIAN", "VIEWER")

                        // التشخيص للتقنيين والمشرفين
                        .requestMatchers("/diagnose/**").hasAnyRole("ADMIN", "TECHNICIAN")

                        // الإدارة للمشرف فقط
                        .requestMatchers(HttpMethod.POST, "/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/cars/**").hasRole("ADMIN")

                        // كل الباقي يحتاج مصادقة
                        .anyRequest().authenticated()
                )

                // بدون Session (Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // إضافة فلتر JWT قبل فلتر المصادقة الافتراضي
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

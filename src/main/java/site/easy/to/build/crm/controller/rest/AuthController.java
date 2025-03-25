package site.easy.to.build.crm.controller.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import site.easy.to.build.crm.model.dto.LoginRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(
        "ZmY3MzY1N2YtNDQyZi00ODFiLTg3YjktMTQ4NzRjMjA2ZDZkZmY3MzY1N2YtNDQyZi00ODFiLTg3YjktMTQ4NzRjMjA2ZDZk"
    ));
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    private static final long EXPIRATION_TIME = 86400000; 

    @PostMapping("/mylogin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Récupération des rôles de l'utilisateur
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Génération du token JWT
            String token = generateJwtToken(authentication);

            // Réponse JSON avec le token et les rôles
            return ResponseEntity.ok(new AuthResponse(token, roles));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Erreur : Identifiants incorrects !");
        }
    }

    private String generateJwtToken(Authentication authentication) {
        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim("roles", authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
            )
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Vérifie que EXPIRATION_TIME est bien défini
            .signWith(SECRET_KEY) // ✅ Pas besoin de getBytes()
            .compact();
    }
    

    // Classe pour la réponse JSON
    private static class AuthResponse {
        private final String token;
        private final List<String> roles;

        public AuthResponse(String token, List<String> roles) {
            this.token = token;
            this.roles = roles;
        }

        public String getToken() {
            return token;
        }

        public List<String> getRoles() {
            return roles;
        }
    }
}

package vde.dev.garage.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
@Service
public class JwtService {
    // Clé secrète utilisée pour signer le token (en pratique, mets ça dans un fichier de config)
    private  final Key secretKey= Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Durée de validité du token (ex: 24 heures)
    private final long jwtExpirationMs = 24 * 60 * 60 * 1000;

    public String generateToken( Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // identifiant de l’utilisateur (souvent l'email ou le username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }
}


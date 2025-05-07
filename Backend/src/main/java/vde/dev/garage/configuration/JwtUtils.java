    package vde.dev.garage.configuration;
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jws;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;

    import javax.crypto.spec.SecretKeySpec;
    import java.security.Key;
    import java.util.Collection;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.function.Function;
    import java.util.stream.Collectors;

    @Component
    public class JwtUtils {

     @Value("${app.secret-key}")
        private String secretKey;

     @Value("${app.expiration-time}")
        private Long expirationTime;


     public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
         Map<String, Object> claims = new HashMap<>();
         claims.put("roles", authorities.stream()
                 .map(GrantedAuthority::getAuthority)
                 .collect(Collectors.toList()));

         return Jwts.builder()
                 .setClaims(claims)
                 .setSubject(username)
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                 .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                 .compact();
     }

        public String generateRefreshToken(String username) {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 jours
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                    .compact();
        }


        private Key getSignKey() {
         byte[] keyBytes = secretKey.getBytes();
         return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        }
        public boolean validateToken(String token, UserDetails userDetails) {
         String username=extractUsername(token);
         return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

        }

        public boolean isTokenExpired(String token) {
         return extractExpirationDate(token).before(new Date());
        }

        private Date extractExpirationDate(String token) {
         return extractClaims(token, Claims::getExpiration);
        }

        public String extractUsername(String token) {
         return extractClaims(token, Claims::getSubject);
        }

        private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
          final Claims claims=extractAllClaims(token);
          return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSignKey()) // Remplace setSigningKey()
                    .build() // Nouvelle méthode obligatoire
                    .parseClaimsJws(token); // Analyse du token
            return claimsJws.getBody();
        }

    }

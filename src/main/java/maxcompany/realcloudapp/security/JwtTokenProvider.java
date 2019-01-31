package maxcompany.realcloudapp.security;

import io.jsonwebtoken.*;
import maxcompany.realcloudapp.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static maxcompany.realcloudapp.security.SecurityConstants.EXPIRATION_TIME;
import static maxcompany.realcloudapp.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    // Generate rhe token

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Date current_time = new Date(System.currentTimeMillis());

        Date expiration_date = new Date(current_time.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String,Object> claimsMap = new HashMap<>();
        claimsMap.put("id",userId);
        claimsMap.put("username",user.getUsername());
        claimsMap.put("fullName",user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claimsMap)
                .setIssuedAt(current_time)
                .setExpiration(expiration_date)
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .compact();
    }


    // Validate the token

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            System.out.println("INVALID JWT SIGNATURE");
        }catch (MalformedJwtException ex){
            System.out.println("INVALID JWT TOKEN");
        }catch (ExpiredJwtException ex){
            System.out.println("EXPIRED JWT TOKEN");
        }catch (UnsupportedJwtException ex){
            System.out.println("UNSUPPORTED JWT TOKEN");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT CLAIMS STRING IS EMPTY");
        }
        return false;
    }

    // Get userId from token
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");

        return Long.parseLong(id);
    }
}

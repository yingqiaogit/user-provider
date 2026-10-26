package com.example.user_management.utils;

import java.security.SecureRandom;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

public class JwtUtil {
    private static final String SECRET_KEY;

    static {
        var randomSecure = new SecureRandom();
        var randomBytes = new byte[32];
        randomSecure.nextBytes(randomBytes);
        SECRET_KEY = new String(randomBytes);
    }

    public static String generateToken(String role) {
        // Implement token generation logic here        
        // In a real implementation, you would use a JWT library to create the token
        // For example: return Jwts.builder().setClaims(claim).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        return JWT.create()
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // decode a token and verify its signature
    public static Claim decodeRole(String token) {
        // Implement token decoding and verification logic here
        // In a real implementation, you would use a JWT library to decode and verify the token
        // For example: return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        var decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
        return decodedJWT.getClaim("role");
    }    
}

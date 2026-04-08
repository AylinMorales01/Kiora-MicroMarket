package com.kiora.micromarket.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    String secretKey;

    @Value("${security.jwt.token-expiration}")
    Long tokenExpiration;

    private SecretKey getSignKey() {
        byte[] keyBites = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBites);
    }

    /**
     * Generación del token.
     * Es el pasaporte digital de cada usuario
     *
     * @param userId
     * @param Cedula
     * @param rolId
     * @return
     */

    public String generateToken(Long userId, String Cedula) {
        return Jwts.builder()
                .claims(Map.of("userId", userId)) // Datos personalizados (Payload)
                .subject(Cedula) // Identificador del token, lo que define al usuario (quién es el usuario?)
                .issuedAt(new Date()) // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration)) // Vencimiento
                .signWith(getSignKey()) // Firma digital de seguridad
                .compact(); // se acaba la construccion y lo convierte en String al final.
                            //lo que maik nos dijo, lo que compone un jwt, header-payload-signature
    }

    /**
     * Verifica si el token es auténtico y si aún está vigente.
     *
     * @param token
     * @return
     */

    public Boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignKey()) //configura el lector para que se use la misma clave con la que se firmo
                    .build()//abre y lee el token, si no es valido lanza una excepcion
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException e) {
            e.printStackTrace(); //retorna false si algo salio mal con  el token
            return false;
        }
    }

    /**
     * Motor de extracción
     * Permite obtener cualquier dato (claim)-la informacion que transmite el token
     * Esto falla si el token está expirado
     *
     * @param <T>
     * @param token
     * @param resolver
     * @return
     */

    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    /**
     * Extrae el username osea Cedula del token
     *
     * @param Cedula
     * @return
     */
    public String extractCedula(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extrae el userId del token
     *
     * @param token
     * @return
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extrae el rolId del token
     *
     * @param token
     * @return
     */
    public Long extractRolId(String token) {
        return extractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    /**
     * Genera un nuevo token con la informacion del usuario del token anterior
     * siempre y cuando la firma sea correcta
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new RuntimeException("Token expirado");
            }

            return generateToken(
                    claims.get("userId", Long.class),
                    claims.getSubject());

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado");
        } catch (JwtException e) {
            throw new RuntimeException("Token inválido");
        }
    }
}

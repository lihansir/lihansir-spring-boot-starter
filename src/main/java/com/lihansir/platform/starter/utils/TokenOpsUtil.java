/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.utils;

import cn.hutool.core.map.MapUtil;
import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token operation tool class
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
public class TokenOpsUtil {

    /**
     * jwt secret
     */
    @Value("${cloud.jwt.secret:lihansircloudplatformaaaaaaaaaaaabbbbbbbbbbbbbb}")
    private String secret;

    /**
     * Validity period (two weeks by default) in seconds
     */
    @Value("${cloud.jwt.expirationTimeInSecond:1209600}")
    private Long expirationTimeInSecond;

    /**
     * Get data from token string
     *
     * @param token
     *            Token string
     * @return Data map
     */
    public Map<String, Object> getDataFromToken(String token) {
        Boolean tokenExpired = isTokenExpired(token);
        if (tokenExpired) {
            throw new BusinessException(CommonCode.ERROR_TOKEN.getCode(), "Token is expired");
        }
        Claims claimsFromToken = getClaimsFromToken(token);
        if (claimsFromToken == null) {
            return MapUtil.empty();
        }
        Map<String, Object> data = new HashMap<>();
        for (String key : claimsFromToken.keySet()) {
            data.put(key, claimsFromToken.get(key));
        }
        return data;
    }

    /**
     * Generate token string by data
     *
     * @param claims
     *            Information to be added
     * @return Token string containing information
     */
    public String generateToken(Map<String, Object> claims) {
        Date createdTime = new Date();
        Date expirationTime = this.getExpirationTime();
        byte[] keyBytes = this.secret.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder().setClaims(claims).setIssuedAt(createdTime).setExpiration(expirationTime)
            // You can also switch to your favorite algorithm
            // The supported algorithms are shown in：
            // https://github.com/jwtk/jjwt#features
            .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Verify whether it is expired
     *
     * @param token
     *            Token string
     * @return true if token is expired
     */
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration == null) {
            return true;
        }
        return expiration.before(new Date());
    }

    /**
     * Calculate token expiration time
     *
     * @return expiration time
     */
    private Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + this.expirationTimeInSecond * 1000);
    }

    /**
     * Get expiration date from token string
     *
     * @param token
     *            Token string
     * @return Expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        return claims.getExpiration();
    }

    /**
     * Get Claims from the token string
     *
     * @param token
     *            Token string
     * @return Claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(this.secret.getBytes()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpirationTimeInSecond() {
        return expirationTimeInSecond;
    }

    public void setExpirationTimeInSecond(Long expirationTimeInSecond) {
        this.expirationTimeInSecond = expirationTimeInSecond;
    }
}

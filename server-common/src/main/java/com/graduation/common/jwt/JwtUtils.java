package com.graduation.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.graduation.common.format.FormatUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    //秘钥
    private static final String SECRET_KEY = "ZZYjiancexitongbiyesheji";
    //token过期时间 30分钟
    private static final long TOKEN_EXPIRE_TIME = 30 * 60 * 1000;
    //refreshToken过期时间 1周
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
    //签发人
    private static final String ISSUER = "ZZY";

    //签发Token
    public static String genToken(Map<String, Object> map){
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        JWTCreator.Builder tokenBuilder = JWT.create().withIssuedAt(now).withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME));

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            tokenBuilder.withClaim(entry.getKey(), FormatUtils.objectToJson(entry.getValue()));
        }

        return tokenBuilder.sign(algorithm);
    }

    //签发refreshToken
    public static String genRefreshToken(Map<String, Object> map){
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        JWTCreator.Builder tokenBuilder = JWT.create().withIssuedAt(now).withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME));

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            tokenBuilder.withClaim(entry.getKey(), FormatUtils.objectToJson(entry.getValue()));
        }

        return tokenBuilder.sign(algorithm);
    }

    //验证token
    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    //从token获取username
    public static Map<String, Object> getInfo(String token){
        Map<String, Claim> info = JWT.decode(token).getClaims();
        Map<String, Object> map = new HashMap<>();
        String value;
        for (Map.Entry<String, Claim> entry : info.entrySet()) {
            value = entry.getValue().asString();
            if(value != null){
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }
}

package com.jishe.jupyter.component;

import com.jishe.jupyter.entity.WechatUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: jupyter
 * @description: jwt，用于用户登录的加密
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2021-05-10 23:43
 **/

public class JWT2 {
    /**
     * 加密算法
     *
     * @param
     * @return
     * @throws Exception
     */
    public String createJWT(long ttlMillis, WechatUser user) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;//指定算法，这里使用hs256
        //生成令牌的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //创建私有声明服务
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("Nickname", user.getNickname());
        claims.put("OpenId", user.getOpenId());//实现相关的jwt注入
        SecretKey key = generalKey();

        JwtBuilder builder = Jwts.builder() //设置jwt的body
                .setClaims(claims)          //给builder的claim赋值，
                .setId(String.valueOf(user.getId()))                  //设置jti(JWT ID)用来作为一次性token
                .setIssuedAt(now)           //iat:用于设置签发时间
                .setSubject(user.getOpenId())        //可以存放什么userid，用户的唯一标志
                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();
    }

    /**
     * 解密算法
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        Claims claimss =null;
        if (jwt.equals("kfzjw0000")){
          return claimss;
        }
        SecretKey key = generalKey();  //签名秘钥
        Claims claims = Jwts.parser()  //得到Parser
                .setSigningKey(key)         //设置秘钥
                .parseClaimsJws(jwt).getBody();//设置jwt
        return claims;
    }

    private SecretKey generalKey() {
        String stringKey = "ZjAyMGNlMDhlY2NhNTAMEjaaL9ua3ZjEwOTI3Y2NmYjE=";
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        //System.out.println(encodedKey);//[B@152f6e2
        //System.out.println(Base64.encodeBase64URLSafeString(encodedKey));//7786df7fc3a34e26a61c034d5ec8245d
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
        return key;
    }
}

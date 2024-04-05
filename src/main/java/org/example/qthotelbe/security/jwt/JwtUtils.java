package org.example.qthotelbe.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public class JwtUtils {
    private  static  final Logger logger=  LoggerFactory.getLogger(JwtUtils.class); // cai nay dung de ghi log thong tin


    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expirationInMs}")
    private int jwtExpirationTime;

}

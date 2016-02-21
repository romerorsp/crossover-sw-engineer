package com.crossover.test.exam.maker;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.crossover.test.exam.maker.business.UserBusiness;

public final class CryptoUtil {
	private CryptoUtil(){}
	
	private static final byte[] SALT = new byte[]{-16, 105, 110, 117, -113, -127, -62, -105, 15, 80, 112, -60, -78, -49, 94, 124};
	private static final String SALT_BASE64 = Base64.getEncoder().encodeToString(SALT);
	public static final String ISSUER = "crossover.com";
	
	public static String hash(String pwd) {
		try {
			KeySpec spec = new PBEKeySpec(pwd.toCharArray(), SALT, 65536, 128);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = f.generateSecret(spec).getEncoded();
			Base64.Encoder enc = Base64.getEncoder();
			return enc.encodeToString(hash);
		} catch(Exception e) {
			Logger.getLogger(CryptoUtil.class.getName()).log(Level.SEVERE, "Back to the lab man!");
			throw new RuntimeException(e);
		}
	}
	
	public static String createJWT(String id, String issuer, String subject, long ttlMillis) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SALT_BASE64);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder()
								 .setId(id)
								 .setIssuedAt(now)
								 .setSubject(subject)
								 .setIssuer(issuer)
								 .signWith(signatureAlgorithm, signingKey);
		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    builder.setExpiration(exp);
		}
		return builder.compact();
	}

	public static boolean validateJWT(String token, UserBusiness userBusiness) {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SALT_BASE64)).parseClaimsJws(token).getBody();
		if(claims.getExpiration().getTime() <= System.currentTimeMillis() ||
		  !claims.getIssuer().equals(ISSUER) ||
		  !userBusiness.validateUserJWT(claims.getId(), claims.getSubject())) {
			return false;
		}
		return true;
	}
}

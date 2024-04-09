package br.com.management.server.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import br.com.management.server.data.vo.security.TokenVO;
import br.com.management.server.exception.InvalidJWTAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTTokenProvider {
		
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt.token.expire-length:expireLength}")
	private long expireLength = 3600000L;
	
	@Autowired
	private UserDetailsService detailsService;
	
	Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}	
	
	public TokenVO createAccessToken(String username, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expireLength);
		
		var accessToken = getAcessToken(username, roles, now, validity);
		var refreshToken = getRefreshToken(username, roles, now);
		
		return new TokenVO(username, true, now, validity, accessToken, refreshToken);
	}

	private String getRefreshToken(String username, List<String> roles, Date now) {
		Date validityRefreshToken = new Date(now.getTime() + (expireLength * 3));
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validityRefreshToken)
				.withSubject(username)
				.sign(algorithm);
	}

	private String getAcessToken(String username, List<String> roles, Date now, Date validity) {
		String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withIssuer(issuerUrl)
				.withSubject(username)
				.sign(algorithm);
	}
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);		
		UserDetails userDetails = this.detailsService.loadUserByUsername(decodedJWT.getSubject());
		
		return new UsernamePasswordAuthenticationToken(detailsService, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
		
		JWTVerifier jwtVerifier = JWT.require(alg).build();
		
		return jwtVerifier.verify(token);
	}
	
	public String resolveToken(HttpServletRequest req) {
		String bToken = req.getHeader("Authorization");
	
		// Bearer eyJ0e...
		if(bToken != null && bToken.startsWith("Bearer ")) {
			return bToken.substring("Bearer ".length());
		}
		
		return null;
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decodedToken = decodedToken(token);
		
		try {
			if(decodedToken.getExpiresAt().before(new Date())) {
				return false;
			}
				
			return true;
		} catch (Exception e) {
			throw new InvalidJWTAuthenticationException("Expired or invalid JWT Token");
		}		
	}
	
}


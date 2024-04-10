package br.com.management.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.management.server.core.FieldValidator;
import br.com.management.server.data.vo.security.AccountCredentialsVO;
import br.com.management.server.data.vo.security.TokenVO;
import br.com.management.server.model.User;
import br.com.management.server.repositories.UserRepository;
import br.com.management.server.security.jwt.JWTTokenProvider;

@Service
public class AuthService {

	@Autowired
	private JWTTokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository repository;

	public TokenVO signin(AccountCredentialsVO credentialsVO) throws Exception {
		FieldValidator.check(new String[] { "username", "password" }, credentialsVO);

		try {
			String username = credentialsVO.getUsername();
			String password = credentialsVO.getPassword();

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			User user = repository.findByUsername(username);
			TokenVO tokenResponse = new TokenVO();

			if (user != null) {
				tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username " + username + " not found");
			}

			return tokenResponse;

		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password supplied");
		}
	}

	public TokenVO refreshToken(String username, String refreshToken) throws Exception {		
		User user = repository.findByUsername(username);
		TokenVO tokenResponse = new TokenVO();

		if (user != null) {
			tokenResponse = tokenProvider.createRefreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found");
		}

		return tokenResponse;
	}

}

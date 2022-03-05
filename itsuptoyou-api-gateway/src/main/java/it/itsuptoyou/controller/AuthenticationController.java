package it.itsuptoyou.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.itsuptoyou.security.JwtAuthenticationTokenRequest;
import it.itsuptoyou.security.JwtTokenUtil;
import it.itsuptoyou.security.jwt.JwtAuthenticationResponse;

@RestController
public class AuthenticationController {
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@RequestMapping(value = "public/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody JwtAuthenticationTokenRequest authRequest, HttpServletResponse response)
					throws AuthenticationException, JsonProcessingException{
		
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authRequest.getUsername(),
							authRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
				
		
		final UserDetails userDetails =
				userDetailsService.loadUserByUsername(authRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		System.out.println(token);
		response.setHeader(tokenHeader, token);
		return ResponseEntity.ok(
				new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities(), token));
		
	}
	
	@RequestMapping(value = "protected/refresh-token", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(tokenHeader);
        UserDetails userDetails =
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            response.setHeader(tokenHeader,refreshedToken);

            return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getUsername(),userDetails.getAuthorities(), refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}



package it.itsuptoyou.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import it.itsuptoyou.collection.Account;
import it.itsuptoyou.collection.Authority;
import it.itsuptoyou.security.jwt.JwtUser;

public class JwtUserFactory {

	private JwtUserFactory() {
		
	}
	
	public static JwtUser create(Account user) {
		return new JwtUser(user.getUsername(),
				user.getPassword(),
				mapToGrantedAuthorities(user.getAuthorities()),
				user.isEnabled()
		);
	}
	
	private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities){
		return authorities.stream()
				.map(authority -> 
				new SimpleGrantedAuthority(authority.getName().name()))
				.collect(Collectors.toList());
	}
}
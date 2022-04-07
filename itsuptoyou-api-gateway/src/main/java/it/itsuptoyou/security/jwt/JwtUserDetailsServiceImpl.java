package it.itsuptoyou.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.itsuptoyou.collection.Account;
import it.itsuptoyou.repository.AccountRepository;
import it.itsuptoyou.security.JwtUserFactory;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		Account user = accountRepository.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("No user found with username: " + username);
		}else {
			return JwtUserFactory.create(user);
		}
	}
}

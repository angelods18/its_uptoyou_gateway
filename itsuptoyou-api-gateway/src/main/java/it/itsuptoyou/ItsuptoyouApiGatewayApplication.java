package it.itsuptoyou;

import java.util.Arrays;
import java.util.List;

import javax.activation.DataSource;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.itsuptoyou.collection.Account;
import it.itsuptoyou.collection.Authority;
import it.itsuptoyou.enums.AuthorityName;
import it.itsuptoyou.repository.AccountRepository;
import it.itsuptoyou.repository.AuthorityRepository;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableZuulProxy
public class ItsuptoyouApiGatewayApplication {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ItsuptoyouApiGatewayApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(AccountRepository userRepository, AuthorityRepository authorityRepository) {
		return (args) -> {

			Account user=userRepository.findByUsername("admin");

			if(user == null){

				/**
				 * Inizializzo i dati del mio test
				 */


				Authority authorityAdmin=new Authority();
				authorityAdmin.setId(1L);
				authorityAdmin.setName(AuthorityName.ROLE_ADMIN);
				authorityAdmin=authorityRepository.save(authorityAdmin);

				Authority authorityUser=new Authority();
				authorityUser.setId(2L);
				authorityUser.setName(AuthorityName.ROLE_USER);
				authorityUser=authorityRepository.save(authorityUser);


				List<Authority> authorities = Arrays.asList(new Authority[] {authorityAdmin,authorityUser});


				user = new Account();
				user.setAuthorities(authorities);
				user.setEnabled(true);
				user.setUsername("admin");
				user.setPassword(passwordEncoder.encode("admin"));

				user = userRepository.save(user);

			}
		};
	}
}

package it.itsuptoyou.collection;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="users")
public class Account {

	@Indexed(unique=true)
	private String userId;
	
	@CreatedDate
	private LocalDateTime creationDate;
	
	@LastModifiedDate 
	private LocalDateTime lastUpdateDate;
	
	private boolean enabled = true;
	
	@Indexed(unique=true)
	private String username;
	private String password;
		
	private List<Authority> authorities;
	
	
}

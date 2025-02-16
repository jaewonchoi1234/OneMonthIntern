package com.intern.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.intern.assignment.entity.Authority;
import com.intern.assignment.entity.User;
import com.intern.assignment.repository.AuthorityRepository;
import com.intern.assignment.repository.UserRepository;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	
	@Test
	public void userSave() {
		//given
		String username = "TEST";
		String password = "test1234!";
		String nickname = "TEST";

		Authority authority = new Authority("ROLE_USER");
		authority=authorityRepository.save(authority);
		
		Set<Authority> authorities = new HashSet<>();
		authorities.add(authority);
		
		User user = new User(1L,username,password,nickname,authorities);

		//when
		User saveUser = userRepository.save(user);
		
		//then
		assertEquals(saveUser.getUsername(), user.getUsername());
		assertEquals(saveUser.getNickname(), user.getNickname());
		assertEquals(saveUser.getPassword(), user.getPassword());
	    for (Authority a : user.getAuthorities()) {
	    	assertTrue(saveUser.getAuthorities().contains(a));        
	    }
	}
	
	@Test
	public void authoritySave() {
		//given
		Authority authority =new Authority(1L,"ROLE_USER");
		//when
		Authority saveAuthority = authorityRepository.save(authority);
		//then
		assertEquals(saveAuthority.getId(),authority.getId());
		assertEquals(saveAuthority.getAuthorityName(),authority.getAuthorityName());
	}
	
	@Test
	public void findByAuthorityName() {
		//given
		String authorityName = "ROLE_USER";
		//when
		Authority saveAuthority = authorityRepository.findByAuthorityName(authorityName).orElse(null);
		//then
		if(saveAuthority != null) {
			assertEquals(saveAuthority.getAuthorityName(),authorityName);
		}
	}
	
	@Test
	public void findByUsername() {
		//given
		String username = "TEST";
		//when
		User user = userRepository.findByUsername(username).orElse(null);
		//then
		if(user != null) {
			assertEquals(user.getUsername(),username);
		}
	
	}
}

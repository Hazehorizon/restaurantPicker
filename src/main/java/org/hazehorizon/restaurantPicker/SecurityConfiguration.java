package org.hazehorizon.restaurantPicker;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hazehorizon.restaurantPicker.model.UserEntity;
import org.hazehorizon.restaurantPicker.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
class SecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	private static Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Autowired
	private UserRepository repository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Optional<UserEntity> user = repository.findByLogin(username);
				if (user.isPresent() && user.get().isActive()) {
					List<GrantedAuthority> authorities = user.get().getRoles().stream()
							.map(r -> new SimpleGrantedAuthority(r.getCode())).collect(Collectors.toList());
					logger.debug("Login successful " + username);
					logger.trace("User " + username + " roles:" + authorities);
					return new User(user.get().getLogin(), user.get().getPasswd(), true, true, true, true, authorities);
				}
				else {
					logger.warn("Unknown user: " + username);
					throw new UsernameNotFoundException("could not find the user '" + username + "'");
				}
			}
		};
	}
}

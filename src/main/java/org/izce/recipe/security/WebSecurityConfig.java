package org.izce.recipe.security;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
 	@Override
 	public void configure(WebSecurity web) throws Exception {
 		// Spring Security should completely ignore URLs starting with /resources/
 		web.ignoring().antMatchers("/index", "/surprise", "/resources/**", "/images/**", "/webjars/**", "/css/**", "/js/**");
 	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/recipe/**").authenticated()
			.and()
		.formLogin().loginPage("/login").permitAll()
			.and()
		.logout().permitAll()
			.and()
		.requiresChannel().anyRequest().requiresSecure();
		
		
		// The following part should be used as workaround to prevent 8080 to 8443 redirect. 
//		var portMapper = new PortMapperImpl();
//		portMapper.setPortMappings(Map.of("80", "443", "8080", "8080"));
//		var portResolver = new PortResolverImpl();
//		portResolver.setPortMapper(portMapper);
//		var entryPoint = new LoginUrlAuthenticationEntryPoint("/login");
//		entryPoint.setForceHttps(true);
//		entryPoint.setPortMapper(portMapper);
//		entryPoint.setPortResolver(portResolver);
//
//		http
//			.exceptionHandling().authenticationEntryPoint(entryPoint)
//				.and()
//			.authorizeRequests().antMatchers("/**").hasRole("USER")
//				.and()
//			.formLogin().loginPage("/login").permitAll()
//				.and()
//			.logout().permitAll()
//				.and()
//			.requiresChannel().anyRequest().requiresSecure();
	
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		//UserDetails user = User.withDefaultPasswordEncoder()
		
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encPwd = encoder.encode("password");

		UserDetails admin1 = User
				.withUsername("izzet")
				.password(encPwd)
				.roles("USER")
				.build();
		
		UserDetails user1 = User
				.withUsername("user")
				.password(encPwd)
				.roles("USER")
				.build();
		
		return new InMemoryUserDetailsManager(admin1, user1);
	}
}
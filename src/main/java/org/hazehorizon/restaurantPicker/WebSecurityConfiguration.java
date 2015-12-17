package org.hazehorizon.restaurantPicker;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
 
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/api/v1/settings/**").hasAuthority("ADMIN")
    	.and().authorizeRequests().antMatchers("/api/v1/system/**").hasAuthority("SYSTEM")
/*    	.and().exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
			    response.setContentType("application/json");
			    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage() );
			    response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");				
			}
		})*/
    	.and().httpBasic().and().csrf().disable();
  }
}

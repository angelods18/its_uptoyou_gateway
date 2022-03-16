package it.itsuptoyou.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.netflix.zuul.context.RequestContext;

public class JwtAuthenticationTokenFilter  extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authToken= request.getHeader(this.tokenHeader);
		UserDetails userDetails = null;
		
		if(authToken != null) {
			authToken=jwtTokenUtil.trimToken(authToken);
			userDetails = jwtTokenUtil.getUserDetails(authToken);
			propagateUserIdentity(userDetails.getUsername());
		}
		
		if(userDetails!=null && 
				SecurityContextHolder.getContext().getAuthentication() == null) {
			if(jwtTokenUtil.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}
	
	private void propagateUserIdentity(String username) {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		context.addZuulRequestHeader("username", username);
	}
}
package fr.cephee.unilille;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.userdetails.GrantedAuthorityFromAssertionAttributesUserDetailsService;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
   
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);    
        
    @Value("${cas.url}")
    private String casUrl;
    
    @Value("${server.url}")
    private String serverUrl;
    
    @Value("${server.contextPath}")
    private String serverContextPath;
    
    // URL relative qui gère l'authentification coté application (login, global logout)
    @Value("${server.cas.authentication.url}")
    private String serverCasAuthenticationUrl;
    
    @Value("${server.cas.logout.url}")
    private String serverCasLogoutUrl;
    
    @Autowired
    private AuthenticationUserDetailsService loadUserDetailsService;            
    
    /*******************************************/
    /****** Configuration Spring security ******/
    /*******************************************/
    
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
            SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();                        
            singleSignOutFilter.setCasServerUrlPrefix(casUrl);
            
            singleSignOutFilter.setIgnoreInitConfiguration(true);
            singleSignOutFilter.setArtifactParameterName(Protocol.CAS2.getArtifactParameterName());
            singleSignOutFilter.setFrontLogoutParameterName(casUrl);
            singleSignOutFilter.setLogoutParameterName("logoutRequest");
            singleSignOutFilter.setRelayStateParameterName("RelayState");
            
            return singleSignOutFilter;
    }

    @Bean
    public LogoutFilter requestCasGlobalLogoutFilter() throws UnsupportedEncodingException {
            SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
            //handler.setClearAuthentication(false);
            //handler.setInvalidateHttpSession(false);
            
            LogoutFilter logoutFilter = new LogoutFilter(casUrl + "/logout?service="
                            + URLEncoder.encode(serverUrl + serverContextPath, "UTF-8"), handler);
            logoutFilter.setFilterProcessesUrl(serverCasLogoutUrl);
            logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher(serverCasLogoutUrl, "POST"));
            return logoutFilter;
    }    

    // Définition de l'authentification et des droits d'accès
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(casAuthenticationFilter());
        http.addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);
        http.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);
        
        http.exceptionHandling()
            .authenticationEntryPoint(casAuthenticationEntryPoint());
        
        http.csrf().ignoringAntMatchers(serverCasAuthenticationUrl);
        
        // Spring security prend la première url qui match, l'ordre est important
        http
            .authorizeRequests()
                .antMatchers("/**").authenticated()
            .and()
            .authorizeRequests()
                .anyRequest()
                    .permitAll();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/css/**","/js/**","/fonts/**","/img/**");
    }
    
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(serverUrl + serverContextPath + serverCasAuthenticationUrl);
        serviceProperties.setSendRenew(false);                
        return serviceProperties;
    }    
    
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(loadUserDetailsService);
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("CAS_DEFAULT_PROVIDER");
        return casAuthenticationProvider;
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casUrl);
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setFilterProcessesUrl(serverCasAuthenticationUrl);
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return casAuthenticationFilter;
    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casUrl);
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider());
    }  
}

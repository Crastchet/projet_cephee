package fr.cephee.unilille;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import fr.cephee.unilille.model.Member;


@Service
public class CasUserDetailsService extends AbstractCasAssertionUserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CasUserDetailsService.class);   

    @Value("${attribute.mail}")
    private String mailAttribute;
    
    @Value("${attribute.lastName}")
    private String lastNameAttribute;
    
    @Value("${attribute.firstName}")
    private String firstNameAttribute;
    

    @Override
    protected UserDetails loadUserDetails(Assertion asrtn) {
        
        AttributePrincipal principal = asrtn.getPrincipal();
        
        // retrieve CAS ID
        String id = principal.getName();
        
        // Retrieve all the attributes from CAS response
        Map<String, Object> attributs = principal.getAttributes();
        
        // Adding roles for user       
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        
        String mail=null, lastName=null, firstName=null;  
        
        for(Entry<String, Object> entry : attributs.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(mailAttribute)) {
                mail = (String) entry.getValue();
            }
            
            if(entry.getKey().equalsIgnoreCase(lastNameAttribute)) {
                lastName = (String) entry.getValue();
            }
            
            if(entry.getKey().equalsIgnoreCase(firstNameAttribute)) {
                firstName = (String) entry.getValue();
            }
        }

        return new Member(id, "", authorities, mail, lastName, firstName);  
    }
}
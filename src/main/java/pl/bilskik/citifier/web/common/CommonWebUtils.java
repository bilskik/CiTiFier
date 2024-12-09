package pl.bilskik.citifier.web.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public final class CommonWebUtils {

    public static String retrieveLoginFromAuthentication(Authentication authentication) {
        if(authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return "";
    }

}

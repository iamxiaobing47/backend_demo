package com.taco.backend_demo.common.constants;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class LoginConstants {

    public static final SimpleGrantedAuthority ROLE_GUEST = new SimpleGrantedAuthority("ROLE_GUEST");
    public static final SimpleGrantedAuthority ROLE_AUTHENTICATING = new SimpleGrantedAuthority("ROLE_AUTHENTICATING");
    public static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
}

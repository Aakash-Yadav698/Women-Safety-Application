package com.womensafety.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Small helper so every controller doesn't repeat this SecurityContext
// boilerplate. This is what guarantees "you can only act on YOUR OWN
// account" - the userId always comes from the verified token, never from
// anything the client typed in the request body or URL.
public class AuthUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}

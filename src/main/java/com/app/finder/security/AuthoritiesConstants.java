package com.app.finder.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    //增加文章管理角色
    public static final String ARTICLE_ADMIN = "ROLE_ARTICLE_ADMIN";
    
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}

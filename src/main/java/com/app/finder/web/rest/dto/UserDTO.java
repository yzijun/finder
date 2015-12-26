package com.app.finder.web.rest.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.app.finder.domain.Authority;
import com.app.finder.domain.User;
/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String nickName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;
    
    @Lob
    private byte[] picture;
    
    private String pictureContentType;
    
    private String gender;
    
    private String signature;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(), null, user.getNickName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()), 
            user.getPictureContentType(), user.getPicture(), user.getGender(), user.getSignature());
    }

    public UserDTO(String login, String password, String nickName,
        String email, boolean activated, String langKey, Set<String> authorities,
        String pictureContentType, byte[] picture, String gender, String signature) {

        this.login = login;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
        this.pictureContentType = pictureContentType;
        this.picture = picture;
        this.gender = gender;
        this.signature = signature;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getNickName() {
		return nickName;
	}

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getPictureContentType() {
		return pictureContentType;
	}

	public void setPictureContentType(String pictureContentType) {
		this.pictureContentType = pictureContentType;
	}
	
	public String getGender() {
		return gender;
	}
	
	public String getSignature() {
		return signature;
	}

	public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", password='" + password + '\'' +
            ", nickName='" + nickName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", gender='" + gender + '\'' +
            ", signature='" + signature + '\'' +
            ", authorities=" + authorities +
            "}";
    }
}

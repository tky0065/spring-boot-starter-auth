package com.enokdev.spring_boot_starter_auth.oauth2;

import com.enokdev.spring_boot_starter_auth.exeption.ValidationException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new ValidationException("Connexion avec " + registrationId + " n'est pas supportée.");
        }
    }
}

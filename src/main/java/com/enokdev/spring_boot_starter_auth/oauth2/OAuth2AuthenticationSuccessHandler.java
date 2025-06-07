package com.enokdev.spring_boot_starter_auth.oauth2;

import com.enokdev.spring_boot_starter_auth.config.OAuth2Properties;
import com.enokdev.spring_boot_starter_auth.entities.AuthUser;
import com.enokdev.spring_boot_starter_auth.services.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final OAuth2Properties oAuth2Properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUri = request.getParameter("redirect_uri");

        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri != null ? redirectUri : oAuth2Properties.getSuccessUrl();
        String token = jwtService.generateToken((AuthUser) authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        if (oAuth2Properties.getAuthorizedRedirectUris().isEmpty()) {
            return true; // If no authorized URIs are set, allow any redirect URI
        }

        return oAuth2Properties.getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> uri.startsWith(authorizedRedirectUri));
    }
}

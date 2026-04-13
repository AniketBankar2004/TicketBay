package com.aniket.TicketBay.service;

import com.aniket.TicketBay.model.AppUser;
import com.aniket.TicketBay.model.Role;
import com.aniket.TicketBay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest request)
            throws OAuth2AuthenticationException {

        System.out.println(">>> loadUser called");

        OidcUser oidcUser = super.loadUser(request);

        Map<String, Object> attrs = oidcUser.getAttributes();
        String provider   = request.getClientRegistration().getRegistrationId();
        String providerId = attrs.get("sub").toString();
        String email      = attrs.get("email").toString();
        String name       = attrs.get("name").toString();

        System.out.println(">>> email=" + email);

        AppUser user = userRepository
                .findByProviderAndProviderId(provider, providerId)
                .map(existing -> updateExistingUser(existing, name))
                .orElseGet(() -> createNewUser(provider, providerId, email, name));

        System.out.println(">>> role=" + user.getRole().name());

        // return a NEW DefaultOidcUser with YOUR role as the authority
        return new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority(user.getRole().name())),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "email"                  // use email as the principal name
        );
    }

    private AppUser createNewUser(String provider, String providerId,
                                  String email, String name) {
        AppUser user = AppUser.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .name(name)
                .role(Role.ROLE_USER)
                .build();
        return userRepository.save(user);
    }

    private AppUser updateExistingUser(AppUser existing, String name) {
        existing.setName(name);
        return userRepository.save(existing);
    }
}
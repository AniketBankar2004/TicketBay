package com.aniket.TicketBay.service;

import com.aniket.TicketBay.model.AppUser;
import com.aniket.TicketBay.model.Role;
import com.aniket.TicketBay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService  extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();

        Map<String, Object> attrs = oAuth2User.getAttributes();
        String providerId = attrs.get("sub").toString();   // Google's unique user ID
        String email      = attrs.get("email").toString();
        String name       = attrs.get("name").toString();

        AppUser user = userRepository
                .findByProvierAndproviderId(provider,providerId)
                .map(existing->updateExistingUser(existing,name))
                .orElseGet(()->createNewUser(provider,providerId,email,name));
        return new DefaultOAuth2User(
                List.of(()->user.getRole().name()),
                attrs,
                "email"
        );

    }

    private AppUser createNewUser(String provider, String providerId, String email, String name) {
        AppUser user = AppUser.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .name(name)
                .role(Role.ROLE_USER)
                .build();
        return userRepository.save(user);
    }

    private AppUser updateExistingUser(AppUser existingUser, String name) {
        existingUser.setName(name);
        return userRepository.save(existingUser);
    }
}

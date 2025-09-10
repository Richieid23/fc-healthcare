package id.web.fitrarizki.healthcare.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import id.web.fitrarizki.healthcare.dto.UserInfo;
import id.web.fitrarizki.healthcare.entity.Role;
import id.web.fitrarizki.healthcare.entity.User;
import id.web.fitrarizki.healthcare.repository.RoleRepository;
import id.web.fitrarizki.healthcare.repository.UserRepository;
import id.web.fitrarizki.healthcare.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CacheService cacheService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final String USER_CACHE_KEY = "cache:user:";
    private final String USER_ROLES_CACHE_KEY = "cache:user:roles:";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userCacheKey = USER_CACHE_KEY + username;
        String roleCacheKey = USER_ROLES_CACHE_KEY + username;

        Optional<User> userCache = cacheService.get(userCacheKey, User.class);
        Optional<List<Role>> roleCache = cacheService.get(roleCacheKey, new TypeReference<List<Role>>(){});

        if (userCache.isPresent() && roleCache.isPresent()) {
            return UserInfo.builder()
                    .user(userCache.get())
                    .roles(roleCache.get())
                    .build();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        List<Role> roles = roleRepository.findByUserId(user.getId());


        cacheService.put(userCacheKey, user);
        cacheService.put(roleCacheKey, roles);

        return UserInfo.builder()
                .user(user)
                .roles(roles)
                .build();
    }
}

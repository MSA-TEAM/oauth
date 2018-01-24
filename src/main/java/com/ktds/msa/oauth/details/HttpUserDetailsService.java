package com.ktds.msa.oauth.details;

import com.ktds.msa.oauth.details.BaseUserDetails;
import com.ktds.msa.oauth.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HttpUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BaseUserDetails userDetails = null;


        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://USER/user/{userId}")
                .buildAndExpand(username)
                .encode();

        HttpEntity<User> entity = restTemplate.getForEntity(uriComponents.toUri(), User.class);

        if(entity==null) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        userDetails = new BaseUserDetails();

        User user = entity.getBody();

        logger.info(user.toString());

        userDetails.setUsername(username);
        userDetails.setPassword(user.getUserPass());
        userDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));

        userDetails.setAccountNonExpired(user.getAccountNonExpired().booleanValue());           // 계정 만료
        userDetails.setAccountNonLocked(user.getAccountNonLocked().booleanValue());             // 계정 잠김
        userDetails.setCredentialsNonExpired(user.getCredentialsNonExpired().booleanValue());   // 비밀번호 만료
        userDetails.setEnabled(user.getEnabled().booleanValue());

        return userDetails;
    }
}

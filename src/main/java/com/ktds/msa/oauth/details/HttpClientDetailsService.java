package com.ktds.msa.oauth.details;

import com.ktds.msa.oauth.domain.Client;
import com.ktds.msa.oauth.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;


@Service
public class HttpClientDetailsService implements ClientDetailsService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        BaseClientDetails clientDetails = new BaseClientDetails();

        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://CLIENT/client/{clientId}")
                .buildAndExpand(clientId)
                .encode();

        HttpEntity<Client> entity = restTemplate.getForEntity(uriComponents.toUri(), Client.class);

        if(entity==null) throw new InvalidClientException("클라이언트를 찾을 수 없습니다.");

        Client client = entity.getBody();

        logger.info(client.toString());

        clientDetails.setClientId(clientId);
        clientDetails.setClientSecret(client.getClientSecret());
        clientDetails.setResourceIds(Arrays.asList(client.getResourceIds()));
        clientDetails.setScope(Arrays.asList(client.getScope()));
        clientDetails.setAuthorizedGrantTypes(StringUtils.commaDelimitedListToSet(client.getAuthorizedGrantTypes()));
        clientDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(client.getAuthorities()));
        clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());

        return clientDetails;
    }

}

package org.cloudfoundry.identity.uaa.oauth;

import java.util.Map;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.google.common.collect.Maps;

public class UaaTokenEnhancerImpl implements UaaTokenEnhancer {

    public Map<String, String> getExternalAttributes(OAuth2Authentication authentication) {

        Map<String, String> map = Maps.newHashMap();

        authentication.getPrincipal();

        return map;
    }

}

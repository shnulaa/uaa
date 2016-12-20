package org.cloudfoundry.identity.uaa.oauth;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.cloudfoundry.identity.uaa.authentication.UaaPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.google.common.collect.Maps;

/**
 * UaaTokenEnhancerImpl
 * 
 * @author liuyq
 *
 */
public class UaaTokenEnhancerImpl implements UaaTokenEnhancer {

    /**
     * Mapping the useful information to ext_attr
     */
    public Map<String, String> getExternalAttributes(OAuth2Authentication authentication) {

        Map<String, String> map = Maps.newHashMap();
        Object principal = authentication.getPrincipal();
        if (principal != null && principal instanceof UaaPrincipal) {
            final UaaPrincipal uaaPrincipal = (UaaPrincipal) principal;
            map.put("sex", uaaPrincipal.getSex() == null ? "" : uaaPrincipal.getSex().toString());
            map.put("user_pic", StringUtils.isBlank(uaaPrincipal.getUserPic()) ? "" : uaaPrincipal.getUserPic());
            map.put("external_id",
                    StringUtils.isBlank(uaaPrincipal.getExternalId()) ? "" : uaaPrincipal.getExternalId());
            map.put("nick_name", StringUtils.isBlank(uaaPrincipal.getNickName()) ? "" : uaaPrincipal.getNickName());
        }
        return map;
    }
}

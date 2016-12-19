package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

/**
 * QQClaimsFetcher
 * 
 * @author liuyq
 *
 */
public class QQClaimsFetcher extends AbstractClaimsFetcher {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4118883092983521838L;

    /**
     * the default constructor
     * 
     * @param codeToken
     *            code token info
     * @param config
     *            the YML file information
     */
    public QQClaimsFetcher(RestTemplate restTemplate, IdentityProviderProvisioning providerProvisioning) {
        super(restTemplate, providerProvisioning);
    }

    @Override
    protected Map<String, Object> getToken(XOAuthCodeToken codeToken, AbstractXOAuthIdentityProviderDefinition config) {
        Map<String, String> tokenParas = Maps.newHashMap();
        tokenParas.put("client_id", config.getRelyingPartyId());
        tokenParas.put("client_secret", config.getRelyingPartySecret());
        tokenParas.put("grant_type", "authorization_code");
        tokenParas.put("code", codeToken.getCode());
        tokenParas.put("redirect_uri", codeToken.getRedirectUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        Map<String, Object> tokenMap = restHttp(HttpMethod.GET, config.isSkipSslValidation(),
                config.getTokenUrl().toString(), tokenParas, headers);

        String accessToken = (String) tokenMap.get("access_token");
        if (StringUtils.isBlank(accessToken)) {
            log.error("accessToken is empty or null when fetch qq token.");
            return null;
        }

        Map<String, String> openIdParas = Maps.newHashMap();
        openIdParas.put("access_token", (String) tokenMap.get("access_token"));
        Map<String, Object> openIdMap = restHttp(HttpMethod.GET, config.isSkipSslValidation(),
                config.getTokenKeyUrl().toString(), openIdParas, headers);

        Object callbackMap = openIdMap.get("callback");
        if (callbackMap == null) {
            log.error("openid is empty or null when fetch qq openid.");
            return null;
        }
        tokenMap.putAll((Map<String, Object>) callbackMap);
        return tokenMap;
    }

    @Override
    protected Map<String, Object> getUserInfo(AbstractXOAuthIdentityProviderDefinition config,
            OAuthOpenIdToken openidToken) {
        final String accessToken = openidToken.getAccessToken();
        final String openId = openidToken.getOpenId();

        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)) {
            log.error("accessToken or openid is empty or null .");
            return null;
        }

        Map<String, String> paras = Maps.newHashMap();
        paras.put("access_token", accessToken);
        paras.put("openid", openId);
        paras.put("oauth_consumer_key", config.getRelyingPartyId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        final String userInfoUrl = config.getIssuer();

        Map<String, Object> map = restHttp(HttpMethod.GET, config.isSkipSslValidation(), userInfoUrl, paras, headers);
        map.put("openId", openId);
        return map;
    }
}

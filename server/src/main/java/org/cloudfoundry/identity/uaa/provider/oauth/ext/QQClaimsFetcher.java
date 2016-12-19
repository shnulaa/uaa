package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        // retMap return the access_token and openid
        Map<String, Object> retMap = Maps.newHashMap();

        //////////////////////////////////////////////////////////
        ////////////////////// fetch QQ token/////////////////////
        //////////////////////////////////////////////////////////
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", config.getRelyingPartyId());
        body.add("client_secret", config.getRelyingPartySecret());
        body.add("grant_type", "authorization_code");
        body.add("code", codeToken.getCode());
        body.add("redirect_uri", codeToken.getRedirectUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> tokenMap = restHttpForm(config.isSkipSslValidation(),
                config.getTokenUrl().toString(), body, headers);

        String accessToken = (String) tokenMap.getFirst("access_token");
        if (StringUtils.isBlank(accessToken)) {
            log.error("accessToken is empty or null when fetch qq token.");
            return null;
        }
        retMap.put("access_token", accessToken); // add access_token

        //////////////////////////////////////////////////////////
        ////////////////////// fetch QQ OpenId/////////////////////
        //////////////////////////////////////////////////////////
        MultiValueMap<String, String> openIdBody = new LinkedMultiValueMap<>();
        openIdBody.add("access_token", accessToken);

        MultiValueMap<String, Object> openIdMap = restHttpForm(config.isSkipSslValidation(),
                config.getTokenKeyUrl().toString(), openIdBody, headers);

        retMap.put("openid", openIdMap.getFirst("openid")); // add openid
        return retMap;
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

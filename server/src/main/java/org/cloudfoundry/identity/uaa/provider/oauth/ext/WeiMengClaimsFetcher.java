package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

/**
 * WeiMengClaimsFetcher
 * 
 * @author liuyq
 *
 */
public class WeiMengClaimsFetcher extends AbstractClaimsFetcher {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8396885960295818190L;

    /**
     * the default constructor
     * 
     * @param codeToken
     *            code token info
     * @param config
     *            the YML file information
     */
    public WeiMengClaimsFetcher(RestTemplate restTemplate, IdentityProviderProvisioning providerProvisioning) {
        super(restTemplate, providerProvisioning);
    }

    @Override
    protected Map<String, Object> getToken(XOAuthCodeToken codeToken, AbstractXOAuthIdentityProviderDefinition config) {
        Map<String, String> paras = Maps.newHashMap();
        paras.put("client_id", config.getRelyingPartyId());
        paras.put("client_secret", config.getRelyingPartySecret());
        paras.put("grant_type", "authorization_code");
        paras.put("code", codeToken.getCode());
        paras.put("redirect_uri", codeToken.getRedirectUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return restHttp(HttpMethod.POST, config.isSkipSslValidation(), config.getTokenUrl().toString(), paras, headers);
    }

    @Override
    protected Map<String, Object> getUserInfo(AbstractXOAuthIdentityProviderDefinition config,
            OAuthOpenIdToken openidToken) {
        Map<String, String> paras = Maps.newHashMap();
        paras.put("access_token", openidToken.getAccessToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return restHttp(HttpMethod.GET, config.isSkipSslValidation(), config.getIssuer().toString(), paras, headers);
    }

}

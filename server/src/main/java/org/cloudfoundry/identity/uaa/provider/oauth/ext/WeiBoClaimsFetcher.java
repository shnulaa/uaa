package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

/**
 * WeiBoClaimsFetcher
 * 
 * @author liuyq
 *
 */
public class WeiBoClaimsFetcher extends AbstractClaimsFetcher {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4420811822143285776L;

    /**
     * the default constructor
     * 
     * @param codeToken
     *            code token info
     * @param config
     *            the YML file information
     */
    public WeiBoClaimsFetcher(RestTemplate restTemplate, IdentityProviderProvisioning providerProvisioning) {
        super(restTemplate, providerProvisioning);
    }

    @Override
    protected Map<String, Object> getToken(XOAuthCodeToken codeToken, AbstractXOAuthIdentityProviderDefinition config) {
        Map<String, String> paras = Maps.newHashMap();
        paras.put("client_id", config.getRelyingPartyId());
        paras.put("client_secret", config.getRelyingPartySecret());
        paras.put("grant_type", "authorization_code");
        paras.put("code", codeToken.getCode());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return get(config.isSkipSslValidation(), config.getTokenUrl().toString(), paras, headers);
    }

    @Override
    protected Map<String, Object> getUserInfo(AbstractXOAuthIdentityProviderDefinition config,
            OAuthOpenIdToken openidToken) {
        Map<String, String> paras = Maps.newHashMap();
        paras.put("access_token", openidToken.getAccessToken());
        paras.put("uid", openidToken.getUid());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return get(config.isSkipSslValidation(), config.getIssuer().toString(), paras, headers);
    }

}

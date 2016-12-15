package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

/**
 * WeiXinClaimsFetcher
 * 
 * @author liuyq
 *
 */
public class WeiXinClaimsFetcher extends AbstractClaimsFetcher {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5169989744530750966L;

    /**
     * the default constructor
     * 
     * @param codeToken
     *            code token info
     * @param config
     *            the YML file information
     */
    public WeiXinClaimsFetcher(RestTemplate restTemplate, IdentityProviderProvisioning providerProvisioning) {
        super(restTemplate, providerProvisioning);
    }

    @Override
    protected Map<String, Object> getToken(XOAuthCodeToken codeToken, AbstractXOAuthIdentityProviderDefinition config) {
        Map<String, String> paras = Maps.newHashMap();
        paras.put("appid", config.getRelyingPartyId());
        paras.put("secret", config.getRelyingPartySecret());
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
        paras.put("openid", openidToken.getOpenId());
        paras.put("lang", "zh_CN");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return get(config.isSkipSslValidation(), config.getIssuer().toString(), paras, headers);
    }

}

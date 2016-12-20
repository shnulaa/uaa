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

    private static final String GENDER = "gender";
    private static final String SEX = "sex";

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
        paras.put("uid", openidToken.getUid());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        Map<String, Object> map = restHttp(HttpMethod.GET, config.isSkipSslValidation(), config.getIssuer().toString(),
                paras, headers);
        if (map == null || map.isEmpty()) {
            return null;
        }
        map.put(SEX, mappingGender(map));
        return map;
    }

    /**
     * mapping the gender
     * @param gender
     * @return
     */
    private Integer mappingGender(Map<String, Object> map) {
        String gender = (String) map.get(GENDER);
        if (StringUtils.isBlank(gender)) {
            return null;
        }
        switch (gender) {
        case "m":
            return 1;
        case "f":
            return 2;
        case "u":
            return 0;
        default:
            return null;
        }
    }
}

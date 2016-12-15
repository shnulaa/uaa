package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.springframework.web.client.RestTemplate;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Map<String, Object> getUserInfo(AbstractXOAuthIdentityProviderDefinition config,
            OAuthOpenIdToken openidToken) {
        // TODO Auto-generated method stub
        return null;
    }

}

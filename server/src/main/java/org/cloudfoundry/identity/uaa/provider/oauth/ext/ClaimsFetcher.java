package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;

/**
 * the instance of Claims fetcher
 * 
 * @author liuyq
 *
 */
public interface ClaimsFetcher {

	/**
	 * get Claims user information
	 * 
	 * @return the information of Claims user
	 */
	Map<String, Object> getClaims(XOAuthCodeToken codeToken, AbstractXOAuthIdentityProviderDefinition config);

}

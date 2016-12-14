package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.ExternalOAuthAuthenticationManager.AuthType;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * the factory of ClaimsFetcher
 * 
 * @author liuyq
 *
 */
public final class ClaimsFetcherFactory {
	/**
	 * the log instance
	 */
	protected final static Logger log = LoggerFactory.getLogger(ClaimsFetcherFactory.class);

	/**
	 * get instance of ClaimsFetcher
	 * 
	 * @param type
	 * @param codeToken
	 * @param config
	 * @return the instance of ClaimsFetcher
	 */
	public static ClaimsFetcher getInstance(AuthType type, RestTemplate restTemplate,
			IdentityProviderProvisioning providerProvisioning) {
		if (type == null) {
			log.error("the AuthType is null!");
			throw new IllegalArgumentException("the AuthType is null!");
		}

		switch (type) {
		case WEIXIN:
			return new WeiXinClaimsFetcher(restTemplate, providerProvisioning);
		case QQ:
			return new QQClaimsFetcher(restTemplate, providerProvisioning);
		case WEIBO:
			return new WeiBoClaimsFetcher(restTemplate, providerProvisioning);
		case WEIMENG:
			return new WeiMengClaimsFetcher(restTemplate, providerProvisioning);
		default:
			log.error("the AuthType is not support!");
			throw new IllegalArgumentException("the AuthType is not support!");
		}

	}

}

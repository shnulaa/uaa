package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * WeiXinClaimsFetcher
 * 
 * @author liuyq
 *
 */
public class WeiXinClaimsFetcher extends AbstractClaimsFetcher {

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
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("appid", config.getRelyingPartyId());
		body.add("secret", config.getRelyingPartySecret());
		body.add("grant_type", "authorization_code");
		body.add("code", codeToken.getCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		return restHttp(config, body, headers, HttpMethod.GET);
	}

	@Override
	protected Map<String, Object> getUserInfo(AbstractXOAuthIdentityProviderDefinition config, String accessToken,
			String openId) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("access_token", accessToken);
		body.add("openid", openId);
		body.add("lang", "zh_CN");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		return restHttp(config, body, headers, HttpMethod.GET);
	}

}

package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import static org.cloudfoundry.identity.uaa.util.UaaHttpRequestUtils.getNoValidatingClientHttpRequestFactory;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.xpath.operations.Bool;
import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

/**
 * the abstract class of ClaimsFetcher
 * 
 * @author liuyq
 *
 */
public abstract class AbstractClaimsFetcher implements ClaimsFetcher {

	/**
	 * the log instance
	 */
	protected final static Logger log = LoggerFactory.getLogger(AbstractClaimsFetcher.class);

	protected RestTemplate restTemplate;
	protected IdentityProviderProvisioning providerProvisioning;

	// protected XOAuthCodeToken codeToken;
	// protected AbstractXOAuthIdentityProviderDefinition<?> config;

	/**
	 * getToken with code using http client
	 * 
	 * @return token information map
	 */
	protected abstract Map<String, Object> getToken(XOAuthCodeToken codeToken,
			AbstractXOAuthIdentityProviderDefinition config);

	/**
	 * getUserInfo with token using http client
	 * 
	 * @return Claims user info map
	 */
	protected abstract Map<String, Object> getUserInfo(AbstractXOAuthIdentityProviderDefinition config,
			String accessToken, String openId);

	/**
	 * the default constructor
	 * 
	 * @param codeToken
	 * @param config
	 */
	public AbstractClaimsFetcher(RestTemplate restTemplate, IdentityProviderProvisioning providerProvisioning) {
		this.restTemplate = restTemplate;
		this.restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		this.providerProvisioning = providerProvisioning;
	}

	@Override
	public Map<String, Object> getClaimss(XOAuthCodeToken codeToken, AbstractXOAuthIdentityProviderDefinition config) {
		// first get the token from code
		Map<String, Object> tokens = getToken(codeToken, config);
		if (tokens == null || tokens.isEmpty()) {
			log.error("getToken ret tokens is null or empty..");
			return null;
		}

		String accessToken = (String) tokens.get("access_token");
		String openId = (String) tokens.get("openid");
		if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)) {
			log.error("accessToken or openId is balank..");
			return null;
		}
		log.info("accessToken:%s, openId:%s.", accessToken, openId);

		Map<String, Object> userInfo = getUserInfo(config, accessToken, openId);
		if (userInfo == null || userInfo.isEmpty()) {
			log.error("userInfo is null or isEmpty..");
			return null;
		}
		return userInfo;
	}

	/**
	 * get to specified the URL with body
	 * 
	 * @param config
	 * @param body
	 * @param headers
	 * @return
	 */
	protected Map<String, Object> get(boolean skipSsl, String baseUrl, Map<String, String> paras,
			HttpHeaders headers) {
		final String requestUrl = appendUrl(baseUrl, paras);
		try {
			if (skipSsl) {
				restTemplate.setRequestFactory(getNoValidatingClientHttpRequestFactory());
			}
			HttpEntity requestEntity = new HttpEntity<>(null, headers);
			ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET,
					requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {
					});
			return responseEntity.getBody();
		} catch (HttpServerErrorException | HttpClientErrorException ex) {
			log.error("Http exception occurred when POST..", ex);
			throw ex;
		}
	}

	/**
	 * appendUrl
	 * 
	 * @param baseUrl
	 * @param paras
	 * @return
	 */
	private String appendUrl(String baseUrl, Map<String, String> paras) {
		StringBuffer url = new StringBuffer();
		url.append(baseUrl.toString());
		url.append("?");
		for (final Entry<String, String> entry : paras.entrySet()) {
			url.append(entry.getKey());
			url.append("=");
			url.append(entry.getValue());
			url.append("&");
		}
		return url.toString();
	}

}

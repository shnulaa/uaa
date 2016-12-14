/*******************************************************************************
 * Cloud Foundry
 * Copyright (c) [2009-2016] Pivotal Software, Inc. All Rights Reserved.
 * <p>
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 * <p>
 * This product includes a number of subcomponents with
 * separate copyright notices and license terms. Your use of these
 * subcomponents is subject to the terms and conditions of the
 * subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/

package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.cloudfoundry.identity.uaa.provider.AbstractXOAuthIdentityProviderDefinition;
import org.cloudfoundry.identity.uaa.provider.IdentityProviderProvisioning;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthAuthenticationManager;
import org.cloudfoundry.identity.uaa.provider.oauth.XOAuthCodeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class intend for WEIXIN, QQ, WEIBO, WEIMENG AuthAuthentication
 * 
 * @author liuyq
 *
 */
public class ExternalOAuthAuthenticationManager extends XOAuthAuthenticationManager {
    /**
     * the log instance
     */
    protected final static Logger log = LoggerFactory.getLogger(ExternalOAuthAuthenticationManager.class);

    /**
     * the enum of AuthType
     * 
     * @author liuyq
     *
     */
    public static enum AuthType {
        WEIXIN, QQ, WEIBO, WEIMENG, GOOGLE
    }

    /**
     * default constructor
     * 
     * @param providerProvisioning
     */
    public ExternalOAuthAuthenticationManager(IdentityProviderProvisioning providerProvisioning) {
        super(providerProvisioning);
    }

    @Override
    protected Map<String, Object> getClaimsFromToken(XOAuthCodeToken codeToken,
            AbstractXOAuthIdentityProviderDefinition config) {
        String origin = codeToken.getOrigin();
        if (StringUtils.isBlank(origin)) {
            log.error("given origin is null when getClaimsFromToken.");
            return null;
        }

        AuthType authType = AuthType.valueOf(origin.toUpperCase());
        // if google then go super method
        if (authType == AuthType.GOOGLE) {
            return super.getClaimsFromToken(codeToken, config);
        }

        // get instance of ClaimsFetcher
        ClaimsFetcher fetcher = ClaimsFetcherFactory.getInstance(authType, restTemplate, providerProvisioning);
        if (fetcher == null) {
            log.error("the instance of ClaimsFetcher is null when create..");
            return null;
        }

        try {
            return fetcher.getClaims(codeToken, config);
        } catch (Exception e) {
            log.error("Exception occurred when getClaimss.. ", e);
        }
        return null;
    }

}

package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.io.Serializable;

/**
 * OAuthOpenIdToken
 * @author liuyq
 *
 */
public class OAuthOpenIdToken implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8687847628371698805L;

    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String openId;
    private String scope;
    private String unionId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

}

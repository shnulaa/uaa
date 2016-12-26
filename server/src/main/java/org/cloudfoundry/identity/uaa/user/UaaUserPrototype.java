package org.cloudfoundry.identity.uaa.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.security.core.GrantedAuthority;
import org.xml.sax.SAXException;

/*******************************************************************************
 * Cloud Foundry Copyright (c) [2009-2015] Pivotal Software, Inc. All Rights
 * Reserved.
 * <p>
 * This product is licensed to you under the Apache License, Version 2.0 (the
 * "License"). You may not use this product except in compliance with the
 * License.
 * <p>
 * This product includes a number of subcomponents with separate copyright
 * notices and license terms. Your use of these subcomponents is subject to the
 * terms and conditions of the subcomponent's license, as noted in the LICENSE
 * file.
 *******************************************************************************/
public final class UaaUserPrototype {

    private String id = "NaN";

    private String username;

    private String password;

    private String email;

    private String givenName;

    private String familyName;

    private String phoneNumber;

    private Date created;

    private Date modified;

    private String origin;

    private String externalId;

    private String salt;

    private Date passwordLastModified;

    private String zoneId;

    private String sex;

    private String userPic;

    private List<? extends GrantedAuthority> authorities;

    private boolean verified = false;

    private boolean legacyVerificationBehavior;

    public String getId() {
        return id;
    }

    public UaaUserPrototype withId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UaaUserPrototype withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UaaUserPrototype withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UaaUserPrototype withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public UaaUserPrototype withGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public UaaUserPrototype withFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UaaUserPrototype withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public UaaUserPrototype withCreated(Date created) {
        this.created = created;
        return this;
    }

    public Date getModified() {
        return modified;
    }

    public UaaUserPrototype withModified(Date modified) {
        this.modified = modified;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public UaaUserPrototype withOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public UaaUserPrototype withExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public UaaUserPrototype withSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public Date getPasswordLastModified() {
        return passwordLastModified;
    }

    public UaaUserPrototype withPasswordLastModified(Date passwordLastModified) {
        this.passwordLastModified = passwordLastModified;
        return this;
    }

    public String getZoneId() {
        return zoneId;
    }

    public UaaUserPrototype withZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UaaUserPrototype withAuthorities(List<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getSex() {
        return sex;
    }

    public UaaUserPrototype withSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getUserPic() {
        return userPic;
    }

    public UaaUserPrototype withUserPic(String userPic) {
        this.userPic = userPic;
        return this;
    }

    public UaaUserPrototype withVerified(boolean verified) {
        this.verified = verified;
        return this;
    }

    public boolean isLegacyVerificationBehavior() {
        return legacyVerificationBehavior;
    }

    public UaaUserPrototype withLegacyVerificationBehavior(boolean legacyVerificationBehavior) {
        this.legacyVerificationBehavior = legacyVerificationBehavior;
        return this;
    }

    public static void printMatches(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        // Check all occurrences
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
            System.out.println(" Found: " + matcher.group());
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String content = new String(Files.readAllBytes(Paths.get("d:\\1.xml"))); 
        printMatches(content, "2764490341");
    }
}

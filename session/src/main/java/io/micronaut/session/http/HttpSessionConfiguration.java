/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.session.http;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.HttpHeaders;
import io.micronaut.session.SessionConfiguration;

import java.time.temporal.TemporalAmount;
import java.util.Optional;

/**
 * Allows configuration of the session.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@ConfigurationProperties("http")
public class HttpSessionConfiguration extends SessionConfiguration {

    /**
     * Cookie name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String DEFAULT_COOKIENAME = "SESSION";

    /**
     * The default remember me value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_REMEMBERME = false;

    /**
     * The default base64 encode value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_BASE64ENCODE = true;

    private boolean rememberMe = DEFAULT_REMEMBERME;
    private boolean base64Encode = DEFAULT_BASE64ENCODE;
    private TemporalAmount cookieMaxAge;
    private String cookiePath;
    private String domainName;
    private String cookieName = DEFAULT_COOKIENAME;
    private String prefix;
    private String[] headerNames = new String[] { HttpHeaders.AUTHORIZATION_INFO, HttpHeaders.X_AUTH_TOKEN };

    /**
     * @return Whether the Base64 encode sessions IDs sent back to clients
     */
    public boolean isBase64Encode() {
        return base64Encode;
    }

    /**
     * Default value ({@value #DEFAULT_BASE64ENCODE}).
     * @param base64Encode Enable the Base64 encode for sessions IDs sent back to clients
     */
    public void setBase64Encode(boolean base64Encode) {
        this.base64Encode = base64Encode;
    }

    /**
     * @return The cookie name to use
     */
    public String getCookieName() {
        return cookieName;
    }

    /**
     * Default value ({@value #DEFAULT_COOKIENAME}).
     * @param cookieName Set the cookie name to use
     */
    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    /**
     * @return The prefix to use when serializing session ID
     */
    public Optional<String> getPrefix() {
        return Optional.ofNullable(prefix);
    }

    /**
     * @param prefix Set the prefix to use when serializing session ID
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return The header names when using a Header strategy
     */
    public String[] getHeaderNames() {
        return headerNames;
    }

    /**
     * Default values ([{@value io.micronaut.http.HttpHeaders#AUTHORIZATION_INFO}, {@value io.micronaut.http.HttpHeaders#X_AUTH_TOKEN}]).
     * @param headerNames Set the header names when using a Header strategy
     */
    public void setHeaderNames(String[] headerNames) {
        this.headerNames = headerNames;
    }

    /**
     * @return The cookie path to use
     */
    public Optional<String> getCookiePath() {
        return Optional.ofNullable(cookiePath);
    }

    /**
     * @param cookiePath Set the cookie path to use
     */
    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    /**
     * @return The domain name to use for the cookie
     */
    public Optional<String> getDomainName() {
        return Optional.ofNullable(domainName);
    }

    /**
     * @param domainName Set the domain name to use for the cookie
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * @return The max age to use for the cookie
     */
    public Optional<TemporalAmount> getCookieMaxAge() {
        return Optional.ofNullable(cookieMaxAge);
    }

    /**
     * @param cookieMaxAge Set the max age to use for the cookie
     */
    public void setCookieMaxAge(TemporalAmount cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    /**
     * @return Is remember me config
     */
    public boolean isRememberMe() {
        return rememberMe;
    }

    /**
     * Default value ({@value #DEFAULT_REMEMBERME}).
     * @param rememberMe Enable the remember me setting
     */
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}

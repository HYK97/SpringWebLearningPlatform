package com.hy.demo.Config.OAuth.Provider;

public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();
}

package com.hy.demo.config.oauth;

import com.hy.demo.Entity.User;
import com.hy.demo.Repository.UserRepository;
import com.hy.demo.config.auth.PrincipalDetails;
import com.hy.demo.config.oauth.provider.FacebookUserInfo;
import com.hy.demo.config.oauth.provider.GoogleUserInfo;
import com.hy.demo.config.oauth.provider.NaverUserInfo;
import com.hy.demo.config.oauth.provider.OAuth2UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder ;

    @Autowired
    private UserRepository userRepository;
    // 구글로 부터 받은 userRequest 데이터를 후처리하는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        logger.info("userRequest = " + userRequest.getClientRegistration()); //getClientRegistration 으로 어떤 OAuth로 로그인했는지 확인가능
        logger.info("getAccessToken.getTokenValue() = " + userRequest.getAccessToken().getTokenValue());
        logger.info("loadUser(userRequest).getAttributes() = " + super.loadUser(userRequest).getAttributes());
        /**  loadUser(userRequest).getAttributes() 의 리턴값
         * {sub=12312423412341231412345123,
         * name=KsBi kim,
         * given_name=KsBi,
         * family_name=KsBi,
         * picture=https://lh3.google.com/a/AATXAJzPTCeqTDf3ywaH_28lKvL1=s96-c,
         * email=ddha963dw963@gmail.com,
         * email_verified=true,
         * locale=ko}
         *
         * 예를 들면 회원가입은
         * username = google_12312423412341231412345123 이런식으로 하면중복안됌
         * password = "암호화(겟인데어)"
         * email = ddha963dw963@gmail.com
         * role = ROLE_USER
         * provider = "google"
         * providerId= ="12312423412341231412345123" 이런식으로 저장
         * */
        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> 코드를 리턴받음 ()OAuth-client라이브러리) -> AccessToken 요청 여기까지가
        //userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필을 받아준다.

        OAuth2User auth2User =super.loadUser(userRequest);

        String providerId=null;
            OAuth2UserInfo oAuth2UserInfo =null;
        //userRequest.getClientRegistration().getRegistrationId() = 회원가입 페이지.
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo =new GoogleUserInfo(auth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            oAuth2UserInfo =new FacebookUserInfo(auth2User.getAttributes());
        }else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo =new NaverUserInfo((Map)auth2User.getAttributes().get("response"));
        } else {
            logger.info(" 지원하지않는 OAuth 로그인입니다. ");
        }

        String password =bCryptPasswordEncoder.encode("겟인데어");
        String username=oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProviderId();
        String role ="ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity =User.builder()
                    .username(username)
                    .password(password)
                    .email(oAuth2UserInfo.getEmail())
                    .role(role)
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            userRepository.save(userEntity);
        }


        return new PrincipalDetails(userEntity,auth2User.getAttributes());

    }
}

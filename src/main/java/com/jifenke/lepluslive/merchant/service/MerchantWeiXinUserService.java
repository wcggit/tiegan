package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;
import com.jifenke.lepluslive.merchant.repository.MerchantWeiXinUserRepository;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Created by wcg on 16/5/17.
 */
@Service
@Transactional(readOnly = true)
public class MerchantWeiXinUserService {

  @Inject
  private MerchantWeiXinUserRepository merchantWeiXinUserRepository;

  @Inject
  private MerchantService merchantService;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveWeiXinUser(Map<String, Object> userDetail, Map<String, Object> map)
      throws IOException {
    String openid = userDetail.get("openid").toString();
    MerchantWeiXinUser weiXinUser = merchantWeiXinUserRepository.findByOpenId(openid);
    ScoreA scoreA = null;
    ScoreB scoreB = null;
    if (weiXinUser == null) {
      weiXinUser = new MerchantWeiXinUser();
      weiXinUser.setLastUpdated(new Date());
      RegisterOrigin registerOrigin = new RegisterOrigin();
      registerOrigin.setId(1L);
    }

    weiXinUser.setOpenId(openid);
    weiXinUser.setCity(userDetail.get("city").toString());
    weiXinUser.setCountry(userDetail.get("country").toString());
    weiXinUser.setSex(Long.parseLong(userDetail.get("sex").toString()));
    weiXinUser.setNickname(userDetail.get("nickname").toString());
    weiXinUser.setLanguage(userDetail.get("language").toString());
    weiXinUser.setHeadImageUrl(userDetail.get("headimgurl").toString());
    weiXinUser.setProvince(userDetail.get("province").toString());
    weiXinUser.setAccessToken(map.get("access_token").toString());
    weiXinUser.setRefreshToken(map.get("refresh_token").toString());
    weiXinUser.setLastUserInfoDate(new Date());
    merchantWeiXinUserRepository.save(weiXinUser);


  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public MerchantWeiXinUser findWeiXinUserByOpenId(String openid) {
    return merchantWeiXinUserRepository.findByOpenId(openid);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void bindMerchantUser(String openId, MerchantUser merchantUser) {
    MerchantWeiXinUser merchantWeiXinUser = findWeiXinUserByOpenId(openId);
    if (merchantUser.getType() == 1) {
      Optional<MerchantWeiXinUser>
          optional =
          merchantWeiXinUserRepository.findByMerchantUser(merchantUser);
      if (optional.isPresent()) {
        if (!optional.get().getId().equals(merchantWeiXinUser.getId())) {
          throw new RuntimeException();
        }
      }
    }
    merchantWeiXinUser.setMerchantUser(merchantUser);
    merchantWeiXinUserRepository.save(merchantWeiXinUser);

  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void unbindMerchantUser(String openId) {
    MerchantWeiXinUser merchantWeiXinUser = findWeiXinUserByOpenId(openId);
    merchantWeiXinUser.setMerchantUser(null);
    merchantWeiXinUserRepository.save(merchantWeiXinUser);

  }
}

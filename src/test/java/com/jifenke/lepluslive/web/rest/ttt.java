//package com.jifenke.lepluslive.web.rest;
//
//import com.jifenke.lepluslive.Application;
//import com.jifenke.lepluslive.global.config.Constants;
//import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
//import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
//import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
//import com.jifenke.lepluslive.partner.domain.entities.Partner;
//import com.jifenke.lepluslive.score.repository.ScoreARepository;
//import com.jifenke.lepluslive.score.repository.ScoreBRepository;
//import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.IntegrationTest;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//
//import javax.inject.Inject;
//
///**
// * Created by wcg on 16/4/15.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest
//@ActiveProfiles({Constants.SPRING_PROFILE_DEVELOPMENT})
//public class ttt {
//
//
//  @Inject
//  private WeiXinUserRepository weiXinUserRepository;
//
//  @Inject
//  private LeJiaUserRepository leJiaUserRepository;
//
//  @Inject
//  private ScoreARepository scoreARepository;
//
//  @Inject
//  private MerchantRepository merchantRepository;
//
//
//  @Test
//  public void tttt(){
//    List<Merchant> all = merchantRepository.findAll();
//    for(Merchant merchant : all){
////      merchant.setPartner(new Partner(1L));
////      merchant.setCreateDate(new Date());
////      merchant.setCycle(2);
////      merchant.setReceiptAuth(0);
////      merchant.setScoreBRebate(new BigDecimal(50));
////      merchant.setScoreARebate(new BigDecimal(50));
////      merchant.setUserLimit(100L);
////      merchant.setState(0);
////      merchant.setContact(merchant.getPayee());
//      if(merchant.getPartnership()==1){
//        merchant.setLjBrokerage(new BigDecimal(0.6));
//        merchantRepository.save(merchant);
//      }
//    }
//  }
//
//////  public static void main(String[] args) {
//////    int x[][] = new int[9][9];
//////    for(int i=0;i<9;i++){
//////      for(int y=0;y<9;y++){
//////        x[i][y]=new Random().nextInt(2);
//////      }
//////    }
//////    Scanner input = new Scanner(System.in);
//////    int a = input.nextInt();
//////    int b = input.nextInt();
//////    int n = input.nextInt();
//////
//////    for(int z=1;z<n;z++){
//////      int m = x[a][b];
//////      int a1 = x[a-1][b];
//////      int a2 = x[a+1][b];
//////      int a3 = x[a][b+1];
//////      int a4 = x[a][b-1];
//////
//////
//////
//////    }
////
////
////
////  }
//
//
//
//
//}

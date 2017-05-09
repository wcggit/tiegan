package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.repository.FinancialStatisticRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/5/21.
 */
@Service
@Transactional(readOnly = true)
public class FinanicalStatisticService {

  @Inject
  private MerchantService merchantService;

  @Inject
  private FinancialStatisticRepository financialStatisticRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void createFinancialstatistic(Object[] object) {
    Merchant merchant = merchantService.findMerchantById(Long.parseLong(object[0].toString()));
    if (merchant != null) {
      FinancialStatistic financialStatistic = new FinancialStatistic();
      financialStatistic.setMerchant(merchant);
      financialStatistic.setBalanceDate(new Date());
      financialStatistic.setTransferPrice(Long.parseLong(object[1].toString()));
      financialStatisticRepository.save(financialStatistic);
    }
  }

  public Page findFinanicalByMerchantAndPage(Merchant merchant, Integer offset) {

    return financialStatisticRepository.findAllByMerchant(merchant, new PageRequest(offset - 1, 20,
                                                                                    new Sort(
                                                                                        Sort.Direction.DESC,
                                                                                        "balanceDate")));
  }

  public Long sumTotalPriceByMonth(Long dateTime, Merchant merchant) {
    Calendar cal = Calendar.getInstance();
    Date date = new Date(dateTime);
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    Date start = MvUtil.getMonthStartDate(year, month, cal);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    String dateStr=sdf.format(start)+"%";
    return financialStatisticRepository.countTransferPriceByMonth(merchant.getId(), dateStr);
  }

  public FinancialStatistic findFinancialByStatisticId(String id) {
    return financialStatisticRepository.findByStatisticId(id);

  }

  public List<Object[]> findTotalAndNumberFromDailyOrderlePLus(Long id) {
    return financialStatisticRepository.findTotalAndNumberFromDailyOrderlePLus(id);

  }




}

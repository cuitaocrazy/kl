package com.yada.btg.web.service;

import com.yada.btg.web.entity.CardBinType;
import com.yada.btg.web.repository.CardBinTypeRepository;
import com.yada.btg.web.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lj on 2019/12/24
 * <p>
 * Description: 卡binService
 *
 * @author lj
 */
@Service
public class CardBinTypeService {
    /**
     * 卡号不存在
     */
    @Value("${comm.cardNoNotExist}")
    public String cardNoNotExist;

    /**
     * 异常
     */
    @Value("${comm.errorMsg}")
    public String errorMsg;

    private final CardBinTypeRepository cardBinTypeRepository;

    @Autowired
    public CardBinTypeService(CardBinTypeRepository cardBinTypeRepository) {
        this.cardBinTypeRepository = cardBinTypeRepository;
    }

    /**
     * 校验卡bin
     *
     * @param cardNo 卡号
     */
    public String verifyCard(String cardNo) {
        try {
            if (cardNo.length() < 7) {
                return cardNoNotExist;
            }
            String cardBin = cardNo.substring(0, 7);
            List<CardBinType> cardBinTypeList = cardBinTypeRepository.findByCardBinStartingWith(cardBin);
            if (cardBinTypeList == null || cardBinTypeList.size() == 0) {
                return cardNoNotExist;
            } else {
                return "";
            }
        } catch (Exception e) {
            LogUtil.error("校验卡bin系统异常", e);
            return errorMsg;
        }
    }
}
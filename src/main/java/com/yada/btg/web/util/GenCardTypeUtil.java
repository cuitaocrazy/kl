package com.yada.btg.web.util;

import com.yada.btg.web.entity.CardTypeEnum;
import com.yada.btg.web.repository.CardBinTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author wh
 * @date 2019/12/31
 * 获取卡类型
 */
@Component
public class GenCardTypeUtil {
    private final CardBinTypeRepository cardBinTypeRepository;
    private HashMap<String, CardTypeEnum> nameEnumMap;

    @Autowired
    public GenCardTypeUtil(CardBinTypeRepository cardBinTypeRepository) {
        this.cardBinTypeRepository = cardBinTypeRepository;
        nameEnumMap = new HashMap<>();
        nameEnumMap.put("首付通卡", CardTypeEnum.SFT);
        nameEnumMap.put("首礼美点卡", CardTypeEnum.MEIDIAN);
        nameEnumMap.put("贵友卡", CardTypeEnum.GUIYOU);
        nameEnumMap.put("西单卡", CardTypeEnum.XIDAN);
    }

    /**
     * 根据卡bin返回相对应的枚举值
     *
     * @param cardBin 卡bin
     * @return 卡类型枚举值
     */
    public CardTypeEnum getCardType(String cardBin) {
        return nameEnumMap.getOrDefault(cardBinTypeRepository.getOne(cardBin).getCardType(), CardTypeEnum.OTH);
    }
}

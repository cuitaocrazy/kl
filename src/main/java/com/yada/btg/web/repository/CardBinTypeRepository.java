package com.yada.btg.web.repository;

import com.yada.btg.web.entity.CardBinType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by lj on 2019/12/24
 * <p>
 * Description: 卡binDAO
 */
public interface CardBinTypeRepository extends JpaRepository<CardBinType, String> {

    List<CardBinType> findByCardBinStartingWith(String cardBin);

}

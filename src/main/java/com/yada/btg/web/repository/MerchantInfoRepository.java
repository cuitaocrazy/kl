package com.yada.btg.web.repository;

import com.yada.btg.web.entity.MerchantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yn on 2019/12/24.
 * 特约商户主表DAO
 */
public interface MerchantInfoRepository extends JpaRepository<MerchantInfo, String>, JpaSpecificationExecutor<MerchantInfo> {

    /**
     * 获取展示在首页的推荐商户
     *
     * @param recommendFlag 推荐表示
     * @param enableFlag    启用标识
     * @return 首页的推荐商户
     */
    List<MerchantInfo> findTop8ByRecommendFlagAndEnableFlag(String recommendFlag, String enableFlag);

}

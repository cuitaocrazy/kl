package com.yada.btg.web.repository;

import com.yada.btg.web.entity.BusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yn on 2019/12/27.
 * 业务介绍DAO
 */
public interface BusinessInfoRepository extends JpaRepository<BusinessInfo, String> {

    /**
     * 获取前4条介绍业务（按照权重排序）
     *
     * @param showFlag 首页是否显示
     * @return 业务介绍列表
     */
    List<BusinessInfo> findAllByShowFlagOrderByBusinessWeightDesc(Integer showFlag);
}

package com.yada.btg.web.repository;

import com.yada.btg.web.entity.MerLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yn on 2019/12/24.
 * 商户链接DAO
 */
public interface MerLinkRepository extends JpaRepository<MerLink, String>, JpaSpecificationExecutor<MerLink> {
    /**
     * 获取首页的商户链接列表（按照商户序号排序）
     *
     * @return 首页的商户链接列表
     */
    List<MerLink> findAllByOrderByMerOrderDesc();
}

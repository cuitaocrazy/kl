package com.yada.btg.web.repository;

import com.yada.btg.web.entity.StaticRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yn on 2019/12/24.
 * 静态资源DAO
 */
public interface StaticResRepository extends JpaRepository<StaticRes, String>, JpaSpecificationExecutor<StaticRes> {

    /**
     * 获取静态数据
     *
     * @param resName 资源名称
     * @return 资源实体
     */
    StaticRes findByResName(String resName);
}

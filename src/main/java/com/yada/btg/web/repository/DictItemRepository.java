package com.yada.btg.web.repository;

import com.yada.btg.web.entity.DictInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Description: 数字字典表Repository
 *
 * @author songpeijiang
 * @since 2020/1/7
 */
public interface DictItemRepository extends JpaRepository<DictInfo, String>, JpaSpecificationExecutor<DictInfo> {
}

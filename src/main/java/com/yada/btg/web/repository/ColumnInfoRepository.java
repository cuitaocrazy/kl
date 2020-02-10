package com.yada.btg.web.repository;

import com.yada.btg.web.entity.ColumnInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yn on 2019/12/20.
 * 栏目类型DAO
 */
public interface ColumnInfoRepository extends JpaRepository<ColumnInfo, String> {

}

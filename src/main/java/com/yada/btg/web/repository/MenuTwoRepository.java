package com.yada.btg.web.repository;


import com.yada.btg.web.entity.MenuTwo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yn on 2019/12/20.
 * 二级菜单DAO
 */
public interface MenuTwoRepository extends JpaRepository<MenuTwo, String> {

    /**
     * 获取二级菜单列表（按照菜单权重排序）
     *
     * @return 二级菜单列表
     */
    List<MenuTwo> findAllByOrderByMenuWeightDesc();

}

package com.yada.btg.web.repository;

import com.yada.btg.web.entity.MenuOne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yn on 2019/12/20.
 * 一级菜单DAO
 */
public interface MenuOneRepository extends JpaRepository<MenuOne, String> {
    /**
     * 获取一级菜单列表（按照菜单权重排序）
     *
     * @return 一级菜单列表
     */
    List<MenuOne> findAllByOrderByMenuWeightDesc();

}

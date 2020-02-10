package com.yada.btg.web.repository;

import com.yada.btg.web.entity.ColumnInfo;
import com.yada.btg.web.entity.TextInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * @author zsy
 * @date 2019/12/25
 */
public interface TextInfoRepository extends JpaRepository<TextInfo, String> {

    /**
     * 获取每种栏目类型的前6条数据
     *
     * @param columnInfo 栏目类型
     * @return 栏目类型的文章列表
     */
    List<TextInfo> findTop6ByTextTypeOrderByUpdateTimeDesc(ColumnInfo columnInfo);


    Page<TextInfo> findAllByTextType(ColumnInfo columnInfo, Pageable pageable);
}

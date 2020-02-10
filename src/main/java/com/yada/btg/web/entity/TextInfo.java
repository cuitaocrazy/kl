package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zsy
 * @date 2019/12/24
 * 文章实体类
 */
@Data
@Entity
@Table(name = "T_B_TEXT_INFO")
public class TextInfo implements Serializable {
    private static final long serialVersionUID = 7884604428352770431L;
    /**
     * 主键id  uuid
     */
    @Id
    @Column(name = "TEXT_ID", length = 32,nullable = false)
    private String textId;
    /**
     * 文章标题
     */
    @Column(name = "TEXT_TITLE", length = 50)
    private String textTitle;
    /**
     * 文章内容
     */
    @Lob
    @Column(name = "CONTENT")
    private String content;
    /**
     * 文章创建人
     */
    @Column(name = "CREATE_USER", length = 32)
    private String createUser;
    /**
     * 文章更新时间
     */
    @Column(name = "UPDATE_TIME", length = 19)
    private String updateTime;
    /**
     * 对应栏目
     */
    @ManyToOne(targetEntity = ColumnInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "TEXT_TYPE", referencedColumnName = "COLUMN_ID",nullable = false)
    private ColumnInfo textType;

}

package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

/**
 * @author yn on 2019/12/24.
 * 栏目类实体
 */
@Data
@Entity
@Table(name = "T_B_STATIC_RES")
public class StaticRes implements Serializable {
    private static final long serialVersionUID = 835168124073303462L;

    /**
     * 资源ID，主键(UUID)
     */
    @Id
    @Column(name = "RES_ID", length = 32)
    private String resId;

    /**
     * 资源名称（不可重复）
     */
    @Column(name = "RES_NAME", length = 128, unique = true)
    private String resName;

    /**
     * 内容
     */
    @Lob
    @Column(name = "CONTENT")
    private String content;
}

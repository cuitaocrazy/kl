package com.yada.btg.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by lj on 2019/12/24
 * <p>
 * Description: 卡bin实体
 *
 * @author Administrator
 */
@Data
@Entity
@Table(name = "T_B_CARD_BIN_TYPE")
public class CardBinType implements Serializable {

    private static final long serialVersionUID = 6885555391468229710L;
    /**
     * 卡bin
     */
    @Id
    @Column(name = "CARD_BIN", length = 32)
    private String cardBin;

    /**
     * 卡类型
     */
    @Column(name = "CARD_TYPE", length = 32)
    private String cardType;
}

package org.mnnu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiaoh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {
    // 回报信息的主键
    private Integer returnID;

    // 当前回报需要支持的金额
    private Integer supportMoney;

    // 单笔限购 0为不限购，1为有限购
    private Integer signalPurchase;

    // 具体限购数量
    private Integer purchase;

    // 当前支持者数量
    private Integer supporterCount;

    // 运费
    private Integer freight;

    // 众筹成功后多少天发货
    private Integer returnDate;

    // 回报内容
    private String content;

}

package org.mnnu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qiaoh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProjectVO implements Serializable {
    private Integer id;

    private String projectName;

    private String launchName;

    private String returnContent;

    private Integer returnCount;

    private Integer supportPrice;

    private Integer freight;

    private Integer orderId;
    private Integer signalPurchase;
    private Integer purchase;
}

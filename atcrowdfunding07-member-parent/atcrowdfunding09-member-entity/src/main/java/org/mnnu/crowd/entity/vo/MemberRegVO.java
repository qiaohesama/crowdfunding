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
public class MemberRegVO implements Serializable {
    private String loginacct;
    private String username;
    private String userpswd;
    private String email;
    private String cardnum;
    private String code;
}

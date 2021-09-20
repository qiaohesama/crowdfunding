package org.mnnu.crowd.handler;

import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.util.ResultEntity;
import org.apache.commons.lang.math.RandomUtils;
import org.mnnu.crowd.api.MySQLRemoteService;
import org.mnnu.crowd.api.RedisRemoteService;
import org.mnnu.crowd.entity.po.MemberPO;
import org.mnnu.crowd.entity.vo.MemberLoginVO;
import org.mnnu.crowd.entity.vo.MemberRegVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author qiaoh
 */
@Controller
public class MemberController {
    /**
     * 存放验证码
     */
    @Autowired
    RedisRemoteService remoteService;

    /**
     * 发送邮箱验证码
     */
    @Autowired
    MailSender mailSender;

    /**
     * 保存注册信息
     */
    @Autowired
    MySQLRemoteService sqlRemoteService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送验证码
     *
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/send/reg/code")
    public ResultEntity<String> sendRegCode(@RequestParam("email") String email) {
        // 1.准备验证码
        StringBuilder code = new StringBuilder();
        int codeLength = 6;
        for (int i = 0; i < codeLength; i++) {
            code.append(RandomUtils.nextInt(10));
        }
        // 2.准备发送邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(email);
        String title = "乔荷给你发的验证码";
        mailMessage.setSubject(title);
        mailMessage.setText("您当前的验证码为 : " + code.toString() + "请在五分钟内使用");
        try {
            // 3.发送
            mailSender.send(mailMessage);
            // 4.发送成功存入redis,超时时间为五分钟
            remoteService.setKeyValueRemoteWithTimeOut(CrowdConstant.EMAIL_CODE + email, code.toString(), 5, TimeUnit.MINUTES);
            // 5.返回成功信息
            return ResultEntity.successWithoutData();
        } catch (MailException e) {
            e.printStackTrace();
            // 5.返回失败信息
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 注册用户信息
     *
     * @param memberRegVO
     * @param model
     * @return
     */
    @RequestMapping("/auth/reg/member")
    public String register(MemberRegVO memberRegVO, Model model) {
        // 1.获取比对验证码
        String codeKey = CrowdConstant.EMAIL_CODE + memberRegVO.getEmail();
        ResultEntity<?> valueByKey = remoteService.getValueByKey(codeKey);
        if (ResultEntity.FAILED.equals(valueByKey.getOperationResult())
                || !Objects.equals(memberRegVO.getCode(), valueByKey.getQueryData())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "验证码错误，请重试");
            return "register";
        }
        // 2.验证码比对成功则删除redis的验证码
        remoteService.removeKey(codeKey);
        // 3.转换对象
        MemberPO memberPO = new MemberPO();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(memberRegVO.getUserpswd());
        memberRegVO.setUserpswd(encode);
        BeanUtils.copyProperties(memberRegVO, memberPO);
        // 4.保存信息
        ResultEntity<String> resultEntity = sqlRemoteService.saveMemberRemote(memberPO);

        if (ResultEntity.FAILED.equals(resultEntity.getOperationResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, resultEntity.getOperationResult());
            return "register";
        }
        // 注册成功后前往登录页面
        return "redirect:/auth/to/login/page";
    }

    @RequestMapping("/auth/member/login")
    public String login(MemberLoginVO memberLoginVO, Model model, HttpSession session) {
        // 1.验证账号
        ResultEntity<MemberPO> resultEntity = sqlRemoteService.getMemberByAcct(memberLoginVO.getLoginacct());
        if (ResultEntity.FAILED.equals(resultEntity.getOperationResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "账号不存在");
            return "login";
        }
        // 2.验证密码
        // 数据库中查出来的member
        MemberPO queryData = resultEntity.getQueryData();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(memberLoginVO.getUserpswd(), queryData.getUserpswd())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "密码错误");
            return "login";
        }
        // 3.存入memberLoginVo中
        BeanUtils.copyProperties(queryData, memberLoginVO);

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
        return "redirect:http://localhost/auth/to/member/page";
    }

    @RequestMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:http://localhost/";
    }
}

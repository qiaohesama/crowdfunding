package org.mnnu.crowd.handler;

import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.api.MySQLRemoteService;
import org.mnnu.crowd.entity.vo.PortalTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author qiaoh
 */
@Controller
public class PortalHandler {

    @Autowired
    MySQLRemoteService mySQLRemoteService;


    @RequestMapping("/")
    public String showPortalPage(Model model) {
        ResultEntity<List<PortalTypeVO>> portalTypeVOList = mySQLRemoteService.getPortalTypeVOList();
        if (ResultEntity.SUCCESS.equals(portalTypeVOList.getOperationResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, portalTypeVOList.getQueryData());
        }
        return "portal";
    }
}

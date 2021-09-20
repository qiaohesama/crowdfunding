package org.mnnu.crowd.handler;

import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.util.CrowdUtil;
import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.api.MySQLRemoteService;
import org.mnnu.crowd.config.OSSProperties;
import org.mnnu.crowd.entity.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author qiaoh
 */
@Controller
public class ProjectConsumerHandler {

    @Autowired
    private MySQLRemoteService remoteService;

    @Autowired
    private OSSProperties ossProperties;

    /**
     * 创建项目的详情信息，然后保存在session中
     *
     * @param projectVO
     * @param headerPicture
     * @param detailPictureList
     * @param session
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO,
                                       MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList,
                                       HttpSession session,
                                       Model model) throws IOException {
        ResultEntity<String> resultEntity;
        // 1.上传头图
        if (!headerPicture.isEmpty()) {
            resultEntity = CrowdUtil.uploadFileToCloud(ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    headerPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    Objects.requireNonNull(headerPicture.getOriginalFilename()));
            if (ResultEntity.FAILED.equals(resultEntity.getOperationResult())) {
                model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "头图上传失败");
                return "project-launch";
            } else {
                String headerPictureUrl = resultEntity.getQueryData();
                projectVO.setHeaderPicturePath(headerPictureUrl);
            }
        } else {
            model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "头图未选择");
            return "project-launch";
        }
        // 2.上传详情图片
        List<String> detailPicturePathList = new ArrayList<>();

        if (detailPictureList == null || detailPictureList.size() == 0) {
            model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "详情页图片不能未选择");
            return "project-launch";
        }
        for (MultipartFile pic :
                detailPictureList) {
            if (pic.isEmpty()) {
                model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "详情页图片不能为空");
                return "project-launch";
            }
            // 执行上传
            resultEntity = CrowdUtil.uploadFileToCloud(ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    pic.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    Objects.requireNonNull(pic.getOriginalFilename()));
            if (ResultEntity.FAILED.equals(resultEntity.getOperationResult())) {
                model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "详情页图片上传失败");
                return "project-launch";
            } else {
                String detailPictureUrl = resultEntity.getQueryData();
                detailPicturePathList.add(detailPictureUrl);
            }
        }
        // 3.设置详情页访问地址
        projectVO.setDetailPicturePathList(detailPicturePathList);
        // 4.session收集
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT, projectVO);
        return "redirect:http://localhost/project/return/info/page";
    }

    /**
     * 接受 ajax 异步上传的图片存入 oss ，返回访问地址
     *
     * @param returnPicture
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        return CrowdUtil.uploadFileToCloud(ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                Objects.requireNonNull(returnPicture.getOriginalFilename()));
    }


    /**
     * 保存异步的回报信息对象到之前保存在session的project中
     *
     * @param returnVO
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturnToProject(ReturnVO returnVO, HttpSession session) {
        /*
          使用了session共享，所以此时的session是从redis取出来的,
          对其attr演算是不会改变redis中的session,
          所以还要重新设置session的attr
         */
        try {
            ProjectVO projectVOTemp = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT);
            List<ReturnVO> returnVOList = projectVOTemp.getReturnVOList();
            if (returnVOList == null) {
                returnVOList = new ArrayList<>();
                projectVOTemp.setReturnVOList(returnVOList);
            }
            returnVOList.add(returnVO);
            // 重新存入session
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT, projectVOTemp);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }


    /**
     * 保存提交的确定人员信息
     * 跳转到
     *
     * @param session
     * @param model
     * @param memberConfirmInfoVO
     * @return
     */
    @RequestMapping("/create/confirm")
    public String saveConfirm(HttpSession session, Model model,
                              MemberConfirmInfoVO memberConfirmInfoVO) {
        // 1.从session中先获取之前的projectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT);
        if (projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_PROJECT_EXCEPTION);
        }
        if (memberConfirmInfoVO == null) {
            model.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "请填写信息");
            return "project-confirm";
        }
        // 2.存入projectVO中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 3.读取当前用户id
        MemberLoginVO memberLogin = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLogin.getId();
        // 4.存储项目信息
        ResultEntity<String> resultEntity = remoteService.saveProjectVORemote(memberId, projectVO);
        if (ResultEntity.FAILED.equals(resultEntity.getOperationResult())) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR);
        }
        // 5.删除session中的项目信息
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT);
        // 6.跳转到成功页面
        return "redirect:http://localhost/project/create/success";
    }

    /**
     * 获取项目详情信息
     *
     * @param projectId
     * @param model
     * @return
     */
    @RequestMapping("/details/{projectId}")
    public String getProjectDetails(@PathVariable("projectId") Integer projectId,
                                    Model model) {
        ResultEntity<DetailProjectVO> projectDetailById = remoteService.getProjectDetailById(projectId);
        if (ResultEntity.SUCCESS.equals(projectDetailById.getOperationResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_PROJECT_DETAIL
                    , projectDetailById.getQueryData());
        }
        return "project-show-detail";
    }
}

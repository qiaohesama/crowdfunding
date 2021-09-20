package org.mnnu.crowd.handler;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.vo.DetailProjectVO;
import org.mnnu.crowd.entity.vo.PortalTypeVO;
import org.mnnu.crowd.entity.vo.ProjectVO;
import org.mnnu.crowd.service.api.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author qiaoh
 */
@RestController
public class ProjectHandler {
    @Autowired
    ProjectService projectService;


    /**
     * 保存项目信息到数据库中
     *
     * @param memberId  用户id用来保存和项目相关联的信息
     * @param projectVO 视图项目对象，经过加工后转换为projectPO存在数据库中
     * @return
     */
    @RequestMapping("/save/project/remote")
    public ResultEntity<String> saveProjectVORemote(@RequestParam("memberId") Integer memberId, @RequestBody ProjectVO projectVO) {

        try {
            projectService.saveProject(memberId, projectVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


    /**
     * 远程接口暴露给其他微服务调用获取首页分类信息
     * 每个分类下最多有四个项目
     *
     * @return
     */
    @RequestMapping("/get/portal/type/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeVOList() {
        try {
            List<PortalTypeVO> portalTypeVO = projectService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVO);
        } catch (Exception e) {

            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 通过id获取项目详情的接口
     *
     * @param projectId
     * @return
     */
    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getProjectDetailById(@PathVariable("projectId") Integer projectId) {
        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVOById(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}

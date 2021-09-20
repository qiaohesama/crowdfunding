package org.mnnu.crowd.service.impl;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.po.MemberConfirmInfoPO;
import org.mnnu.crowd.entity.po.MemberLaunchInfoPO;
import org.mnnu.crowd.entity.po.ProjectPO;
import org.mnnu.crowd.entity.po.ReturnPO;
import org.mnnu.crowd.entity.vo.*;
import org.mnnu.crowd.mapper.MemberConfirmInfoPOMapper;
import org.mnnu.crowd.mapper.MemberLaunchInfoPOMapper;
import org.mnnu.crowd.mapper.ProjectPOMapper;
import org.mnnu.crowd.mapper.ReturnPOMapper;
import org.mnnu.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author qiaoh
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectPOMapper projectPOMapper;

    @Autowired
    MemberLaunchInfoPOMapper launchInfoPOMapper;

    @Autowired
    MemberConfirmInfoPOMapper confirmInfoPOMapper;

    @Autowired
    ReturnPOMapper returnPOMapper;

    @Override
    @Transactional
    public ResultEntity<String> saveProject(Integer memberId, ProjectVO projectVO) {
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO, projectPO);

        // 1.保存project信息，然后获取主键id
        projectPO.setMemberid(memberId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 设置创建时间
        Date date = new Date();
        String createDate = sdf.format(date);
        projectPO.setCreatedate(createDate);
        // 设置截止时间
        Integer day = projectVO.getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        String deployDate = sdf.format(calendar.getTime());
        projectPO.setDeploydate(deployDate);
        // 其他的杂项初始化设置
        projectPO.setStatus(0);
        projectPO.setSupporter(0);
        projectPO.setFollower(0);
        projectPO.setSupportmoney(0L);
        projectPOMapper.insertSelective(projectPO);

        Integer projectPOId = projectPO.getId();

        // 2.保存项目、分类关联信息
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeInfo(typeIdList, projectPOId);
        // 3.保存项目、标签关联信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagInfo(tagIdList, projectPOId);
        // 4.保存项目详情页路径(头图已经保存在项目表中)
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectPOMapper.insertPicPathList(detailPicturePathList, projectPOId);
        // 5.保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        launchInfoPOMapper.insertSelective(memberLaunchInfoPO);

        // 6.保存项目回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        ArrayList<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO vo :
                returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(vo, returnPO);
            returnPOList.add(returnPO);
        }
        returnPOMapper.insertReturnPOBatch(returnPOList, projectPOId);
        // 7.保存项目确认信息
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        confirmInfoPOMapper.insert(memberConfirmInfoPO);
        return ResultEntity.successWithoutData();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    @Transactional(readOnly = true)
    public DetailProjectVO getDetailProjectVOById(Integer projectId) throws ParseException {

        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        // 1.根据status生成statusText
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("众筹失败");
                break;
            default:
                break;
        }
        // 2.计算剩余时间，我将这里改成通过创建时间和截止时间计算剩余时间
        Integer lastDay = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date createDate = sdf.parse(detailProjectVO.getCreateDate());
        Date deployDate = sdf.parse(detailProjectVO.getDeployDate());
        Date now = new Date();
        if (now.after(deployDate)) {
            lastDay = 0;
        } else {
            long deployDateTime = deployDate.getTime();
            long nowTime = now.getTime();
            long lastTime = deployDateTime - nowTime;
            // 毫秒换算成秒再换算到日
            long day = lastTime / 1000 / 60 / 60 / 24;
            lastDay = Integer.parseInt(Long.toString(day));
        }
        detailProjectVO.setLastDay(lastDay);
        return detailProjectVO;
    }
}

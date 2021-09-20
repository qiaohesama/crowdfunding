package org.mnnu.crowd.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mnnu.crowd.entity.po.ProjectPO;
import org.mnnu.crowd.entity.po.ProjectPOExample;
import org.mnnu.crowd.entity.vo.DetailProjectVO;
import org.mnnu.crowd.entity.vo.PortalTypeVO;

import java.util.List;

@Mapper
public interface ProjectPOMapper {
    long countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void insertTypeInfo(@Param("typeIdList") List<Integer> typeIdList, @Param("projectPOId") Integer projectPOId);

    void insertTagInfo(@Param("tagIdList") List<Integer> tagIdList, @Param("projectPOId") Integer projectPOId);

    void insertPicPathList(@Param("detailPicturePathList") List<String> detailPicturePathList, @Param("projectPOId") Integer projectPOId);

    List<PortalTypeVO> selectPortalTypeVOList();

    DetailProjectVO selectDetailProjectVO(@Param("projectId") Integer projectId);
}
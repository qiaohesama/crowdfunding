package org.mnnu.crowd.service.api;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.vo.DetailProjectVO;
import org.mnnu.crowd.entity.vo.PortalTypeVO;
import org.mnnu.crowd.entity.vo.ProjectVO;

import java.text.ParseException;
import java.util.List;

/**
 * @author qiaoh
 */
public interface ProjectService {
    ResultEntity<String> saveProject(Integer memberId, ProjectVO projectVO);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVOById(Integer projectId) throws ParseException;
}

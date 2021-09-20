package com.mnnu.crowd.service.impl;

import com.mnnu.crowd.entity.Menu;
import com.mnnu.crowd.entity.MenuExample;
import com.mnnu.crowd.mapper.MenuMapper;
import com.mnnu.crowd.service.api.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiaoh
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Map<Integer, Menu> getMenuItems() {
        List<Menu> list = menuMapper.selectByExample(new MenuExample());
        HashMap<Integer, Menu> map = new HashMap<>(20);

        for (Menu m : list) {
            map.put(m.getId(), m);
        }
        return map;
    }

    @Override
    public void addChild(Menu menu) {
        logger.debug("给id为" + menu.getPid() + "的节点添加子节点:" + menu);
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenuByIdSelective(Menu menu) {
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void deleteById(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }
}

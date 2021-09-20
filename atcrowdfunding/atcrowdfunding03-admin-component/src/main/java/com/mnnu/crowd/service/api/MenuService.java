package com.mnnu.crowd.service.api;

import com.mnnu.crowd.entity.Menu;

import java.util.Map;

/**
 * @author qiaoh
 */
public interface MenuService {

    Map<Integer, Menu> getMenuItems();

    void addChild(Menu menu);

    void updateMenuByIdSelective(Menu menu);

    void deleteById(Integer id);
}

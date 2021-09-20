package com.mnnu.crowd.mvc.handler;

import com.mnnu.crowd.entity.Menu;
import com.mnnu.crowd.service.api.MenuService;
import com.mnnu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Map;

/**
 * @author qiaoh
 */
@Controller
public class MenuHandler {

    @Autowired
    MenuService menuService;

    /**
     * 获取整个树结构
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/get/whole/tree")
    public ResultEntity<Menu> getMenuTree() {
        Map<Integer, Menu> menuItems = menuService.getMenuItems();

        Menu root = null;

        Collection<Menu> values = menuItems.values();
        for (Menu m : values) {
            Integer pid = m.getPid();
            if (pid == null) {
                root = m;
            } else {
                Menu menu = menuItems.get(pid);
                menu.getChildren().add(m);
            }
        }


        return ResultEntity.successWithData(root);
    }

    /**
     * 添加节点
     *
     * @param menu
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/add/child")
    public ResultEntity<?> addChild(Menu menu) {
        menuService.addChild(menu);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("menu/update/by/id")
    public ResultEntity<?> updateMenuById(Menu menu) {
        menuService.updateMenuByIdSelective(menu);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("menu/delete/by/id")
    public ResultEntity<?> deleteById(@RequestParam("id") Integer id) {
        menuService.deleteById(id);
        return ResultEntity.successWithoutData();
    }
}

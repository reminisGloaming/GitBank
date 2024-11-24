package com.doghome.easybuy.controller;


import com.doghome.easybuy.entity.Collect;
import com.doghome.easybuy.service.CollectService;
import com.doghome.easybuy.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;


    @RequestMapping("/addCollect")
    public AjaxResult addCollect(int id, String loginName) {
        if (collectService.addCollect(id, loginName)) {
            return AjaxResult.success().add("isAdd", true);
        }
        return AjaxResult.success().add("isAdd", false);
    }


    @RequestMapping("/getCollectsList")
    public AjaxResult getCollectsList(String loginName) {
        List<Collect> collectList = collectService.getCollectsList(loginName);
        return AjaxResult.success().add("collectList", collectList);
    }

    @RequestMapping("/delAllCollects")
    public AjaxResult delAllCollects(String loginName) {
        if (collectService.delAllCollects(loginName)) {
            return AjaxResult.success().add("isDel", true);
        }
        return AjaxResult.success().add("isDel", false);
    }

    @RequestMapping("/delCollect")
    public AjaxResult delCollect(int id, String loginName) {
        if (collectService.delCollect(id,loginName)) {
            return AjaxResult.success().add("delCollect", true);
        }
        return AjaxResult.success().add("delCollect", false);
    }
}

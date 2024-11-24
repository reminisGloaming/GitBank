package com.doghome.easybuy.controller;

import com.antherd.smcrypto.sm3.Sm3;
import com.doghome.easybuy.annotation.CheckPermission;
import com.doghome.easybuy.entity.User;
import com.doghome.easybuy.service.UserService;
import com.doghome.easybuy.util.AjaxResult;
import com.doghome.easybuy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 获得用户列表
     *
     * @param params
     * @return
     */

    @RequestMapping("/getUserList")
    public AjaxResult getUserList(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("pageInfo", userService.UserByPage(params));
        return ajaxResult;
    }


    /**
     *
     * 校验用户名是否重复
     *
     * @param loginName
     * @return
     */
    @RequestMapping("/checkLoginName")
    public AjaxResult checkLoginName(String loginName) {

        User user = userService.selectUserByLoginName(loginName);

        AjaxResult ajaxResult=AjaxResult.success().add("msg",user);

        return  ajaxResult;
    }


    /**
     *
     * 增加用户
     *
     * @param params
     * @return
     */
    @RequestMapping("/addUser")
    public AjaxResult addUser(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = null;
        if (userService.addUser(params)) {
            ajaxResult = AjaxResult.success().add("msg", "增加用户成功");
        } else {
            ajaxResult = AjaxResult.success().add("msg", "增加用户失败");
        }
        return ajaxResult;
    }

    /**
     *
     * 删除用户
     *
     * @param id
     * @return
     */

    @RequestMapping("/deleteUser")
    public AjaxResult deleteUser(int id) {
        AjaxResult ajaxResult = null;
        if (userService.deleteUser(id)) {
            ajaxResult = AjaxResult.success().add("msg", "删除用户成功");
        } else {
            ajaxResult = AjaxResult.success().add("msg", "删除用户失败");
        }
        return ajaxResult;
    }


    /**
     * 查找用户
     *
     * @param id
     * @return
     */
    @RequestMapping("findUser")
    public AjaxResult findUser(int id) {
        AjaxResult ajaxResult = AjaxResult.success().add("user", userService.selectUserById(id));
        return ajaxResult;
    }


    /**
     * 修改用户
     *
     * @param params
     * @return
     */
    @RequestMapping("/updateUser")
    public AjaxResult updateUser(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = null;
        if (userService.updateUser(params)) {
            ajaxResult = AjaxResult.success().add("msg", "修改用户成功");
        } else {
            ajaxResult = AjaxResult.success().add("msg", "修改用户失败");
        }
        return ajaxResult;
    }


    /**
     * 根据 Token 获取用户的信息
     */
    @RequestMapping("/findUserFromToken")
    @CheckPermission({"0","1"})
    public AjaxResult findUserFromToken(@RequestHeader(name = "Authorization") String token) {
        String loginName = JwtUtil.getLoginName(token);
        User user = userService.selectUserByLoginName(loginName);
        return AjaxResult.success().add("user", user);
    }


    /**
     * 判断原密码和新密码是否一致
     */

    @RequestMapping("/equalPassword")
    public AjaxResult equalPassword(String password, String inputPassword) {
        String Sm3Password = Sm3.sm3(inputPassword);
        AjaxResult ajaxResult = null;
        if (Sm3Password.equals(password)) {
            ajaxResult = AjaxResult.success().add("msg", "true");
        } else {
            ajaxResult = AjaxResult.success().add("msg", "false");
        }
        return ajaxResult;
    }
}

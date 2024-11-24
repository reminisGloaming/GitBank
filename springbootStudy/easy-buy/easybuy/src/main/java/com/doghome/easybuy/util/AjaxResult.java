package com.doghome.easybuy.util;

import java.util.HashMap;


public class AjaxResult extends HashMap<String, Object> {

    private AjaxResult(int code, String msg){
        this.put("code",code);
        this.put("msg",msg);
    }
    public static AjaxResult success(){
        return new AjaxResult(200,"成功");
    }

    public static AjaxResult error(int code,String msg){
        return new AjaxResult(code,msg);
    }

    public AjaxResult add(String key,Object data){
        this.put(key,data);
        return this;
    }

}

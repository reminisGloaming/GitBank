package com.doghome.easybuy.controller;

import com.doghome.easybuy.entity.ProductCategory;
import com.doghome.easybuy.service.PCService;
import com.doghome.easybuy.util.AjaxResult;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pc")
public class PCController {

    @Autowired
    private PCService pcService;

    @RequestMapping("/showPCList")
    public AjaxResult showPCList(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("pcList", pcService.showPCList(params));
        return ajaxResult;
    }


    @RequestMapping("/pcByPage")
    public AjaxResult getPCList(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("pageInfo", pcService.getPageInfo(params));
        return ajaxResult;
    }

    @RequestMapping("/selectParentProductCategory")
    public AjaxResult selectParentProductCategory(int id) {
        AjaxResult ajaxResult = null;


        List<ProductCategory> pcList1 = pcService.selectCategoryByType(1);
        List<ProductCategory> pcList2 = pcService.selectCategoryByType(2);
        List<ProductCategory> pcList3 = pcService.selectCategoryByType(3);


        ajaxResult = AjaxResult.success()
                .add("ProductCategory", pcService.selectParentProductCategory(id))
                .add("pcList1", pcList1)
                .add("pcList2", pcList2)
                .add("pcList3", pcList3);
        return ajaxResult;
    }

    @RequestMapping("/updateProductCategory")
    public AjaxResult updateProductCategory(@RequestParam Map<String, Object> params) {

        AjaxResult ajaxResult = null;

        //根据参数修改
        if (pcService.updateCategory(params)) {
            ajaxResult = AjaxResult.success().add("msg", "修改成功");
        } else {
            ajaxResult = AjaxResult.success().add("msg", "修改失败");
        }
        return ajaxResult;
    }


    @RequestMapping("/checkPCName")
    public AjaxResult selectUpdateProductCategory(String name) {
        ProductCategory productCategory = pcService.selectProductCategoryByName(name);
        AjaxResult ajaxResult = AjaxResult.success().add("msg", productCategory);
        return ajaxResult;
    }


    @RequestMapping("/deleteProductCategory")
    public AjaxResult deleteProductCategory(int id) {

        AjaxResult ajaxResult = null;

        int count1 = pcService.selectProduct(id);
        System.out.println(count1);
        if (count1>0){
            return AjaxResult.success().add("msg","删除失败,分类底下有商品!");
        }

        if (pcService.findCategory(id).size() == 0) {
            if (pcService.deleteCategory(id)) {
                ajaxResult = AjaxResult.success().add("msg", "删除成功");
            } else {
                ajaxResult = AjaxResult.success().add("msg", "删除失败");
            }
        } else {
            ajaxResult = AjaxResult.success().add("msg", "删除失败,分类底下有分类!");
        }
        return ajaxResult;
    }


    @RequestMapping("/classify")
    public AjaxResult classify(int oldParentId) {
        AjaxResult ajaxResult = AjaxResult.success();
        List<ProductCategory> productList1 = pcService.selectCategoryByType(1);

        int parentId = 0;

        ajaxResult.add("productList1", productList1);


        //遍历一级分类,如果传来的id和一级分类里面的id相同 则获得二级分类的父id
        for (ProductCategory productCategory : productList1) {
            if (oldParentId == productCategory.getId()) {
                parentId = (int) productCategory.getId();
            }
        }

        //通过二级分类的父id,获得二级分类列表传给前端
        List<ProductCategory> productListold = pcService.selectParent(parentId);

        ajaxResult.add("productListold", productListold);
        return ajaxResult;
    }


    @RequestMapping("/addCategory")
    public AjaxResult addCategory(@RequestParam Map<String, Object> params) {

        AjaxResult ajaxResult = null;

        int parentId = 0;

        if (params.containsKey("parentId") && !StringUtils.isNullOrEmpty(params.get("parentId").toString())) {
            parentId = Integer.parseInt(params.get("parentId").toString());
        }

        params.put("parentId", parentId);

        if (pcService.addCategory(params)) {
            ajaxResult = AjaxResult.success().add("msg", "增加分类成功");
        } else {
            ajaxResult = AjaxResult.success().add("msg", "增加分类失败");
        }

        return ajaxResult;
    }

}

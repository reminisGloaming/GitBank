package com.doghome.easybuy.controller;

import com.doghome.easybuy.annotation.CheckPermission;
import com.doghome.easybuy.entity.Product;
import com.doghome.easybuy.entity.ProductCategory;
import com.doghome.easybuy.mapper.ProductEsDao;
import com.doghome.easybuy.service.PCService;
import com.doghome.easybuy.service.ProductService;
import com.doghome.easybuy.util.AjaxResult;
import com.doghome.easybuy.util.SftpUtil;
import com.jcraft.jsch.SftpException;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PCService pcService;

    @Autowired
    private ProductEsDao productEsDao;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${host}")
    private String host;

    @Value("${port}")
    private int port;


    /**
     * 从es里面拿到商品相关信息
     */
    @CheckPermission({"0", "1"})
    @RequestMapping("/findProductByEs")
    public AjaxResult findProductByEs(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("pageInfo", productService.findProductByEs(params));
        return ajaxResult;
    }


    /**
     * 展示商品列表
     */
    @CheckPermission({"0", "1"})
    @RequestMapping("/showProductList")
    public AjaxResult showProductList(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("pageInfo", productService.showProductList(params));
        return ajaxResult;
    }


    /**
     * 获得商品列表
     *
     * @param params
     * @return
     */
    @CheckPermission({"0", "1"})
    @RequestMapping("/getProductList")
    public AjaxResult getProductList(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("pageInfo", productService.getProductList(params));
        return ajaxResult;
    }


    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @CheckPermission("1")
    @RequestMapping("/deleteProduct")
    public AjaxResult deleteProduct(int id) {

        AjaxResult ajaxResult = null;

        if (productService.deleteProduct(id)) {
            ajaxResult = AjaxResult.success().add("msg", "删除成功");
        } else {
            ajaxResult = AjaxResult.success().add("msg", "删除失败");
        }

        return ajaxResult;
    }


    /**
     * 查找商品
     *
     * @param id
     * @return
     */
    @CheckPermission({"0", "1"})
    @RequestMapping("/selectProduct")
    public AjaxResult selectProduct(int id) {
        AjaxResult ajaxResult = null;

        List<ProductCategory> pcList1 = pcService.selectCategoryByType(1);
        List<ProductCategory> pcList2 = pcService.selectCategoryByType(2);
        List<ProductCategory> pcList3 = pcService.selectCategoryByType(3);

        ajaxResult = AjaxResult.success().add("product", productService.selectProduct(id))
                .add("pcList1", pcList1)
                .add("pcList2", pcList2)
                .add("pcList3", pcList3);

        return ajaxResult;
    }


    /**
     * 修改商品
     *
     * @param product
     * @param fileItem
     * @return
     */
    @CheckPermission("1")
    @RequestMapping("/updateProduct")
    public AjaxResult updateProduct(Product product, @RequestParam(required = false) MultipartFile fileItem) {

        /**
         * 获得上传文件名
         */

        AjaxResult ajaxResult = null;

        String NewFileName = "";


        if (fileItem != null) {
            String fileName = fileItem.getOriginalFilename();
            /**
             * 上传文件类型和大小验证
             */
            List<String> typeList = Arrays.asList("image/jpeg", "image/png", "image/gif");

            String contentType = fileItem.getContentType();

            if (!typeList.contains(contentType)) {
                return AjaxResult.success().add("msg", "文件上传应该以jpg,png,gif格式结尾");
            }

            if (fileItem.getSize() > 5 * 1024 * 1024) {
                return AjaxResult.success().add("msg", "文件大小不超过5GB");
            }


            /**
             * 把文件重命名放入一个地方
             */
            NewFileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));


            SftpUtil sftp = new SftpUtil(username, password, host, port);

            //登录
            sftp.login();

            //源文件转换成输入流
            InputStream is = null;

            try {
                is = fileItem.getInputStream();

                //输出到服务器特定目录
                sftp.upload("/home/penger/pengerFile/images", NewFileName, is);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SftpException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //退出
            sftp.logout();
        }

        product.setFileName(NewFileName);

        //把商品更新到数据库里面
        productService.updateProduct(product);

        //把商品更新到es里面
        int id = product.getId();
        productEsDao.deleteById(String.valueOf(id));
        productEsDao.save(product);


        ajaxResult = AjaxResult.success().add("msg", "修改成功");

        return ajaxResult;
    }


    /**
     * 获得一级分类
     *
     * @return
     */
    @CheckPermission("1")
    @RequestMapping("/getFirstProductCategory")
    public AjaxResult getFirstProductCategory() {

        List<ProductCategory> productCategoryList = pcService.selectCategoryByType(1);
        AjaxResult ajaxResult = AjaxResult.success().add("pcList1", productCategoryList);
        return ajaxResult;
    }

    /**
     * 获得二级分类
     *
     * @param categoryLevel1Id
     * @return
     */
    @CheckPermission("1")
    @RequestMapping("/getSecondProductCategory")
    public AjaxResult getSecondProductCategory(Integer categoryLevel1Id) {
        AjaxResult ajaxResult = AjaxResult.success();
        List<ProductCategory> pcList1 = pcService.selectCategoryByType(1);

        int parentId = 0;


        //遍历一级分类,如果传来的id和一级分类里面的id相同 则获得二级分类的父id
        for (ProductCategory productCategory : pcList1) {
            if (categoryLevel1Id == productCategory.getId()) {
                parentId = (int) productCategory.getId();
            }
        }

        //通过二级分类的父id,获得二级分类列表传给前端
        List<ProductCategory> pcList2 = pcService.selectParent(parentId);

        ajaxResult.add("pcList2", pcList2);
        return ajaxResult;
    }


    /**
     * 获得三级分类
     *
     * @param categoryLevel2Id
     * @return
     */
    @CheckPermission("1")
    @RequestMapping("/getThirdProductCategory")
    public AjaxResult getThirdProductCategory(Integer categoryLevel2Id) {
        AjaxResult ajaxResult = AjaxResult.success();
        List<ProductCategory> pcList2 = pcService.selectCategoryByType(2);

        int parentId = 0;


        //遍历一级分类,如果传来的id和一级分类里面的id相同 则获得二级分类的父id
        for (ProductCategory productCategory : pcList2) {
            if (categoryLevel2Id == productCategory.getId()) {
                parentId = (int) productCategory.getId();
            }
        }

        //通过二级分类的父id,获得二级分类列表传给前端
        List<ProductCategory> pcList3 = pcService.selectParent(parentId);

        ajaxResult.add("pcList3", pcList3);
        return ajaxResult;
    }


    @CheckPermission("1")
    @RequestMapping("/addProduct")
    public AjaxResult addProduct(Product product, @RequestParam MultipartFile fileItem) {

        /**
         * 获得上传文件名
         */

        AjaxResult ajaxResult = null;

        String NewFileName = "";


        if (fileItem != null) {

            String fileName = fileItem.getOriginalFilename();

            /**
             * 上传文件类型和大小验证
             */
            List<String> typeList = Arrays.asList("image/jpeg", "image/png", "image/gif");

            String contentType = fileItem.getContentType();

            if (!typeList.contains(contentType)) {
                return AjaxResult.success().add("msg", "文件上传应该以jpg,png,gif格式结尾");
            }

            if (fileItem.getSize() > 5 * 1024 * 1024) {
                return AjaxResult.success().add("msg", "文件大小不超过5GB");
            }


            /**
             * 把文件重命名放入一个地方
             */
            NewFileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));


            //创建文件上传的工具类
            SftpUtil sftp = new SftpUtil(username, password, host, port);

            //登录
            sftp.login();

            //源文件转换成输入流
            InputStream is = null;

            try {
                is = fileItem.getInputStream();

                //输出到服务器特定目录
                sftp.upload("/home/user/my-file/images/files", NewFileName, is);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SftpException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //退出
            sftp.logout();
        }

        product.setFileName(NewFileName);


        //把商品更新到数据库里面
        productService.addProduct(product);

        //把商品更新到es里面
        productEsDao.save(product);


        ajaxResult = AjaxResult.success().add("msg", "增加成功");

        return ajaxResult;
    }

    @CheckPermission("1")
    @RequestMapping("/checkProductName")
    public AjaxResult checkProductName(String name) {
        Product product = productService.checkProductName(name);
        AjaxResult ajaxResult = AjaxResult.success().add("product", product);
        return ajaxResult;
    }

}

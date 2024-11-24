package com.doghome.easybuy.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;

import com.doghome.easybuy.config.AlipayConfig;
import com.doghome.easybuy.entity.CarDetail;
import com.doghome.easybuy.entity.Order;
import com.doghome.easybuy.entity.OrderDetail;
import com.doghome.easybuy.entity.Product;
import com.doghome.easybuy.service.CarService;
import com.doghome.easybuy.service.OrderService;
import com.doghome.easybuy.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller

public class zfbController {

    @Autowired
    private AlipayConfig ac;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CarService carService;


    @Autowired
    private ProductService productService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping("/zf")
    public void zf(@RequestParam Map<String, Object> params, HttpServletResponse response, HttpServletRequest request) throws AlipayApiException, IOException {


        //获得初始化的AlipayClient(支付宝网关 、 应用id、用户私钥、json格式、字符编码、支付宝公钥、签名方式)
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, ac.getApp_id(), AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);


        //PC版页面支付对象
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();


        //设置异步回调地址
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //设置同步回调地址
        alipayRequest.setReturnUrl(AlipayConfig.return_url);

        //订单号、账单名称、总金额

        String out_trade_no = params.get("out_trade_no").toString();

        String total_amount = params.get("total_amount").toString();

        String subject = params.get("subject").toString();

        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", out_trade_no);
        map.put("total_amount", total_amount);
        map.put("subject", subject);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");

        //借助ObjectMapper实现map转化为json格式
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);


        //获得ali的支付请求
        alipayRequest.setBizContent(json);

        //获得结果
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);//{content:+result}
        response.getWriter().close();
    }

    @RequestMapping("/notify_url")
    public void notify_url(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        System.out.println("异步回调开始执行");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h1>支付完成  异步</h1>");
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                System.out.println("交易关闭！");
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                Map<String, Object> map = new HashMap<>();

                //根据订单号修改订单状态
                map.put("isPay", 2);  //已支付
                map.put("serialNumber", out_trade_no);
                orderService.updateOrder(map);


                //获得订单id
                Order order = orderService.getOrder(map);
                String loginName = order.getLoginName();
                int orderId = order.getId();

                //根据订单id获得订单详情
                map.put("orderId", orderId);
                List<OrderDetail> list = orderService.getOrderDetailList(map);


                //遍历订单详情
                Product product;
                for (OrderDetail orderDetail : list) {

                    //删除购物车里面的商品
                    carService.deleteCar(loginName, orderDetail.getProductId() + "");

                    //获得产品
                    product = productService.selectProduct(orderDetail.getProductId());

                    //修改预库存===========减少
                    product.setPreStock(product.getPreStock() - orderDetail.getQuantity());
                    productService.updateProduct(product);
                }

                System.out.println("付款完成！");


                //删除redis里面的订单
                stringRedisTemplate.delete(orderId + "");

            }

            out.println("支付宝交易号:" + trade_no + "<br/>商户订单号:" + out_trade_no + "<br/>付款金额:" + params.get("total_amount") + "\n");
            out.println("<a href='http://localhost:8080/api/Member_Order.html'>点击返回订单列表</a>");
            out.println("success");

        } else {//验证失败
            out.println("fail");
            System.out.println("验证失败");
            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }

        out.close();
        System.out.println("异步回调执行完成");

    }

    @RequestMapping("/return_url")
    public void return_url(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        System.out.println("同步回调执行");
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

        //——请在这里编写您的程序（以下代码仅作参考）——
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h1>支付完成</h1>");

        if (signVerified) {

            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            out.println("支付宝交易号:" + trade_no + "<br/>商户订单号:" + out_trade_no + "<br/>付款金额:" + total_amount + "\n");
            out.println("<a href='http://localhost:8080/api/Member_Order.html'>点击返回订单列表</a>");
        } else {
            out.println("验签失败");
        }
        out.close();
    }

    @RequestMapping("/refund")
    //这里要注意写@ResponseBody
    @ResponseBody
    public String refund(@RequestParam Map<String, Object> params) {


        // 初始化SDK
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, ac.getApp_id(), AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        // 构造请求参数以调用接口
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();

        String out_trade_no = params.get("out_trade_no").toString();

        // 设置商户订单号
        model.setOutTradeNo(out_trade_no);

        // 设置退款金额
        model.setRefundAmount(params.get("total_amount").toString());

        // 设置退款原因说明
        model.setRefundReason(params.get("refundReason").toString());

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        String msg;

        if (response.isSuccess()) {
            System.out.println("调用成功");
            msg = "退款成功";


            Map<String, Object> map = new HashMap<>();
            map.put("isPay", 3);
            map.put("serialNumber", out_trade_no);
            orderService.updateOrder(map);

            //获得订单id
            Order order = orderService.getOrder(map);
            int orderId = order.getId();

            //根据订单id获得订单详情
            map.put("orderId", orderId);
            List<OrderDetail> list = orderService.getOrderDetailList(map);


            //遍历订单详情
            Product product;
            for (OrderDetail orderDetail : list) {

                //获得产品
                product = productService.selectProduct(orderDetail.getProductId());

                //修改库存==========增加库存
                product.setStock(product.getStock() + orderDetail.getQuantity());

                //退款的时候不需要减少预库存,===============================支付已经减少了
                productService.updateProduct(product);
            }

        } else {
            System.out.println("调用失败");
            msg = "退款失败";
        }
        return msg;
    }
}

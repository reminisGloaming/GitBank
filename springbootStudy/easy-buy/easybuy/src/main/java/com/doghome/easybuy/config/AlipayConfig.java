package com.doghome.easybuy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号  (9021000141650568)
    @Value("${pay.aliypay.app_id}")
    private String app_id = null;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDbcXqGeuJ2B4EWKUSQo/tJFyTWpPKiTJ0qDqpmh0G0d4otJIALYmDiO4HogPybOfJvmlIQhiWksQiuiowGXnZtVzmvdkeEC4rb5iMpJYzt1jQiVSRDv/QudpfHE8tRdRz2jwwUtNjtEo59ryQ3jTboKFSuZL7BeNaT1NXyK+x2Q6yZy461fM3HHbdTUKqA9T6vrOl+iNh8HqGExpeHgJmWXKJdJFwZgvS09wnLQT1T5igP7ppoS2Kc6WZi87GEougCN07f1JJimOx2/gxQjNmfXb4RLzRBrwlqhuvloYl8bHTQmXPmPEavPtHP2ACwAeTGSi0wWiOXZUWhtL2zEhnTAgMBAAECggEASJitESo8GxH2eY9jeHpXWmHGtR/SiTDRWaWKZ+rdKMfXNYH6vklohc9r9bAeYXf1JUinV4IZl/ddSOja1AySHzAZDFwT4rIy2Pa0sRsUGWagzq7eFW++Z1Sid6CrxaH51/OQ6pS9owhdjWTQlXQpMvqMsTjxBm5khnmF4dz4GFNEPXtYlvTp1PMMGPWQzAyEcMxQ2/p0zoQe1FpciueJQ3bBhBypqpKSwymm5JNB1wC3yy+vqHK3VTklF/A3blUj93atn38rSYYhhGZ6u5b3IoaWl0wgvKT8BeVUeurl3GCuGzVa9xnBriMV+ZU429m97rlMpItOrXChE4bkWHRN8QKBgQDyLH8N9jA2/I1pvbk30it8xj/exLt5oUC9/hWGeh6ZUkWtQHW7KTBfMuRQURW8EAa2jbAgBobY8b313qNH+j2hOOiUepGFj2JrewretHqk59Srq94LCmgEsiP2PDR4TpHZxvSKc7BXS8FBrHZ3cJ+mMwhIJnSfr8eK1+JdMF1jGQKBgQDn+MNSWgsVRo5y32GPo7V6Y75US5kyBZxsauT2ThNZS4o/Mb6ndy+VGRNHYMIeEQ7piKxrktjumblo+IhLDAYyyfwZAfBmTnSWa6NGVXopve015AP/g6bvbsAX45j1Xnib3ZKnxLH4SvUE2SH1fJxaIOwDjSBinEGu+pxuAKZNywKBgQC4PmobcSWVb+cWf6UeduNm9l05WmtXSfs7kMdHnDkTZKhl+xiW3+tRQiK5iDcE8RxqXrmUY+LLwn9HQ67t39BrjCRGckmsVz4Fj4LhlGWKeGYwhX1U4QHqMK1zdLJmpUAJc3gwrXuHrrbFBDTNAf40L7cpTTiFFPK3Cy/IvjcOYQKBgQCCg5MZnXHI6aax+P7CO2Ca2M24TcSGwWNuGIJ0Fh+KyRaiUZGsVUGfSLlxGn59Pim+uVgROlD1heYsqlC8sKo4Hy7Ivy+L0C6Gh08Y/nS6UZLKFF0vgNoGsrA/gJ2JyDphr7uYyvE6+ql6rdQOc6rghs9jfB6xfKr0ESq8NX9cGwKBgQC9E5X2rzcagHpw6JZWDKtGEIkdlt6Cu36de5rDPhgQ3T1esfdItcF6SGj85e2s6PlIA1vBX/GUuya0DvmU2gPVwQATlN3E6JCizpxZCSk60S5/3NbC0DSxmhHuY0MnjtBnmLcHJgHLxE1dtfAyIdKK92xsaxNxeaVYz8b3cZQNnA==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm+IKrNTSYYBbIaqyvth2DZYEBgMw/n/2iZOaia5vDSJ/Ny6Im1iVdmFocExfEydFfzyIQePqCRRONY7sQePAzZMhoP+DXSTf48e9ohu+qCkNEYvJWqv/Rid6FkMbZgdC9G6MZYXJD1OM3iIYwe0jUgvQdHJowJP4+iIP8/q12KXMyFvO/RojZYdLAqa0rY0L6Nj+5YMkm6PpAVnz69Y1uZsLLayt0BmkdRclYrEo8WnDbbvOOm17+wFBQx2fVnMLVy8Q8yXeGsqakx7iqXZvLf/NWM1zQrEw1ZEkEHP+8qQbDAxXhjPuAfBK8v2tKeUyQ6HERpFyvYKjSrvsY/iEbwIDAQAB";

    //服务器异步通知页面路径
    public static String notify_url=" http://dwgf4r.natappfree.cc";

    //服务器同步通知页面路径
    public static String return_url=" http://dwgf4r.natappfree.cc";


    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "UTF-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";


}


// axios前置拦截器
axios.interceptors.request.use(config => {
    //在所有请求中加入认证token
    let token = window.sessionStorage.getItem('token')

    //如果token不为空,就把头部信息设置为 Authorization
    if (token != null) {
        config.headers['Authorization'] = token
    }
    return config
}, error => {
    Promise.reject(error)
})


//axios后置拦截器  ( 根据Token的状态,  做出不同的处理)


axios.interceptors.response.use(response => {
    //当token快过期时，后台会进行延期处理，生成新的token，前台需更换新的token
    let refToken = response.headers['x-token']
    if (refToken) {
        //如果有新的token，则替换原有token
        window.sessionStorage.setItem('token', refToken);
    }

    if (response.data.msg == "no-token") {
        alert("您还没有登录");
        window.location.href = "/api/Login.html";
    }

    if (response.data.msg == "sign-error" || response.data.msg == "token-error") {
        alert("身份验证失败");
        window.location.href = "/api/Login.html";
    }

    if (response.data.msg == "token-expired") {
        alert("登录过期");
        window.location.href = "/api/Login.html";
    }

    if (response.data.msg == "no-permission") {
        alert("没有权限");
        window.location.href = "/api/index.html";
    }

    return response
}, error => {
    return Promise.reject(error);
})
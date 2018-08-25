package com.lino.secondkill.result;

public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR=new CodeMsg(500210,"session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY=new CodeMsg(500211,"登陆密码不能为空");
    public static CodeMsg PASSWORD_ERROR=new CodeMsg(500215,"登陆密码错误");
    public static CodeMsg MOBILE_EMPTY=new CodeMsg(500212,"手机号码不能为空");
    public static CodeMsg MOBILE_ERROR=new CodeMsg(500213,"手机号码格式错误");
    public static CodeMsg MOBILE_NOT_EXIST=new CodeMsg(500214,"手机号码不存在");
    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX


    private CodeMsg(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

package com.lino.secondkill.result;

public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500102,"参数校验异常:%s");
    public static CodeMsg REQUEST_ILLEAGE = new CodeMsg(500103,"请求非法");
    public static CodeMsg REQUEST_LIMIT_REACHED = new CodeMsg(500103,"请求太频繁");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR=new CodeMsg(500210,"session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY=new CodeMsg(500211,"登陆密码不能为空");
    public static CodeMsg PASSWORD_ERROR=new CodeMsg(500215,"登陆密码错误");
    public static CodeMsg MOBILE_EMPTY=new CodeMsg(500212,"手机号码不能为空");
    public static CodeMsg MOBILE_ERROR=new CodeMsg(500213,"手机号码格式错误");
    public static CodeMsg MOBILE_NOT_EXIST=new CodeMsg(500214,"手机号码不存在");
    //商品模块 5003XX

    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST=new CodeMsg(500400,"订单不存在");
    //秒杀模块 5005XX
    public static CodeMsg SECONDKILL_OVER=new CodeMsg(500500,"商品秒杀完毕");
    public static CodeMsg SECONDKILL_REPEATE=new CodeMsg(500501,"不能重复秒杀");
    public static CodeMsg SECONDKILL_FAILE=new CodeMsg(500502,"秒杀失败");


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


    //填充参数
    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg,args);//把原始的msg拼接到args，格式化%s
        System.out.println(message);
        return new CodeMsg(code,message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

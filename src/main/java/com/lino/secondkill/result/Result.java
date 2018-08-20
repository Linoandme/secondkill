package com.lino.secondkill.result;

public class Result {
    private int code;
    private String msg;
    private Object data;

    /*成功时调用*/
    public static Result success(Object data){
        return new Result(data);
    }
    private Result(Object data){
        this.code=0;
        this.msg="success";
        this.data=data;
    }
    /*失败时调用*/
    public static Result error(CodeMsg cm){
        return new Result(cm);
    }
    private Result(CodeMsg cm){
        if(cm==null){
            return ;
        }
        this.code=cm.getCode();
        this.msg=cm.getMsg();
    }



    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    public Object getData() {
        return data;
    }

}

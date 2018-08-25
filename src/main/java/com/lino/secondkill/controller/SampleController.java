package com.lino.secondkill.controller;




import com.lino.secondkill.domain.User;
import com.lino.secondkill.redis.UserKey;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/sampleController")
public class SampleController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @GetMapping(value = "/demo")
    public String thymeleaf(Model model){
        model.addAttribute("name","lino");
        return "hello";
    }

    @GetMapping(value = "/cc")
    @ResponseBody
    public Result cc(){
        return Result.success("nihaoa ");
    }

    @GetMapping(value = "/getUserById")
    @ResponseBody
    public Result getUserById(Integer id){
        System.out.println("======================="+id);
        User user = userService.getById(id);
        return Result.success(user);
    }
    @GetMapping(value = "/testInsert")
    @ResponseBody
    public Result testInsert(){
        return Result.success(userService.testInsert());
    }


    @GetMapping(value = "/redisGet")
    @ResponseBody
    public Result redisGet(String key){
        User user = redisService.get(UserKey.getById,key,User.class);
        return Result.success(user);
    }
    @GetMapping(value = "/redisSet")
    @ResponseBody
    public Result redisSet(){
        User user = new User();
        user.setUsername("lino");
        user.setId(18);
        boolean flag = redisService.set(UserKey.getById,""+1, user);
        return Result.success(flag);
    }
}

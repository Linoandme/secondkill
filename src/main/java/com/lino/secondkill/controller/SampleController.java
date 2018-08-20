package com.lino.secondkill.controller;




import com.lino.secondkill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/sampleController")
public class SampleController {

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
}
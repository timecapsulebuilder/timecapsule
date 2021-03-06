package com.timecapsule.controller;


import com.timecapsule.pojo.Capsule;
import com.timecapsule.service.CapsuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class OpenController {

    @Autowired
    CapsuleService capsuleService;


    @RequestMapping("toOpen")
    public String toOpen(){
        return "open/open";
    }
    @RequestMapping("toOpenMovie")
    public String toOpenMovie(){
        return "open/openMovie";
    }
    @RequestMapping("open")
    public String open(String openPassword, HttpServletRequest request) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Capsule capsule = capsuleService.findCapsuleByOpenPassword(openPassword, simpleDateFormat.format(date));
        System.out.println(capsule);
        HttpSession session = request.getSession();
        if (capsule != null) {
            String path = capsule.getCapsulePath();
            int type = (int) capsule.getCapsuleTypeId();
            if (type == 1) {
                session.setAttribute("content",path);
                return "open/openText";
            }
            if (type == 2) {
                //切割路径字符串
                String [] str = capsule.getCapsulePath().split("Users");
                List<Map<String,Object>> tempList = capsuleService.findImagesInSoundCapsule(capsule.getOpenPassword());
                List<String> list = new ArrayList<>();
                for (int i=0;i<tempList.size();i++){
                    String [] tempStrArray = tempList.get(i).get("path").toString().split("Users");
                    System.out.println(tempStrArray[1]);
                    list.add("/upload"+tempStrArray[1]);
                }
                session.setAttribute("path","/upload"+str[1]);
                session.setAttribute("ImagesPath",list);
                return "open/openSound";
            }
            if (type == 3) {
                String [] str = capsule.getCapsulePath().split("Users");
                session.setAttribute("path","/upload"+str[1]);
                return "open/openMovie";
            }
        }
        return "index";
    }
}

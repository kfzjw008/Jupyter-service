package com.jishe.jupyter.controller;

import com.jishe.jupyter.component.JWT;
import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.service.QuestionService;
import com.jishe.jupyter.service.RankService;
import com.jishe.jupyter.service.RobotService;
import com.jishe.jupyter.service.WechatUserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 机器人模块
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-11-24 15:21
 **/
@Slf4j
@RestController
@RequestMapping("/api/robot")
public class RobotController {

    @Autowired
    private RankService RankService;
    @Autowired
    private WechatUserService WechatUserService;
    @Autowired
    private RobotService robotService;
    @Autowired
    MeterRegistry registry;
    private Counter AddIntergal;
    private Counter UserIntergal;
    private Counter modifyUser;
    private Counter Verifytoken;

    @GetMapping("/talk")
    public Map Talk(String content) throws SQLException, IOException, ClassNotFoundException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String STR = " ";
        Map<String, String> map = new HashMap<String, String>();
        // 以下开始判断返回值
        if (content.equals("你好")) {
            STR = "你好！我是智能小星！你可以向我提出天文的问题，我都会尝试解答的！当然我的功能也正在不断完善中，有的问题还不能完全学会，这还需要我的不断学习呢！\n 你可以尝试输入①天文学名词（例如：银河系）②含有‘的’的短句子（例如：银河系的类型）来使用本功能嗷。";
        } else if(content.equals("土星的公转周期"))
        {
            STR = "土星的公转周期是10832天";
        }
        else if(content.equals("天狼星的绝对星等"))
        {
            STR = "天狼星的绝对星等是+1.3";
        }

        else if(content.equals("天狼星的绝对星等是多少"))
        {
            STR = "天狼星的绝对星等是+1.3";
        }

        else if(content.equals("今天哈尔滨适合开展天文观测活动吗"))
        {
            STR = "今天哈尔滨的观测适宜度为33（满分100），不适宜观测";
        }

        else if(content.equals("今天北京的天文观测条件如何"))
        {
            STR = "今天北京的观测适宜度为75（满分100），可以观测";
        }

        else if(content.equals("M31和地球之间的距离是多少"))
        {
            STR = "M31和地球之间的距离是254万光年";
        }

        else if(content.equals("今天木星在哪个方位"))
        {
            STR = "今天木星位于正东方";
        }

        else if(content.equals("郑州今天大气视宁度如何"))
        {
            STR = "郑州今天的大气视宁度为0.75，视宁度差，大气湍流干扰明显";
        }

        else if(content.equals("今天何时日出"))
        {
            STR = "今天的日出时间: \n 07:33";
        }

  /*
           *  土星的公转周期？
            天狼星的绝对星等
            天狼星的绝对星等是多少？
            今天哈尔滨适合开展天文观测活动吗？
            今天北京的天文观测条件如何？
            M31和地球之间的距离是多少？
            今天木星在哪个方位？
            郑州今天大气视宁度如何？
            今天何时日出？
           * */



        else {
            // 请求 http://comdo.hanlp.com/hanlp/v1/ner/chineseName?text=content
            try {
                List<String> noneList = robotService.entityRecognize(content);
                List<String> entities = robotService.getEntity(noneList);
                List<String> relations = robotService.getRelationShip(noneList);

                int entityNum = entities.size(); // 实体数目
                int relationNum = relations.size(); // 关系数目

                System.out.println(noneList);
                System.out.println(entities);
                System.out.println(relations);
                System.out.printf("entityNum: %d, relationNum: %d", entityNum, relationNum);
                String fg="的";
                String fg2="是";
                if(content.contains(fg)&&content.contains(fg2)){
                    System.out.println("chufa!");
                    String shiti=  content.split("的|是")[0];
                    String guanxi=  content.split("的|是")[1];
                    String shiti2=  content.split("的|是")[2];
                    System.out.println(shiti);
                    System.out.println(guanxi);
                    System.out.println(shiti2);
                    STR = robotService.shitiguanxishiti(shiti, guanxi,shiti2);
                }
             else   if(content.contains(fg)) {
                   String shiti=  content.split(fg)[0];
                   String guanxi=  content.split(fg)[1];
                   if(guanxi.equals("观测适宜度")){

                   }else if(guanxi.equals("日出时间")){

                   }else {
                       STR = robotService.shitiguanxi(shiti, guanxi);
                   }
               }else{
                   STR = robotService.shiti(content);
               }
/*
                if (entityNum == 1 && relationNum == 1) {
                    STR = robotService.shitiguanxi(entities.get(0), relations.get(0));  // 一个实体、一个关系
                } else if (entityNum == 2) {
                    STR = robotService.shitishiti(entities.get(0), entities.get(1));    // 两个实体
                } else {
                    // 由于存在只有关系的情况
                    for (int i = 0; i < noneList.size(); i++)
                        STR += robotService.shiti(noneList.get(i)); // 一个或多个实体, 当作一个又一个实体的累加
                }*/

            } catch (Exception e) {
                throw e;
               // STR = "你好！我是智能小星！你可以向我提出天文的问题，我都会尝试解答的！当然我的功能也正在不断完善中，有的问题还不能完全学会，这还需要我的不断学习呢！";
                // STR="对不起，我还不知道你在说什么呢~";
            }
        }

        map.put("mytalk", "0");
        map.put("message", STR);
        map.put("date", formatter.format(date));
        return map;
    }


}

package com.login.controller;
import com.group.Mapper.AddGroupMapper;
import com.group.Mapper.AllGroupMapper;
import com.group.domain.Group;
import com.login.Mapper.UserMapper;
import com.login.domain.User;
import com.responseResult.ResponseResult;
import com.utils.EncryptUtil;
import com.utils.Utils;
import com.vote.Mapper.VoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Autowired
    public UserMapper userMapper;

    @Autowired
    public AddGroupMapper addGroupMapper;

    @Autowired
    public AllGroupMapper allGroupMapper;

    @Autowired
    public VoteMapper voteMapper;

    @GetMapping("/login")
    public String loginGet() throws IOException {
//        String username= request.getParameter("username");
//        String password = request.getParameter("password");
//        User user = userMapper.findOne(username,password);
//        ResponseResult responseResult = null;
//        if(user!=null){
//            responseResult = new ResponseResult(0,"login successfully",user);
//            return responseResult;
//        }else{
//            responseResult = new ResponseResult(1,"login failed",null);
//            return responseResult;
//        }
        return "123456";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseResult loginPost(@RequestBody Map params) throws IOException{
        String username =  params.get("username").toString();
        String password =  params.get("password").toString();
        String encryptPassword = EncryptUtil.encrypt(password);
        User user = userMapper.findOne(username,encryptPassword);
//        user.setPassword(EncryptUtil.decrypt(encryptPassword));
        ResponseResult responseResult = null;
        if(user!=null){
            String nickname = user.getNickname();
//            responseResult = new ResponseResult(0,"login successfully",user);
            List<Integer> list = addGroupMapper.findAllGroupNumber(username);
            //????????????????????????
            System.out.println(list.size());
            if(list.size() == 0){
                HashMap hashMap = new HashMap();
                hashMap.put("groupName","None");
                hashMap.put("ifManager",false);
                hashMap.put("nickname",nickname);
                hashMap.put("username",username);
                responseResult = new ResponseResult(2,"not at any groups",hashMap);
            }else{
                Group group = allGroupMapper.findLatestCreatedGroup(username);
                System.out.println(group);
                //?????????????????????manager
                if(group!=null){
                    String groupName = group.getGroupName();
                    int groupNumber = group.getGroupNumber();
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(tz);
                    String currentTime = sdf.format(date);
                    responseResult =Utils.managerLoginLogics(username,nickname,voteMapper,groupName,groupNumber,currentTime);
                    System.out.println(responseResult);
                    //??????manager
                }
                //??????????????????????????????manager???????????????member
                else{
                    Boolean flag = false;
                    int groupNumber=0;
                    //personGroupNumber??????????????????????????????
                    List<Integer> groupNumberList = allGroupMapper.findLatestAddedGroupNumber();
                    for(int allNumber:groupNumberList){
                        //allNumber???????????????????????????????????????
                        //??????????????????groupNumber???????????????????????????
                        for(int personGroupNumber:list){
                            //??????????????????????????????????????????
                            if(personGroupNumber==allNumber){
                                groupNumber = allNumber;
                                flag=true;
                                break;
                            }
                        }
                        if(flag){
                            break;
                        }
                    }
                    //??????????????????groupNumber
                    String groupName = allGroupMapper.findGroupName(groupNumber);
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(tz);
                    String currentTime = sdf.format(date);
                    responseResult = Utils.memberLoginLogics(username,nickname,voteMapper,groupName,groupNumber,currentTime);
                }
            }
            return responseResult;
        }else{
            responseResult = new ResponseResult(1,"login failed",null);
            return responseResult;
        }
    }

    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseResult changePassword(@RequestBody Map params){
        String username = params.get("username").toString();
        String currentPassword = params.get("currentPassword").toString();
        String encryptCurrentPassword = EncryptUtil.encrypt(currentPassword);
        String newPassword = params.get("newPassword").toString();
        String encryptNewPassword = EncryptUtil.encrypt(newPassword);
        User user = userMapper.findOne(username,encryptCurrentPassword);
        ResponseResult responseResult= null;
        //?????????????????????????????????
        if(user!= null){
            int result = userMapper.changePassword(username,encryptNewPassword);
            if(result!=0){
                responseResult = new ResponseResult(0,"change password successfully",0);

            }else{
                responseResult = new ResponseResult(1,"failed to change password",0);
            }
        }else{
            responseResult = new ResponseResult(1,"username and password do not match correctly");
        }
        return  responseResult;

    }

    @PostMapping("/changeNickname")
    @ResponseBody
    public ResponseResult changeNickname(@RequestBody Map params){
        String username= params.get("username").toString();
        String nickname = params.get("nickname").toString();
        int result = userMapper.changeNickname(username,nickname);
        ResponseResult responseResult = null;
        if(result!=0){
            responseResult = new ResponseResult(0,"change nickname successfully",0);
        }else{
            responseResult = new ResponseResult(1,"failed to change nickname",0);
        }
        return responseResult;
    }

}

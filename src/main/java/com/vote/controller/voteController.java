package com.vote.controller;

import com.peopleVote.Mapper.PeopleVoteMapper;
import com.responseResult.ResponseResult;
import com.vote.Mapper.VoteMapper;
import com.vote.domain.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/vote")
public class voteController {

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private PeopleVoteMapper peopleVoteMapper;

    @PostMapping("/startVoting")
    @ResponseBody
    public ResponseResult startVoting(@RequestBody Vote[] params){
        boolean flag = true;
        ResponseResult responseResult = null;
        for(Vote vote:params){
            Vote check = voteMapper.checkIfCanInsert(vote.getGroupNumber(),vote.getLocationName(),vote.getLocationAddress(),vote.getVoteStartTime());
            if(check==null){
                int result = voteMapper.insertOne(vote.getGroupNumber(),vote.getActivityName(),vote.getLocationName(),vote.getLocationAddress(),vote.getLocationTag(),vote.getComments(),vote.getPhotos(),vote.getLongitude(),vote.getLatitude(),vote.getVoteStartTime(),vote.getVoteOverTime(),vote.getNumberOfVotes());
                if(result!=0){
                    System.out.println("插入成功！");
                }else{
                    flag=false;
                }
            }else{
                flag=false;
            }
        }
        if(flag){
            responseResult = new ResponseResult(0,"all selected location information has been loaded!",0);
            return responseResult;
        }
        return new ResponseResult(1,"failed to load all selected location information!",0);
    }

    @PostMapping("/votePlus")
    @ResponseBody
    public ResponseResult targetPlace(@RequestBody Map params){
        int groupNumber = Integer.parseInt(params.get("groupNumber").toString());
        String locationName = params.get("locationName").toString();
        String locationAddress = params.get("locationAddress").toString();
        String voteStartTime = params.get("voteStartTime").toString();

        boolean flag = true;
        ResponseResult responseResult = null;

        Vote check = voteMapper.checkIfCanInsert(groupNumber,locationName,locationAddress,voteStartTime);
        //说明有这条记录，加一就行了
        System.out.println(check);
        if(check!=null){
            int result = voteMapper.updateVotes(groupNumber,locationName,locationAddress,voteStartTime);
            if(result!=0){
                System.out.println("插入成功");
                String username = params.get("username").toString();
                int count = peopleVoteMapper.insertOne(groupNumber,username,locationName,voteStartTime);
                if(count!=0){
                    System.out.println("人投票成功");
                }else{
                    System.out.println("人没投票成功");
                }
            }else{
                flag=false;
            }
        }else{
            flag=false;
        }

        if(flag) {
            responseResult = new ResponseResult(0,"all target place number of votes have been updated!",0);
            return responseResult;
        }
        return new ResponseResult(1,"failed to update all target place number of votes",0);
    }

    @PostMapping("/voteFilter")
    @ResponseBody
    public ResponseResult voteFilter(@RequestBody Map params){
        int groupNumber = Integer.parseInt(params.get("groupNumber").toString());
        String voteStartTime = params.get("voteStartTime").toString();
        int voteLimit = Integer.parseInt(params.get("voteLimit").toString());
        List<Vote> list = voteMapper.filter(groupNumber,voteStartTime,voteLimit);
        ResponseResult responseResult = new ResponseResult(0,"the locations whose vote number >= "+voteLimit+" are shown here!",list);
        return  responseResult;
    }

    @PostMapping("/voteTypeFilter")
    @ResponseBody
    public ResponseResult voteTypeFilter(@RequestBody Map params){
        int groupNumber = Integer.parseInt(params.get("groupNumber").toString());
        String voteStartTime = params.get("voteStartTime").toString();
        String locationTag = params.get("locationTag").toString();
        List<Vote> list = voteMapper.typeFilter(groupNumber,voteStartTime,locationTag);
        ResponseResult responseResult = new ResponseResult(0,"the specific tag of locations are shown here!",list);
        return  responseResult;
    }

    @PostMapping("/votingEnd")
    @ResponseBody
    public ResponseResult votingEnd(@RequestBody Map params){
        int groupNumber = Integer.parseInt(params.get("groupNumber").toString());
        String voteStartTime = params.get("voteStartTime").toString();
        Vote vote =voteMapper.lastWinningLocation(groupNumber,voteStartTime);
        ResponseResult responseResult = new ResponseResult(0,"last vote result is here!",vote.getLocationName());
        return responseResult;
    }

    @PostMapping("/findNumberOfNotesOneGroupOneTime")
    @ResponseBody
    public ResponseResult voteGetLocationVote(@RequestBody Map params){
        int groupNumber = Integer.parseInt(params.get("groupNumber").toString());
        String voteStartTime = params.get("voteStartTime").toString();
        String locationName = params.get("locationName").toString();
        int number = voteMapper.findNumberOfNotesOneGroupOneTime(groupNumber,voteStartTime,locationName);
        ResponseResult responseResult = new ResponseResult(0,"get the number of votes of "+locationName+" successfully",number);
        return responseResult;
    }

    @PostMapping("/returnActivityNameAndTime")
    @ResponseBody
    public ResponseResult returnActivityNameAndTime(@RequestBody Map params){
        int groupNumber = Integer.parseInt(params.get("groupNumber").toString());
        SimpleDateFormat sdateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这里传入字符串格式后将按照此格式进行格式化，字符串中y、M、H、m、s分别是时间英文首字母，是不能变的，其他字符可以改变。
        // 4-2:创建一个Date时间对象：
        Date dateformatnew = new Date();//创建 Date对象并将它传入format方法：
        // 4-3：使用format(Date date)对日期进行格式化,需要传入一个Date时间对象：
        String currentTime = sdateformat.format(dateformatnew);
        Vote vote = voteMapper.getActivityNameAndTime(groupNumber,currentTime);
        ResponseResult responseResult;
        if(vote==null){
            responseResult = new ResponseResult(0,"The current group has no vote",null);
        }else{
            String activityName = vote.getActivityName();
            String voteStartTime = vote.getVoteStartTime();
            String voteOverTime = vote.getVoteOverTime();
            HashMap hashMap  = new HashMap();
            hashMap.put("activityName",activityName);
            hashMap.put("voteStartTime",voteStartTime);
            hashMap.put("voteOverTime",voteOverTime);
            responseResult = new ResponseResult(0,"The current group has one vote",hashMap);
        }
        return responseResult;
    }

}

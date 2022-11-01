package com.peopleVote.controller;

import com.peopleVote.Mapper.PeopleVoteMapper;
import com.peopleVote.domain.PeopleVote;
import com.responseResult.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/people")
public class PeopleVoteController {

    @Autowired
    public PeopleVoteMapper peopleVoteMapper;

    @PostMapping("/findIfVote")
    @ResponseBody
    public ResponseResult findIfVote(@RequestBody Map params){
        int groupNumber = Integer.parseInt(params.get("groupNumber").toString());
        String locationName = params.get("locationName").toString();
        String voteStartTime = params.get("voteStartTime").toString();
        String username = params.get("username").toString();
        PeopleVote peopleVote = peopleVoteMapper.findVotePeople(groupNumber,locationName,voteStartTime,username);
        ResponseResult responseResult=null;
        //如果为空表示还没投票
        if(peopleVote==null){
            responseResult = new ResponseResult(0,"this user has not voted to this location!","false");
        }else{
            responseResult = new ResponseResult(0,"this user has already voted to this location!","true");
        }
        return  responseResult;
    }
}

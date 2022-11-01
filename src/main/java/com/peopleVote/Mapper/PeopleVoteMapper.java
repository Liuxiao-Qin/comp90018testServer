package com.peopleVote.Mapper;

import com.peopleVote.domain.PeopleVote;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface PeopleVoteMapper {

    @Select("SELECT * FROM peopleVote where groupNumber=#{groupNumber} and locationName=#{locationName} and voteStartTime=#{voteStartTime} and username=#{username}")
    public PeopleVote findVotePeople(int groupNumber,String locationName,String voteStartTime,String username);

    @Insert("INSERT INTO peopleVote values(#{groupNumber},#{username},#{locationName},#{voteStartTime})")
    public int insertOne(int groupNumber,String username,String locationName,String voteStartTime);
}

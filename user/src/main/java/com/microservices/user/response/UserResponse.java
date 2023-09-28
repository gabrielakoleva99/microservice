package com.microservices.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int id;
    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String token;
    private List<Integer> songlists;

//    public void addSongList(SongListResponse list){
//        if(songlists==null){
//            System.out.println("Empty List");
//        } list.setOwnerId(userId);
//        this.songlists.add(list);
//    }
}

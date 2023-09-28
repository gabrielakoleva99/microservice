package com.microservices.user.request;

import com.microservices.user.response.SongListResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private int id;
    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String token;
    private List<Integer> songlists;
}

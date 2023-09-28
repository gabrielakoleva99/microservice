package com.microservices.user.service;

import com.microservices.user.exception.RessourceNotFoundException;
import com.microservices.user.exception.UnsuccessfulAuthorizationException;
import com.microservices.user.model.User;
import com.microservices.user.repository.UserRepository;
import com.microservices.user.request.UserRequest;
import com.microservices.user.response.SongListResponse;
import com.microservices.user.response.SongResponse;
import com.microservices.user.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public record UserService(UserRepository userRepository){

    @Autowired
    private static RestTemplate restTemplate;

    private static SecureRandom secureRandom = new SecureRandom();
    private static Base64.Encoder base64Encoder = Base64.getUrlEncoder();


    public Iterable<UserResponse> getUsers(){
        Iterable<User> allUsers = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for(User user : allUsers){
            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .userId(user.getUserId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .password(user.getPassword())
                    .token(user.getToken())
                    .songlists(user.getSonglists())
                    .build();
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    public String loginUser(UserRequest userRequest){
        User user = User.builder()
                .userId(userRequest.getUserId())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .songlists(userRequest.getSonglists())
                .build();

       String userId = userRequest.getUserId();
       String password = userRequest.getPassword();

       User user1 = userRepository.findByUserId(userRequest.getUserId());

       if (user1==null){
           throw new UnsuccessfulAuthorizationException("User", "userid", userId);
       }
       if(user1.getUserId().equals(userId) && user1.getPassword().equals(password)){
           byte[] randomBytes = new byte[24];
           secureRandom.nextBytes(randomBytes);
           String response = base64Encoder.encodeToString(randomBytes);
           user1.setToken(response);
           if (user1.getUserId().equals("maxime")) {
               UserResponse userResponse = UserResponse.builder()
                       .userId(user1.getUserId())
                       .id(user1.getId())
                       .firstName(user1.getFirstName())
                       .lastName(user1.getLastName())
                       .password(user1.getPassword())
                       .token(user1.getToken())
                       .build();

               userRepository.save(user1);

               return response;
           } if(user1.getUserId().equals("jane")){


               UserResponse userResponse = UserResponse.builder()
                       .userId(user1.getUserId())
                       .id(user1.getId())
                       .firstName(user1.getFirstName())
                       .lastName(user1.getLastName())
                       .password(user1.getPassword())
                       .token(user1.getToken())
                       .build();

               userRepository.save(user1);
               return response;
           }

       }
        throw new UnsuccessfulAuthorizationException("User", "userid", userId);
    }

}



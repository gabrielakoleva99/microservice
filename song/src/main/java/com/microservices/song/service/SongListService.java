package com.microservices.song.service;

import com.microservices.song.configuration.SongConfig;
import com.microservices.song.exception.ForbiddenException;
import com.microservices.song.exception.RessourceNotFoundException;
import com.microservices.song.exception.UnsuccessfulAuthorizationException;
import com.microservices.song.model.Song;
import com.microservices.song.model.SongList;
import com.microservices.song.repository.SongListRepository;
import com.microservices.song.repository.SongRepository;
import com.microservices.song.request.SongListRequest;
import com.microservices.song.request.SongRequest;
import com.microservices.song.response.SongListResponse;
import com.microservices.song.response.SongResponse;
import com.microservices.song.response.UserResponse;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public record SongListService (SongListRepository songListRepository, SongRepository songRepository, RestTemplate restTemplate){

    @Autowired
    static
    EurekaClient eurekaClient;



    @Autowired
    private static LoadBalancerClient loadBalancer;

    public SongListResponse getSongListById(int id, String authHeader) {
        try {
            UserResponse user = findUserByAuthToken(authHeader);
            if(user.getToken().equals(authHeader)){
                if (user.getUserId().equals("maxime")) {
                    if(!songListRepository.findByIdAndOwnerId(id, "maxime").isEmpty()){
                        SongList sl = songListRepository.findSongListByIdAndUserId(id,"maxime");
                        SongListResponse songListResponse = SongListResponse.builder()
                                .id(sl.getId())
                                .ownerId(sl.getOwnerId())
                                .name(sl.getName())
                                .isPrivate(sl.isPrivate())
                                .songs(sl.getSongs())
                                .build();
                        return songListResponse;
                    } else if(!songListRepository.findByIsPrivateAndUserIdAndId(false, "jane", id).isEmpty()){
                        List<SongList> aListOfSl =songListRepository.findByIsPrivateAndUserIdAndId(false, "jane", id);
                        SongListResponse songListResponseOld = new SongListResponse();
                        for(SongList songList : aListOfSl){
                            if(songList.getOwnerId().equals("jane") && !songList.isPrivate()){
                                 SongListResponse songListResponse = SongListResponse.builder()
                                        .id(songList.getId())
                                        .ownerId(songList.getOwnerId())
                                        .name(songList.getName())
                                        .isPrivate(songList.isPrivate())
                                        .songs(songList.getSongs())
                                        .build();
                                songListResponseOld=songListResponse;
                            }
                        }
                        return songListResponseOld;

                    }else if(!songListRepository.findByIsPrivateAndUserIdAndId(true, "jane", id).isEmpty()){
                        throw new ForbiddenException("SongList", "Song", id);

                    }
                } else if (user.getUserId().equals("jane")) {
                    if(!songListRepository.findByIdAndOwnerId(id, "jane").isEmpty()){
                        SongList sl = songListRepository.findSongListByIdAndUserId(id,"jane");
                        SongListResponse songListResponse = SongListResponse.builder()
                                .id(sl.getId())
                                .ownerId(sl.getOwnerId())
                                .name(sl.getName())
                                .isPrivate(sl.isPrivate())
                                .songs(sl.getSongs())
                                .build();
                        return songListResponse;
                    } else if(!songListRepository.findByIsPrivateAndUserIdAndId(false, "maxime", id).isEmpty()){
                        List<SongList> aListOfSl =songListRepository.findByIsPrivateAndUserIdAndId(false, "maxime", id);
                        SongListResponse songListResponseOld = new SongListResponse();
                        for(SongList songList : aListOfSl){
                            if(songList.getOwnerId().equals("maxime") && !songList.isPrivate()){
                                SongListResponse songListResponse = SongListResponse.builder()
                                        .id(songList.getId())
                                        .ownerId(songList.getOwnerId())
                                        .name(songList.getName())
                                        .isPrivate(songList.isPrivate())
                                        .songs(songList.getSongs())
                                        .build();
                                songListResponseOld=songListResponse;
                            }
                        }
                        return songListResponseOld;

                    }else if(!songListRepository.findByIsPrivateAndUserIdAndId(true, "maxime", id).isEmpty()){
                        throw new ForbiddenException("SongList", "Song", id);

                    }
                }

            }
        } catch (NullPointerException e) {
            throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
        }

        throw new ForbiddenException("SongList", "Song", id);
    }

    public Iterable<SongListResponse> getSongLists(String userid, String authHeader){
        try{
            UserResponse user = findUserByAuthToken(authHeader);
            if (user.getToken().equals(authHeader)) {
                if (user.getUserId().equals("maxime")) {
                    if (userid.equals("maxime")) {
                        Iterable<SongList> allSongLists = songListRepository.selectSongListByUser(userid);
                        List<SongListResponse> songListResponseList = new ArrayList<>();
                        for (SongList songList : allSongLists) {
                            if (songList.getOwnerId().equals("maxime")) {
                                SongListResponse songListResponse = SongListResponse.builder()
                                        .id(songList.getId())
                                        .ownerId(songList.getOwnerId())
                                        .name(songList.getName())
                                        .isPrivate(songList.isPrivate())
                                        .songs(songList.getSongs())
                                        .build();
                                songListResponseList.add(songListResponse);
                            }
                        }
                        return songListResponseList;
                    } else if (userid.equals("jane")) {
                        Iterable<SongList> allSongLists = songListRepository.findByIsPrivateAndUserId(false, "jane");
                        List<SongListResponse> songListResponseList = new ArrayList<>();
                        for (SongList songList : allSongLists) {
                            SongListResponse songListResponse = SongListResponse.builder()
                                    .id(songList.getId())
                                    .ownerId(songList.getOwnerId())
                                    .name(songList.getName())
                                    .isPrivate(songList.isPrivate())
                                    .songs(songList.getSongs())
                                    .build();
                            songListResponseList.add(songListResponse);
                        }
                        return songListResponseList;
                    } else throw new RessourceNotFoundException("User", "userId", userid);
                } else if(user.getUserId().equals("jane")) {
                    if (userid.equals("jane")) {
                        Iterable<SongList> allSongLists = songListRepository.selectSongListByUser(userid);
                        List<SongListResponse> songListResponseList = new ArrayList<>();
                        for (SongList songList : allSongLists) {
                            if (songList.getOwnerId().equals("jane")) {
                                SongListResponse songListResponse = SongListResponse.builder()
                                        .id(songList.getId())
                                        .ownerId(songList.getOwnerId())
                                        .name(songList.getName())
                                        .isPrivate(songList.isPrivate())
                                        .songs(songList.getSongs())
                                        .build();
                                songListResponseList.add(songListResponse);
                            }
                        }
                        return songListResponseList;
                    } else if (userid.equals("maxime")) {
                        Iterable<SongList> allSongLists = songListRepository.findByIsPrivateAndUserId(false, "maxime");
                        List<SongListResponse> songListResponseList = new ArrayList<>();
                        for (SongList songList : allSongLists) {
                            SongListResponse songListResponse = SongListResponse.builder()
                                    .id(songList.getId())
                                    .ownerId(songList.getOwnerId())
                                    .name(songList.getName())
                                    .isPrivate(songList.isPrivate())
                                    .songs(songList.getSongs())
                                    .build();
                            songListResponseList.add(songListResponse);
                        }
                        return songListResponseList;
                    } else throw new RessourceNotFoundException("User", "userId", userid);

                }
            }

        } catch (NullPointerException e) {
            throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
        }
        throw new RessourceNotFoundException("User", "userId", userid);

    }

    public class UserResponseList {
        public List<UserResponse> getUserResponseList() {
            return userResponseList;
        }

        public void setUserResponseList(List<UserResponse> userResponseList) {
            this.userResponseList = userResponseList;
        }

        private List<UserResponse> userResponseList;

        public UserResponseList() {
            userResponseList = new ArrayList<>();
        }

        // standard constructor and getter/setter
    }


    private UserResponse findUserByAuthToken(String authHeader) {
        String path = "http://AUTH/songsWS-gabs-KBE/rest/auth/users";
        ParameterizedTypeReference<List<UserResponse>> responseType = new ParameterizedTypeReference<List<UserResponse>>() {};
        ResponseEntity<List<UserResponse>> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null, responseType);
        List<UserResponse> users = responseEntity.getBody();

        for (UserResponse userResponse : users) {
            if (userResponse.getToken().equals(authHeader)) {
                return UserResponse.builder()
                        .id(userResponse.getId())
                        .userId(userResponse.getUserId())
                        .firstName(userResponse.getFirstName())
                        .lastName(userResponse.getLastName())
                        .token(userResponse.getToken())
                        .songlists(userResponse.getSonglists())
                        .build();
            }
        }
        throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
    }


    private SongListResponse mapSongListToResponse(SongList songList) {
        return SongListResponse.builder()
                .id(songList.getId())
                .ownerId(songList.getOwnerId())
                .name(songList.getName())
                .isPrivate(songList.isPrivate())
                .songs(songList.getSongs())
                .build();
    }

    public String registerSongList(SongListRequest request, String authHeader){
        try{
            UserResponse user = findUserByAuthToken(authHeader);
            if(user.getToken().equals(authHeader)) {
                if(request.getSongs().isEmpty()) {
                    throw new RessourceNotFoundException("User", "userId", request);
                }
                for(Song s : request.getSongs()){
                    if(!songRepository.existsById(s.getId())){
                        throw new RessourceNotFoundException("User", "userId", request);
                    }
                }
                UserResponse ur = findUserByAuthToken(authHeader);
                request.setOwnerId(ur.getUserId());
                SongList songList = SongList.builder()
                        .ownerId(request.getOwnerId())
                        .name(request.getName())
                        .isPrivate(request.isPrivate())
                        .songs(request.getSongs())
                        .build();
                songListRepository.save(songList);
                String path = "Location: /songLists/" + songList.getId();
                return path;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
        }
        throw new RessourceNotFoundException("User", "userId", request);
    }



    public void putSongList(SongListRequest request, int id, String authHeader){
        try{
            UserResponse user = findUserByAuthToken(authHeader);
            if(user.getToken().equals(authHeader)) {
                if(request.getSongs().isEmpty()){
                    throw new RessourceNotFoundException("User", "userId", request);
                }
                if(!songListRepository.existsById(id)){
                    throw new RessourceNotFoundException("User", "userId", request);
                }
                for(Song s : request.getSongs()){
                    if(!songRepository.existsById(s.getId())){
                        throw new RessourceNotFoundException("User", "userId", request);
                    }
                }
                if(user.getUserId().equals("maxime")){
                    if(!songListRepository.findByIdAndOwnerId(id, "maxime").isEmpty()){
                        SongList sl = songListRepository.findSongListByIdAndUserId(id,"maxime");
                        sl.setName(request.getName());
                        sl.setPrivate(request.isPrivate());
                        sl.setSongs(request.getSongs());
                        sl.setOwnerId(user.getUserId());
                        songListRepository.save(sl);
                    } else {
                        throw new ForbiddenException("SongList", "Song", id);
                    }
                } else if(user.getUserId().equals("jane")){
                    if(!songListRepository.findByIdAndOwnerId(id, "jane").isEmpty()) {
                        SongList sl = songListRepository.findSongListByIdAndUserId(id,"jane");
                        sl.setName(request.getName());
                        sl.setPrivate(request.isPrivate());
                        sl.setSongs(request.getSongs());
                        sl.setOwnerId(user.getUserId());
                        songListRepository.save(sl);
                    } else {
                        throw new ForbiddenException("SongList", "Song", id);
                    }
                }
            } else {
                throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
            }

            }catch (NullPointerException e) {
            throw new UnsuccessfulAuthorizationException("User", "token", authHeader);
        }

    }




    public void removeSongList(int id, String authHeader){
        try{
            UserResponse user = findUserByAuthToken(authHeader);
            if(user.getToken().equals(authHeader)) {
                if(user.getUserId().equals("maxime")){
                    if(!songListRepository.findByIdAndOwnerId(id, "maxime").isEmpty()){
                        SongList sl = songListRepository.findSongListByIdAndUserId(id, "maxime");
                        songListRepository.delete(sl);
                    } else {
                        throw new ForbiddenException("SongList", "Song", id);
                    }
                }
                else if(user.getUserId().equals("jane")){
                    if(!songListRepository.findByIdAndOwnerId(id, "jane").isEmpty()) {
                        SongList sl = songListRepository.findSongListByIdAndUserId(id, "jane");
                        songListRepository.delete(sl);
                    }
                    else {
                        throw new ForbiddenException("SongList", "Song", id);
                    }
                    }
            }
            }catch (NullPointerException e) {
            throw new RessourceNotFoundException("User", "userId", id);
        }
    }
    }


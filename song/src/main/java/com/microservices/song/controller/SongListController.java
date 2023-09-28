package com.microservices.song.controller;

import com.microservices.song.request.SongListRequest;
import com.microservices.song.request.SongRequest;
import com.microservices.song.response.SongListResponse;
import com.microservices.song.response.SongResponse;
import com.microservices.song.service.SongListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/songsWS-gabs-KBE/rest")
public record SongListController (SongListService songListService){

    @GetMapping(path = "/songLists/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SongListResponse> getSongListDetails(@PathVariable("id") int id,  @RequestHeader(value = "Authorization") String authHeader){
        SongListResponse songListResponse = songListService.getSongListById(id, authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(songListResponse);
    }

    @GetMapping(path = "/songLists", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Iterable<SongListResponse>> getAllSongLists(@RequestParam(value = "userId") String userId,@RequestHeader(value = "Authorization") String authHeader) {
        Iterable<SongListResponse> allSongs = songListService.getSongLists(userId,authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(allSongs);

    }

    @PostMapping(path = "/songLists", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> registerSongList(@RequestBody SongListRequest songListRequest, @RequestHeader(value = "Authorization") String authHeader){
        try {
            if (songListRequest.getSongs().equals(null)) {
                throw new NullPointerException();
            }
            log.info("new song added {}", songListRequest);
            String path = songListService.registerSongList(songListRequest, authHeader);
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", path);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(path = "/songLists/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateSongList(@PathVariable(value = "id") int id,
                                        @RequestBody SongListRequest songListRequest, @RequestHeader(value = "Authorization") String authHeader) {

        try {
            if (songListRequest.getSongs().equals(null)) {
                throw new NullPointerException();
            }
            songListService.putSongList(songListRequest, id, authHeader);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/songLists/{id}")
    public ResponseEntity<?> deleteSongList(@PathVariable(value = "id") int id, @RequestHeader(value = "Authorization") String authHeader) {
        try {
            songListService.removeSongList(id, authHeader);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();

        }

    }
}

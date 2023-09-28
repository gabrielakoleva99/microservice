package com.microservices.song.controller;

import com.microservices.song.request.SongRequest;
import com.microservices.song.response.SongResponse;
import com.microservices.song.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/songsWS-gabs-KBE/rest")
public record SongController(SongService songService) {


    @PostMapping(path = "/songs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> registerSong(@RequestBody SongRequest songRequest){
        try {
            if (songRequest.getTitle().equals(null)) {
                throw new NullPointerException();
            }
            log.info("new song added {}", songRequest);
            String path = songService.registerSong(songRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", path);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }

    }
    @GetMapping(path = "/songs/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SongResponse> getSongDetails(@PathVariable("id") int id){
      SongResponse songResponse = songService.getSongById(id);
      return ResponseEntity.status(HttpStatus.OK).body(songResponse);
    }

    @GetMapping(path = "/songs", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Iterable<SongResponse>> getAllSongs() {
        Iterable<SongResponse> allSongs = songService.getSongs();
        return ResponseEntity.status(HttpStatus.OK).body(allSongs);

    }

    @PutMapping(path = "/songs/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateSong(@PathVariable(value = "id") int id,
                                           @RequestBody SongRequest songToPut) {

        try {
            if (songToPut.getTitle().equals(null)) {
                throw new NullPointerException();
            }
            songService.putSong(songToPut, id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/songs/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable(value = "id") int id) {
        try {
            songService.removeSong(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();

        }

        }
}

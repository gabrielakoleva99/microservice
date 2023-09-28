package com.microservices.album.controller;

import com.microservices.album.responses.AlbumResponse;
import com.microservices.album.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.microservices.album.requests.AlbumRequest;
@RestController
@RequestMapping("/rest/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAlbum(@RequestBody AlbumRequest albumRequest){
        try {
            if (albumRequest.getName().equals(null)) {
                throw new NullPointerException();
            }
            String path = albumService.createAlbum(albumRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", path);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AlbumResponse> getSongDetails(@PathVariable("id") int id){
        System.out.println("1");
        AlbumResponse albumResponse = albumService.getAlbumById(id);
        return ResponseEntity.status(HttpStatus.OK).body(albumResponse);
    }
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Iterable<AlbumResponse>> getAllSongs() {
        Iterable<AlbumResponse> allALbums = albumService.getAlbums();
        return ResponseEntity.status(HttpStatus.OK).body(allALbums);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable(value = "id") int id) {
        try {
            albumService.removeAlbum(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();

        }

    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateAlbum(@PathVariable(value = "id") int id,
                                            @RequestBody AlbumRequest albumRequest) {

        try {
            if (albumRequest.getSongs().equals(null)) {
                throw new NullPointerException();
            }
            albumService.putAlbum(id, albumRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

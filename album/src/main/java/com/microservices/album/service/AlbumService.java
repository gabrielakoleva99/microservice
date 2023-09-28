package com.microservices.album.service;

import com.microservices.album.exception.RessourceNotFoundException;
import com.microservices.album.model.Album;
import com.microservices.album.repository.AlbumRepository;
import com.microservices.album.requests.AlbumRequest;
import com.microservices.album.responses.AlbumResponse;
import com.microservices.album.responses.SongResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public record AlbumService(RestTemplate restTemplate, AlbumRepository albumRepository) {

    private SongResponse findSongById(int id) {
        String path = "http://SONG/songsWS-gabs-KBE/rest/songs";
        ParameterizedTypeReference<List<SongResponse>> responseType = new ParameterizedTypeReference<List<SongResponse>>() {
        };
        ResponseEntity<List<SongResponse>> responseEntity = restTemplate.exchange(path, HttpMethod.GET, null, responseType);
        List<SongResponse> songs = responseEntity.getBody();
        for (SongResponse songResponse : songs) {
            if (songResponse.getId() == id) {
                SongResponse sr = SongResponse.builder()
                        .title(songResponse.getTitle())
                        .album(songResponse.getAlbum())
                        .label(songResponse.getLabel())
                        .released(songResponse.getReleased())
                        .artist(songResponse.getArtist())
                        .build();
                return sr;
            }
        }
        throw new RessourceNotFoundException("Song", "id", id);
    }

    //testvai go pak za vs sluchai
    public String createAlbum(AlbumRequest albumRequest) {
        try{
            if(!albumRequest.getName().isEmpty()){
                if(!albumRequest.getSongs().isEmpty()){
                    for(int i : albumRequest.getSongs()){
                        if(!findSongById(i).equals(null)){
                            Album album = Album.builder()
                                    .name(albumRequest.getName())
                                    .artist(albumRequest.getArtist())
                                    .year(albumRequest.getYear())
                                    .songs(albumRequest.getSongs())
                                    .build();
                            albumRepository.save(album);
                            String path = "Location: /album/" + album.getId();
                            return path;
                        }else{
                            throw new RessourceNotFoundException("Album", "id", albumRequest);
                        }
                    }
                }else{
                    throw new RessourceNotFoundException("Album", "id", albumRequest);
                }
            }else{
                throw new RessourceNotFoundException("Album", "id", albumRequest);

            }
        }catch (NullPointerException e) {
            throw new RessourceNotFoundException("Album", "id", albumRequest);
        }
        throw new RessourceNotFoundException("Album", "id", albumRequest);
    }

    public AlbumResponse getAlbumById(int id) {
        try{
            if(!albumRepository.findById(id).equals(null)){
                Album album = albumRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Song", "id", id));
                AlbumResponse albumResponse = AlbumResponse.builder()
                        .id(album.getId())
                        .name(album.getName())
                        .artist(album.getArtist())
                        .year(album.getYear())
                        .songs(album.getSongs())
                        .build();
                return albumResponse;
            }else{
                throw new RessourceNotFoundException("Album", "id", id);
            }

        }catch (NullPointerException e) {
            throw new RessourceNotFoundException("Album", "id", id);
        }

    }

    public Iterable<AlbumResponse> getAlbums() {
        List<Album> allAlbums = albumRepository.findAll();
        List<AlbumResponse> albumResponseList = new ArrayList<>();
        for (Album album : allAlbums) {
            AlbumResponse albumResponse = AlbumResponse.builder()
                    .id(album.getId())
                    .name(album.getName())
                    .artist(album.getArtist())
                    .year(album.getYear())
                    .songs(album.getSongs())
                    .build();
            albumResponseList.add(albumResponse);
        }
        return albumResponseList;
    }

    public void putAlbum(int id, AlbumRequest albumRequest){
        try {
            if (!albumRequest.getName().equals(null)) {
                if (!albumRequest.getSongs().isEmpty()) {

                    if (albumRepository.existsById(id)) {
                        Album album = albumRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Song", "id", id));
                        album.setName(albumRequest.getName());
                        album.setArtist(albumRequest.getArtist());
                        album.setYear(albumRequest.getYear());
                        album.setSongs(albumRequest.getSongs());
                        albumRepository.save(album);
                    } else {
                        throw new RessourceNotFoundException("Album", "id", id);
                    }
                } else {
                    throw new RessourceNotFoundException("Album", "id", id);
                }

            } else{
                throw new RessourceNotFoundException("Album", "id", id);
            }
        } catch (NullPointerException e) {
            throw new RessourceNotFoundException("Album", "id", id);
        }
    }

    public void removeAlbum(int id) {
        try {
            if(!albumRepository.findAlbumById(id).equals(null)){
                Album album = albumRepository.findAlbumById(id);
                albumRepository.delete(album);
            } else{
                throw new RessourceNotFoundException("Album", "id", id);
            }

        } catch (NullPointerException e) {
            throw new RessourceNotFoundException("Album", "id", id);
        }
    }
}

package com.microservices.song.service;

import com.microservices.song.request.SongRequest;
import com.microservices.song.response.SongResponse;
import com.microservices.song.exception.RessourceNotFoundException;
import com.microservices.song.model.Song;
import com.microservices.song.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public record SongService(SongRepository songRepository)   {
    public String registerSong(SongRequest request){
            Song song = Song.builder()
                    .title(request.getTitle())
                    .artist(request.getArtist())
                    .label(request.getLabel())
                    .released(request.getReleased())
                    .build();
            songRepository.save(song);
            String path = "Location: /songsWS-gabs-KBE/rest/songs?songId=" + song.getId();
            return path;

    }

    public void putSong(SongRequest request, int id){
        Song songOld = songRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Song", "id", id));
        Song songToPut = Song.builder()
                .title(request.getTitle())
                .artist(request.getArtist())
                .label(request.getLabel())
                .released(request.getReleased())
                .album(request.getAlbum())
                .build();
        songOld.setTitle(songToPut.getTitle());
        songOld.setArtist(songToPut.getArtist());
        songOld.setLabel(songToPut.getLabel());
        songOld.setReleased(songToPut.getReleased());
        songRepository.save(songOld);
    }


    public SongResponse getSongById(int id){
            Song song = songRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Song", "id", id));
        System.out.println(song);
            SongResponse songResponse = SongResponse.builder()
                    .id(song.getId())
                    .title(song.getTitle())
                    .artist(song.getArtist())
                    .label(song.getLabel())
                    .released(song.getReleased())
                    .album(song.getAlbum())
                    .build();
            return songResponse;

        }

        public void removeSong(int id){
            Song song = songRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Song", "id", id));
            songRepository.delete(song);
        }

        public Iterable<SongResponse> getSongs(){
        List<Song> allSongs = songRepository.findAll();
        List<SongResponse> songResponseList = new ArrayList<>();
        for(Song song : allSongs){
            SongResponse songResponse = SongResponse.builder()
                    .id(song.getId())
                    .title(song.getTitle())
                    .artist(song.getArtist())
                    .label(song.getLabel())
                    .released(song.getReleased())
                    .album(song.getAlbum())
                    .build();
            songResponseList.add(songResponse);
        }
        return songResponseList;
        }
    }



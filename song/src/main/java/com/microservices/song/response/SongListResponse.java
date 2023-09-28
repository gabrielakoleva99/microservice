package com.microservices.song.response;

import com.microservices.song.model.Song;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongListResponse {

    int id;
    String ownerId;
    String name;
    boolean isPrivate;
    List<Song> songs;
}

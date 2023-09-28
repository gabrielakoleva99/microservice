package com.microservices.song.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongResponse {

    //dobavi eventualno i id

    int id;
    String title;
    String artist;
    String label;
    String released;
    String album;
}

package com.microservices.song.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongRequest {

    //dobavi eventualno i id
    String title;
    String artist;
    String label;
    String released;
    String album;
}

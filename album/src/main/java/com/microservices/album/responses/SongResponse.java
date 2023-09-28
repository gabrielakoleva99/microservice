package com.microservices.album.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongResponse {


    int id;
    String title;
    String artist;
    String label;
    String released;
    String album;
}
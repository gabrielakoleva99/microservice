package com.microservices.album.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongRequest {

    String title;
    String artist;
    String label;
    String released;
    String album;
}

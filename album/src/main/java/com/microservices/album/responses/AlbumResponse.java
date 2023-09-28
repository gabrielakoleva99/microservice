package com.microservices.album.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponse {

    private int id;
    private String name;
    private int year;
    private String artist;
    private List<Integer> songs;
}

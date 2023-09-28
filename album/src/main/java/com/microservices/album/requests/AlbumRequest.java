package com.microservices.album.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequest {


    private int id;
    private String name;
    private int year;
    private String artist;
    private List<Integer> songs;
}

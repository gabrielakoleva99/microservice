package com.microservices.album.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Album {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="year")
    private int year;
    @Column(name="artist")
    private String artist;

    public List<Integer> getSongs() {
        return songs;
    }

    public void setSongs(List<Integer> songs) {
        this.songs = songs;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "album_songs", joinColumns = @JoinColumn(name = "album_id"))
    @Column(name = "song_id")
    private List<Integer> songs;
}

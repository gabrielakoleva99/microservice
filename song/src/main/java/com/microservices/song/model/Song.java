package com.microservices.song.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "song")
public class Song {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="title")
    private String title;
    @Column(name="artist")
    private String artist;
    @Column(name="label")
    private String label;
    @Column(name="released")
    private String released;
    @Column(name="album")
    private String album;
}

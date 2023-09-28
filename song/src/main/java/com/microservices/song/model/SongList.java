package com.microservices.song.model;

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
@Table(name = "songlist")
public class SongList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="ownerid")
    private String ownerId;
    @Column(name="name")
    private String name;
    @Column(name="isprivate")
    private boolean isPrivate;
    @Column(name="songs")
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "songlist_song",
            joinColumns = {@JoinColumn( name = "songlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn( name = "song_id", referencedColumnName = "id")})
    private List<Song> songs;
}

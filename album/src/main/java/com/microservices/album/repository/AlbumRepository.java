package com.microservices.album.repository;

import com.microservices.album.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    Album findAlbumById(int id);


}

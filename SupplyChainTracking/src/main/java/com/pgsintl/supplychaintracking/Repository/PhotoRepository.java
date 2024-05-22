package com.pgsintl.supplychaintracking.Repository;

import com.pgsintl.supplychaintracking.Entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,Long> {

    Optional<Photo> findByNamePhoto(String fileName);

}

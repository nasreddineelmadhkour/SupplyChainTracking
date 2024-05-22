package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Entities.Photo;
import com.pgsintl.supplychaintracking.Repository.PhotoRepository;
import com.pgsintl.supplychaintracking.Utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PhotoService implements PhotoIService{
    @Autowired
    PhotoRepository photoRepository;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {

        Photo imageData = photoRepository.save(Photo.builder()
                .namePhoto(file.getOriginalFilename())
                .type(file.getContentType())
                .photo(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }
    @Override
    public byte[] downloadImage(String fileName){
        Optional<Photo> dbImageData = photoRepository.findByNamePhoto(fileName);
        byte[] images= ImageUtils.decompressImage(dbImageData.get().getPhoto());
        return images;
    }

}

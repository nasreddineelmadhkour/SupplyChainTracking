package com.pgsintl.supplychaintracking.Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoIService {

    //  public void uploadPhoto(MultipartFile file, String name)throws IOException;

    public byte[] downloadImage(String fileName);
    public String uploadImage(MultipartFile file) throws IOException;
}

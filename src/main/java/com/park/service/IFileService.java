package com.park.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Park on 2018-12-3.
 */
public interface IFileService {
    String upload(MultipartFile file , String path);
}

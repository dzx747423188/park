package com.park.service.impl;

import com.google.common.collect.Lists;
import com.park.service.IFileService;
import com.park.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Park on 2018-12-3.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileUploadName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名:{},上传路径:{},新文件名:{}",fileName,path,fileUploadName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);//设置可写
            fileDir.mkdir();
        }
        File targetFile = new File(path,fileUploadName);
        try {
            file.transferTo(targetFile);//文件上传成功
            //将targetFile上传到我们的FTP服务器
            FTPUtil.uploadFile(Lists.<File>newArrayList(targetFile));
            //上传完成之后，删除up；oad下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.info("上传文件异常:",e);
            e.printStackTrace();
        }
        return targetFile.getName();
    }
}

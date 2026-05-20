package top.wjqian.week04.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.wjqian.week04.common.Result;
import top.wjqian.week04.exception.BusinessException;
import top.wjqian.week04.utils.FileUploadUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class UploadController {
    private static final String FILE_URL_PREFIX = "http://localhost:8080/upload/";

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            throw new BusinessException(400,"文件不能为空");
        }
        String fileName;
        try{
            fileName = FileUploadUtil.upload(file);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        String url = FILE_URL_PREFIX + fileName;
        return Result.success(url);
    }

    @PostMapping("/upload/batch")
    public Result<List<String>> uploadBatch(@RequestParam("file") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new BusinessException(400, "文件不能为空");
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if(file.isEmpty()){
                continue;
            }
            try{
                String fileName = FileUploadUtil.upload(file);
                urls.add(FILE_URL_PREFIX + fileName);
            }catch (IOException e){
                throw new RuntimeException("文件上传失败:"+file.getOriginalFilename(),e);
            }
            }
        return Result.success(urls);
    }
}

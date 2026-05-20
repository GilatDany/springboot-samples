package top.wjqian.week04.utils;



import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import top.wjqian.week04.exception.BusinessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class FileUploadUtil {
    private static final String UPLOAD_DIR = "target/classes/static/upload";

//    static {
//        File dir=new File(UPLOAD_DIR);
//        if(!dir.exists()){
//            if(!dir.mkdirs()){
//                throw new RuntimeException("创建上传目录失败:"+UPLOAD_DIR);
//            }
//        }
//    }

    private static final Set<String> ALLOW_EXTENSIONS = Set.of(
            ".jpg",".jpeg",".png",".gif",".bmp",".webp",
            ".pdf",".doc",".docx",".xls",".xlsx",".ppt",".pptx",
            ".txt",".md",".7z",
            ".json",".xml"
    ) ;

//    private static String getUploadDir(){
//        try{
//            String baseDir= ResourceUtils.getURL("classpath:").getPath();
//
//            Path uploadPath= Paths.get(baseDir,"static/upload/");
//            Files.createDirectories(uploadPath);
//            String uploadDirDir=uploadPath.toAbsolutePath()+"/";
//            log.info("上传目录: {}",uploadDirDir);
//            return uploadDirDir;
//
//        }catch (IOException e){
//            throw new RuntimeException("创建上传目录失败",e);
//        }
//    }
private static String getUploadDirPath() {
    // 使用当前工作目录，避免 classpath 问题
    String userDir = System.getProperty("user.dir");
    String uploadPath = userDir + File.separator + UPLOAD_DIR;

    // 确保目录存在
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
        boolean created = uploadDir.mkdirs();
        if (created) {
            log.info("创建上传目录成功: {}", uploadPath);
        } else {
            log.warn("创建上传目录失败或目录已存在: {}", uploadPath);
        }
    }

    return uploadPath;
}


    public static String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new BusinessException(400, "文件名不能为空");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

        if (!ALLOW_EXTENSIONS.contains(suffix)) {
            throw new BusinessException(400, "不支持的文件格式:" + suffix);
        }
        String fileName = UUID.randomUUID() + suffix;
//        File dest = new File(UPLOAD_DIR + fileName);
        String uploadPath = getUploadDirPath();
        File dest = new File(uploadPath, fileName);

        file.transferTo(dest);
        return fileName;

    }
}




























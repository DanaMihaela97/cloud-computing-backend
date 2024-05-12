//package cloudcomputing.cc.controller;
//
//import cloudcomputing.cc.entity.AWSClientConfig;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//
//@RestController
//public class UploadController {
//    @Autowired
//    private AmazonS3 amazonS3;
//
//    @Autowired
//    private AWSClientConfig awsClientConfig;
//
//    @PostMapping("/apply")
//    public String saveFile(@RequestParam("file") MultipartFile file,
//                           @RequestParam("firstName") String firstName,
//                           @RequestParam("lastName") String lastName,
//                           @RequestParam("email") String email,
//                           @RequestParam("phone") String phone) {
//        try {
//            File localFile = convertMultipartFileToFile(file);
//
//
//            String fileName = firstName + "_" + lastName + ".pdf";
//
//
//            amazonS3.putObject(new PutObjectRequest(awsClientConfig.getBucketName(), fileName, localFile));
//
//
//            return fileName;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error saving file: " + e.getMessage();
//        }
//    }
//
//
//    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
//        File convertedFile = new File(file.getOriginalFilename());
//        file.transferTo(convertedFile);
//        return convertedFile;
//    }
//}

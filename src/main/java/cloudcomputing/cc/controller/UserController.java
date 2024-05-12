package cloudcomputing.cc.controller;

import cloudcomputing.cc.entity.AWSClientConfig;
import cloudcomputing.cc.entity.User;
import cloudcomputing.cc.service.UserService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AWSClientConfig awsClientConfig;

    @PostMapping("/apply")
    public String createUser(@RequestParam("file") MultipartFile file,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("email") String email,
                             @RequestParam("phone") String phone, User user) {
        try {
            File localFile = convertMultipartFileToFile(file);

            String fileName = firstName + "_" + lastName + ".pdf";

            amazonS3.putObject(new PutObjectRequest(awsClientConfig.getBucketName(), fileName, localFile));

            userService.createUser(user);

            return "User created successfully with CV uploaded!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error creating user: " + e.getMessage();
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        file.transferTo(convertedFile);
        return convertedFile;
    }
}
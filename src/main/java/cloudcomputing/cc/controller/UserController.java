package cloudcomputing.cc.controller;

import cloudcomputing.cc.entity.AWSClientConfig;
import cloudcomputing.cc.entity.User;
import cloudcomputing.cc.service.UserService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(path="/apply", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createUser(@RequestPart User user, @RequestPart MultipartFile cv) {

        try {
            userService.createUser(user);
            String fileName = user.getFirstName() + "_" + user.getLastName() + ".pdf";

            PutObjectRequest putObjectRequest = new PutObjectRequest(awsClientConfig.getBucketName(),fileName, cv.getInputStream(), null);
            amazonS3.putObject(putObjectRequest);


            return "User created successfully with CV uploaded!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error creating user: " + e.getMessage();
        }
    }
}
package cloudcomputing.cc.controller;

import cloudcomputing.cc.entity.AWSClientConfig;
import cloudcomputing.cc.entity.User;
import cloudcomputing.cc.service.BucketService;
import cloudcomputing.cc.service.UserService;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class UserController {
    private final UserService userService;
    private final BucketService bucketService;



    @Autowired
    public UserController(UserService userService, BucketService bucketService) {
        this.userService = userService;
        this.bucketService = bucketService;
    }

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AWSClientConfig awsClientConfig;

    @PostMapping(path = "/apply", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createUser(@RequestPart User user, @RequestPart MultipartFile cv) throws IOException {


        userService.createUser(user);
//        bucketService.putObjectIntoBucket(awsClientConfig.getBucketName(), "danacv.pdf", cv.getInputStream());

        return bucketService.getBucketList().toString();
    }

}

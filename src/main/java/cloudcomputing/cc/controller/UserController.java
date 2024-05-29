package cloudcomputing.cc.controller;

import cloudcomputing.cc.config.S3Config;
import cloudcomputing.cc.config.SnsPublisher;
import cloudcomputing.cc.entity.User;
import cloudcomputing.cc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class UserController {
    private final UserService userService;
    private final S3Config s3Config;
    private final SnsPublisher snsPublisher;

    @Autowired
    public UserController(UserService userService, S3Config s3Config, SnsPublisher snsPublisher) {
        this.userService = userService;
        this.s3Config = s3Config;
        this.snsPublisher = snsPublisher;
    }

    @PostMapping(path = "/apply", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createUser(@RequestPart User user, @RequestPart("cv") MultipartFile cv) throws IOException {
        userService.createUser(user);

        String key = user.getFirstName() + "_" + user.getLastName() + ".pdf";
        s3Config.saveInS3(key, cv.getInputStream());

        String userEmail=user.getEmail();
        snsPublisher.subscribe(userEmail);

        return ResponseEntity.ok("Created user");
    }


}

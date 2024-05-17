package cloudcomputing.cc.controller;

import cloudcomputing.cc.entity.User;
import cloudcomputing.cc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class UserController {
    private final UserService userService;
    private final S3Client s3Client;

    @Autowired
    public UserController(UserService userService, S3Client s3Client) {
        this.userService = userService;
        this.s3Client = s3Client;
    }
    @PostMapping(path = "/apply", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createUser(@RequestPart User user, @RequestPart("cv") MultipartFile cv) throws IOException {

//        String cvFileName = cv.getOriginalFilename();
        userService.createUser(user);
        PutObjectRequest objReq = PutObjectRequest.builder()
                .bucket("cvs-ccproject")
                .key(user.getFirstName() + user.getLastName())
                .build();
        s3Client.putObject(objReq, RequestBody.fromInputStream(cv.getInputStream(), cv.getInputStream().available()));
        return "ok";
    }
}
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
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class UserController {
    private final UserService userService;
    private final S3Client s3Client;
    private final SnsClient snsClient;

    @Autowired
    public UserController(UserService userService, S3Client s3Client, SnsClient snsClient) {
        this.userService = userService;
        this.s3Client = s3Client;
        this.snsClient = snsClient;
    }
    @PostMapping(path = "/apply", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createUser(@RequestPart User user, @RequestPart("cv") MultipartFile cv) throws IOException {


        userService.createUser(user);
        // save cv in s3
        PutObjectRequest objReq = PutObjectRequest.builder()
                .bucket("cvs-ccproject")
                .key( user.getFirstName() + user.getLastName() + ".pdf")
                .build();
        s3Client.putObject(objReq, RequestBody.fromInputStream(cv.getInputStream(), cv.getInputStream().available()));

        // send email
        String subject = "Application Confirmation";
        String bodyText = "Hello " + user.getFirstName() + ",\n\n" +
                "We have successfully received your application for our open position.\n\n" +
                "We will get back to you soon with further details.\n\n" +
                "Best regards,\nOur Team";

        PublishRequest request = PublishRequest.builder()
                .topicArn("arn:aws:sns:us-east-1:814615723430:cc-sns")
                .subject(subject)
                .message(bodyText)
                .build();
        snsClient.publish(request);

        return "ok";
    }
}
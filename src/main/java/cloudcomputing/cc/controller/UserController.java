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
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class UserController {
    private final UserService userService;
    private final S3Client s3Client;
    private final SnsClient snsClient;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

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
                .key(user.getFirstName() + "_" + user.getLastName() + ".pdf")
                .build();
        s3Client.putObject(objReq, RequestBody.fromInputStream(cv.getInputStream(), cv.getInputStream().available()));

        // send initial email
        String userEmail=user.getEmail();

        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .topicArn("arn:aws:sns:us-east-1:814615723430:cc-sns")
                .protocol("email")
                .endpoint(userEmail).build();
        snsClient.subscribe(subscribeRequest);

        String subject = "Application Confirmation";
        String bodyText = "Hello " + user.getLastName() + ",\n\n" +
                "We have successfully received your application for our open position.\n\n" +
                "We will get back to you soon with further details.\n\n" +
                "Best regards,\nOur Team";

        sendEmail(subject, bodyText);

        // schedule follow-up email after 10 seconds
        scheduledExecutorService.schedule(() -> sendFollowUpEmail(user), 10, TimeUnit.SECONDS);

        return "ok";
    }

    private void sendEmail(String subject, String bodyText) {
        PublishRequest request = PublishRequest.builder()
                .topicArn("arn:aws:sns:us-east-1:814615723430:cc-sns")
                .subject(subject)
                .message(bodyText)
                .build();
        snsClient.publish(request);
    }

    private void sendFollowUpEmail(User user) {
        String subject = "Application Status Update";
        String bodyText = getRandomMessage(user);

        sendEmail(subject, bodyText);
    }

    private String getRandomMessage(User user) {
        String name = user.getLastName();
        String[] messages = {
                "Hello " + name + ",\n\n" + "We are pleased to inform you that your application has successfully passed to the next stage of our selection process. Congratulations! You will receive further instructions shortly regarding what you need to prepare for the next steps.",
                "Dear " + name + ",\n\n" + "We appreciate your interest in our company. I am writing to inform you that the vacancy you have applied for has now been filled and regrettably we did not get the chance to fully consider your application."
        };

        Random rand = new Random();
        return messages[rand.nextInt(messages.length)];
    }
}

package cloudcomputing.cc.controller;

import cloudcomputing.cc.entity.Job;
import cloudcomputing.cc.entity.User;
import cloudcomputing.cc.service.JobService;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class UserController {
    private final UserService userService;
    private final S3Client s3Client;
    private final SnsClient snsClient;
    private final JobService jobService;

    @Autowired
    public UserController(UserService userService, S3Client s3Client, SnsClient snsClient, JobService jobService) {
        this.userService = userService;
        this.s3Client = s3Client;
        this.snsClient = snsClient;
        this.jobService = jobService;
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

        List<Job> allJobs = jobService.getAllJobs();
        notifySubscribersAboutNewJob();
        return "ok";
    }
    private void notifySubscribersAboutNewJob() {
        String subject = "New Jobs Posted!";
        String bodyText = "A new job has been posted on the platform. Check out the latest jobs at our website.";

        String platformUrl = "http://34.235.53.175:4200";

        bodyText += "\n\nVisit us at: " + platformUrl;

        sendEmail(subject, bodyText);
    }
    private void sendEmail(String subject, String bodyText) {
        PublishRequest request = PublishRequest.builder()
                .topicArn("arn:aws:sns:us-east-1:814615723430:cc-sns")
                .subject(subject)
                .message(bodyText)
                .build();
        snsClient.publish(request);
    }

}

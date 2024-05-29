package cloudcomputing.cc.config;


import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Configuration
public class SnsPublisher {
    private final SnsClient snsClient;

    public SnsPublisher(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void sendEmail(String subject, String bodyText) {
        PublishRequest request = PublishRequest.builder()
                .topicArn("arn:aws:sns:us-east-1:814615723430:cc-sns")
                .subject(subject)
                .message(bodyText)
                .build();
        snsClient.publish(request);
    }

    public void subscribe(String userEmail){
        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .topicArn("arn:aws:sns:us-east-1:814615723430:cc-sns")
                .protocol("email")
                .endpoint(userEmail).build();
        snsClient.subscribe(subscribeRequest);
    }
}

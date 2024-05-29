package cloudcomputing.cc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class S3Config {
    private final S3Client s3Client;
    private final String bucketName = "cvs-ccproject";
    @Autowired
    public S3Config(S3Client s3Client){
        this.s3Client = s3Client;
    }
    public void saveInS3(String key, InputStream inputStream) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(key)
                .acl("public-read")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));
    }

}

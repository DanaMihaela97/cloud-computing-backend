package cloudcomputing.cc.service.impl;

import cloudcomputing.cc.service.BucketService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.List;
@Service
public class BucketImpl implements BucketService {
    @Autowired
    AmazonS3 s3Client;
    @Override
    public List<Bucket> getBucketList() {
        return s3Client.listBuckets();
    }

    @Override
    public void putObjectIntoBucket(String bucketName, String objectName, InputStream filePathToUpload) {
        try {
            s3Client.putObject(bucketName, objectName, filePathToUpload, null);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
    }


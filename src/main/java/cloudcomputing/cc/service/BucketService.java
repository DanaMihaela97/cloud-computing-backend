package cloudcomputing.cc.service;

import com.amazonaws.services.s3.model.Bucket;

import java.io.InputStream;
import java.util.List;

public interface BucketService {
    List<Bucket> getBucketList();

    void putObjectIntoBucket(String bucketName, String objectName, InputStream filePathToUpload);
}

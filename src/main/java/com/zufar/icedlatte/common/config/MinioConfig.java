package com.zufar.icedlatte.common.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${spring.minio.url}")
    private String minioUrl;

    @Value("${spring.minio.secure}")
    private boolean minioSecure;

    @Value("${spring.minio.access-key}")
    private String minioAccessKey;

    @Value("${spring.minio.secret-key}")
    private String minioSecretKey;

    @Value("${spring.minio.buckets.user-avatar}")
    private String minioAvatarBucket;

    @Value("${spring.minio.region}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(minioAccessKey, minioSecretKey);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(minioUrl, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        if (!amazonS3.doesBucketExistV2(minioAvatarBucket)) {
            amazonS3.createBucket(minioAvatarBucket);
        }

        return amazonS3;
    }
}

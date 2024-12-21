package com.atbmtt.l01.MetaStorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {
    private final AwsCredentials credentials;

    public AwsS3Config( @Value("${s3AccessKey}") String accessKey,@Value("${s3SecretKey}") String secretKey){
        credentials = AwsBasicCredentials.create(accessKey, secretKey);
    }
    @Bean
    public S3Client s3Client(){
        return S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}

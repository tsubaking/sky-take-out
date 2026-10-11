package com.sky.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.BasicCredentials;          // 新增导入
import com.aliyun.oss.common.auth.CredentialsProvider;      // 新增导入
import com.aliyun.oss.common.auth.DefaultCredentialProvider; // 新增导入
import com.aliyun.oss.common.comm.SignVersion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String region;

    public String upload(byte[] bytes, String objectName) {
        ClientBuilderConfiguration config = new ClientBuilderConfiguration();
        config.setSignatureVersion(SignVersion.V4);

        // 使用 BasicCredentials 包装 AK/SK，再通过 DefaultCredentialProvider 创建提供者
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
                new BasicCredentials(accessKeyId,accessKeySecret,null)
        );

        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(config)
                .region(region)
                .build();

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder.append(bucketName).append(".").append(endpoint).append("/").append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());
        return stringBuilder.toString();
    }
}
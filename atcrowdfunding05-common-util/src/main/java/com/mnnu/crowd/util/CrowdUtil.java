package com.mnnu.crowd.util;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.mnnu.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author qiaoh
 */
public class CrowdUtil {
    /**
     * 判断请求类型，根据请求的数据类型和X-Requested-With
     *
     * @param request
     * @return
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String XRheader = request.getHeader("X-Requested-With");
        return (accept != null && accept.contains("application/json")) ||
                (XRheader != null && XRheader.contains("XMLHttpRequest"));
    }

    /**
     * 加密密码为md5格式
     * 该方法已经被spring的passwordEncoder代替
     *
     * @param resource 明文的密码
     * @return 加密后的密码
     */
    @Deprecated
    public static String passwordEncoding(String resource) {

        //判断字符串是否合法
        if (resource == null || resource.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        try {
            //设置加密算法，获取执行加密的messageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            //将明文字符串转为标准的utf-8的字节数组，这样在存储中文的时候也不会出错
            //获取到加密后的字符串
            byte[] digest = messageDigest.digest(resource.getBytes(StandardCharsets.UTF_8));

            //因为字符数组直接转字符串是数字，所以转为bigInteger，十六进制字符串
            int signum = 1;
            BigInteger integer = new BigInteger(signum, digest);

            return integer.toString(16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 上传文件到oss
     *
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @param inputStream
     * @param bucketName
     * @param bucketDomain
     * @param originalName
     * @return 返回json数据，包含文件访问路径
     */
    public static ResultEntity<String> uploadFileToCloud(String endpoint,
                                                         String accessKeyId,
                                                         String accessKeySecret,
                                                         InputStream inputStream,
                                                         String bucketName,
                                                         String bucketDomain,
                                                         String originalName) {
        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在 OSS 服务器上保存时的文件名
        // 原始文件名： beautfulgirl.jpg
        // 生成文件名： wer234234efwer235346457dfswet346235.jpg
        // 使用 UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、 文件主体名称、 文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
            // 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName,
                    inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功， 获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultEntity.failed(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息 = " + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {
            if (ossClient != null) {
                // 关闭 OSSClient。
                ossClient.shutdown();
            }
        }
    }
}


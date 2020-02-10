package com.yada.btg.web.config;

import cfca.sadk.control.sip.ConfigurationException;
import cfca.sadk.control.sip.api.SIPDecryptionBuilder;
import cfca.sadk.control.sip.api.SIPDecryptor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author zsy
 * @date 2019/12/24
 */
@Component
public class RootConfig {
    @Value("${connCard.connectTimeout}")
    int connectTimeout;
    @Value("${connCard.readTimeout}")
    int readTimeout;
    @Value("${connCard.connectionRequestTimeout}")
    int connectionRequestTimeout;
    @Value("${connCard.maxConnTotal}")
    int maxConnTotal;
    @Value("${connCard.maxConnPerRoute}")
    int maxConnPerRoute;

    /**
     * 创建与连接卡系统模块交互的客户端
     *
     * @return 客户端
     */
    @Bean
    public RestTemplate restTemplate() {
        //maxConnTotal 是整个连接池的大小   maxConnPerRoute 是单个路由连接的最大数
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create()
                .setMaxConnTotal(maxConnTotal)
                .setMaxConnPerRoute(maxConnPerRoute)
                .build());
        //获取连接超时（单位毫秒）
        requestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        //连接超时（单位毫秒）
        requestFactory.setConnectTimeout(connectTimeout);
        //读取超时（单位毫秒）
        requestFactory.setReadTimeout(readTimeout);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        //设置编码格式为utf-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    /**
     * cfca服务端组件
     *
     * @throws ConfigurationException CFCA证书没找到
     * @throws FileNotFoundException  CFCA证书没找到
     */
    @Bean
    public SIPDecryptor sipDecryptor() throws ConfigurationException, FileNotFoundException {
        // 打成jar包后，文件路径不再是文件系统路径
        // SIPDecryptionBuilder.rsa().config(String, String)方法读取失败，抛出FileNotFoundException异常
        // 改为按流读取，使用SIPDecryptionBuilder.rsa().config(Byte[], String)方法
        InputStream reader = this.getClass().getClassLoader().getResourceAsStream("rsa1024_11111111.pfx");
        if (reader == null) {
            throw new FileNotFoundException("CFCA的证书未找到");
        }
        try {
            byte[] raw = new byte[reader.available()];
            reader.read(raw);
            final String rsa1024Pass = "11111111";
            return SIPDecryptionBuilder.rsa().config(raw, rsa1024Pass);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}

package com.graduation.user.utils.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.graduation.common.http.HttpUtils;
import com.graduation.common.redis.RedisUtils;
import org.apache.shiro.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class WxUtils {

    @Autowired
    private WxConfig wxConfig;

    private static final String TOKEN_KEY = "wx_mini_program_access_token";

    //根据code获取用户数据
    public JSONObject getSessionKeyAndOpenId(String code){
        Map<String, String> param = new HashMap<>();
        param.put("appid", wxConfig.APP_ID);
        param.put("secret", wxConfig.APP_SECRET);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        // 发送请求
        String wxResult = HttpUtils.doGet("https://api.weixin.qq.com/sns/jscode2session", param);
        return JSONObject.parseObject(wxResult);
    }

    //获取微信小程序的接口调用凭证
    public String getAccessToken(){
        //用于存token
        String token;

        //先检查redis里是否存了
        Object tokenObject = RedisUtils.get(TOKEN_KEY);

        //如果token过期，需要重新获取
        if(tokenObject == null){
            Map<String, String> param = new HashMap<>();
            param.put("appid", wxConfig.APP_ID);
            param.put("secret", wxConfig.APP_SECRET);
            param.put("grant_type", "client_credential");
            // 发送请求并将结果解析为json对象
            JSONObject jsonObject = JSONObject.parseObject(HttpUtils.doGet("https://api.weixin.qq.com/cgi-bin/token", param));
            //获取token
            token = jsonObject.getString("access_token");
            //获取token过期时间
            Long expire = jsonObject.getLong("expires_in");
            //将token缓存在redis
            //设置缓存过期时间比token过期时间略短, 这个过期时间不会很短，一定会大于10s，设置略短是防止处理过程中token过期
            RedisUtils.put(TOKEN_KEY, token, expire - 10L, TimeUnit.SECONDS);
        } else {
            token = tokenObject.toString();
        }

        return token;
    }

    //解密加密数据
    public JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSON.parseObject(result);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new JSONObject();
    }

}

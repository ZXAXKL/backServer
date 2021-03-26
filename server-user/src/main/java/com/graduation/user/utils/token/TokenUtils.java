package com.graduation.user.utils.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.redis.RedisHashUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class TokenUtils {

    @Autowired
    private RedisHashUtils redisHashUtils;

    //字符集
    public static String[] chars = new String[] {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B",
            "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z",};


    //有损压缩，压缩到8字节
    public String genShortToken() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    //压缩成字节数组
    public String shortUUID(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        BigInteger bigInteger = new BigInteger(uuid, 16);
        BigInteger radix = new BigInteger(String.valueOf(256));
        List<Byte> list = new ArrayList<>();
        while (bigInteger.longValue() != 0){
            Byte b = (byte) bigInteger.mod(radix).longValue();
            System.out.println(b);
            list.add(b);
            bigInteger = bigInteger.divide(radix);
        }

        int n = list.size();
        byte[] bytes = new byte[n];

        for(int i = 0; i < n; ++ i){
            bytes[i] = list.get(i);
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String tokenKey(Integer personId){
        return String.format("token_%s_%d", personId);
    }

    //把信息存入redis并附上token
    public String putInRedis(Map<String, Object> info, Integer personId){
        //生成token
        String token = genShortToken();
        log.info(String.format("%s%d登陆生成Token:%s", personId, token));
        //存入信息中
        info.put("token", token);
        //存redis
        redisHashUtils.put(tokenKey(personId), info, 20L);
        //返回token
        return token;
    }

    //修改token数据
    public void modify(Integer personId, String key, Object value){
        String k = tokenKey(personId);
        if(redisHashUtils.contain(k)){
            redisHashUtils.put(k, key, value);
        }
    }

    //删除token
    public void revoke(Integer personId){
        redisHashUtils.remove(tokenKey(personId));
    }

}

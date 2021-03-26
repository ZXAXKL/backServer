package com.graduation.common.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    //密码加密：md5+BASE64
    public static String encodePwd(String pwd) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(md5.digest(pwd.getBytes(StandardCharsets.UTF_8)));
    }

    //判断是否为空
    public static Boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //手机号格式检查
    public static Boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(166)|(17[0135678])|(18[0-9])|(19[8|9]))\\d{8}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(cellphone);
        return matcher.matches();
    }


    //解析spel表达式
    public static String generateKeyBySpEL(String spELString, ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        //获取方法参数定义名字
        String[] paramNames = new DefaultParameterNameDiscoverer().getParameterNames(methodSignature.getMethod());

        //SpEL表达式解析
        Expression expression = new SpelExpressionParser().parseExpression(spELString);
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        for(int i = 0 ; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return expression.getValue(context).toString();
    }

    //json字符串转对象
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String jsonStr, Class<T> tClass){
        try{
            if(isBlank(jsonStr) || tClass == null){
                return null;
            }

            return tClass.equals(String.class)? (T) jsonStr :objectMapper.readValue(jsonStr, tClass);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //判断是否是json字符串
    public static boolean isJSON(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //对象转json字符串
    public static <T> String objectToJson(T object){
        if(object == null){
            return null;
        }

        try {
            return object instanceof String ? (String) object : objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //检查imei格式
    public static Boolean checkIMEI (String IMEI) {
        try{
            int even = 0, odd = 0;
            for ( int i = 0; i < IMEI.length(); i++ )
            {
                int me = Integer.parseInt (String.valueOf(IMEI.charAt(i)));
                if (i % 2 == 0) {
                    even += me;
                }
                else {
                    String tmp = 2 * me + "";
                    for ( int j = 0; j < tmp.length (); j++ )
                    {
                        odd += Integer.parseInt (tmp.charAt (j) + "");
                    }
                }
            }
            int sum = even + odd;
            return sum % 10 == 0;
        }catch (Exception e){
            return false;
        }
    }

    //获取参数名到参数值的映射关系
    public static Map<String, Object> ParamMap(ProceedingJoinPoint joinPoint){
        //获取参数名参数值的映射关系
        Object[] params = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] paramNames = methodSignature.getParameterNames();

        Map<String, Object> map = new HashMap<>();
        int len = params.length;
        for(int i = 0; i < len; ++i){
            map.put(paramNames[i], params[i]);
        }

        return map;
    }

    //拼接字符串
    public static String glue(String... strs){
        StringBuilder result = new StringBuilder();
        for(String str : strs){
            result.append(str).append("_");
        }
        return result.deleteCharAt(result.length()-1).toString();
    }

    //对象转map
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();

        if(obj != null){
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                map.put(fieldName, value);
            }
        }

        return map;
    }
}

package com.graduation.user.utils.notify;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.HashMap;
import java.util.Map;

public class MobileMessageUtils {
    // 产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    // 产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    private static final String accessKeyId = "LTAI5tEvYZVWBgmnyQGQPBnx";
    private static final String accessKeySecret = "2TUgZ5w5KxlgjZNg2DuMGkTuF3WiDI";
    private static final String signName = "长沙维九科技有限公司";
    //验证码
    private static final String CodeTemplateCode = "SMS_180200117";


    public static void main(String[] args) {
        /*SendSmsResponse response = sendDeviceExpiringMsg("18670795280", 1, "1");//sendDeviceErrorMsg("18670795280", "869300035495096", "设备异常关机");
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=接口" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());*/
    }

    public static SendSmsResponse sendSms(String mobile, String templateParam, String templateCode) throws ClientException {
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();

        // 必填:待发送手机号
        request.setPhoneNumbers(mobile);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);

        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);

        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        // hint 此处可能会抛出异常，注意catch
        return acsClient.getAcsResponse(request);
    }

    //发验证码
    public static SendSmsResponse sendCodeMsg(String phone, String code){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            return sendSms(phone, JSONObject.toJSONString(map), CodeTemplateCode);
        } catch (ClientException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}


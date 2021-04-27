package com.graduation.device.log;

import com.graduation.common.format.FormatUtils;
import com.graduation.common.result.ResponseDto;
import com.graduation.device.logic.log.OperationRecorder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;

@Aspect
@Slf4j
@Component
public class OperationAspect {

    //依赖注入所有实现类
    @Autowired(required = false)
    private Map<String, OperationRecorder> recorders;

    //启动时执行，建立type和record实例的映射
    @PostConstruct
    public void init(){
        if(recorders != null){
            //存放type到对象的映射
            Map<String, OperationRecorder> map = new HashMap<>();
            //遍历注入的集合，把映射存入容器
            recorders.forEach((key, recorder) -> {
                map.put(recorder.type(), recorder);
            });
            //修改引用
            recorders = map;
        }
    }

    //环切，拦截所有加上Operation注解的方法
    @Around("@annotation(annotation)")
    public Object work(ProceedingJoinPoint joinPoint, Operation annotation) throws Throwable {
        //根据type获取实例
        OperationRecorder recorder = recorders.get(annotation.type());
        //判断是否有这个日志方法
        if(recorder == null){
            return joinPoint.proceed();
        }

        //拿请求头的数据
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        //先生成日志，因为这些参数可能被方法改变
        String log = recorder.execute(
                //这里取名字的时候要注意中文编码的问题，默认不支持中文，需要改编码方式
                URLDecoder.decode(request.getParameter("roomId"), "utf-8"),
                FormatUtils.ParamMap(joinPoint)
        );
        //执行目标方法获取结果
        ResponseDto result = (ResponseDto)joinPoint.proceed();
        //如果执行成功，则投递日志
        System.out.println(log);
        if(result.getCode() == ResponseDto.CODE_SUCCESS){
            recorder.delivery(Integer.parseInt(request.getParameter("roomId")), log);
        }
        //返回结果
        return result;
    }

}

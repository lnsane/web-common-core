package com.github.lnsane.web.common.core.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lnsane
 */
public class TimeIntervalAspect  {

    final static Logger log = LoggerFactory.getLogger(TimeIntervalAspect.class);

    @SneakyThrows
    public static void setServletContext() {
        Method webLog = ReflectUtil.getMethod(WebLogAspect.class, "webLog");
        Pointcut annotation = webLog.getAnnotation(Pointcut.class);
        log.debug(annotation.toString());
        InvocationHandler invocationHandler1 = Proxy.getInvocationHandler(annotation);
        Field hField = invocationHandler1.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        Map memberValues = (Map)hField.get(invocationHandler1);
        List<String> yaml = getYaml();
        String s = resolveList(yaml);
        log.debug(yaml.toString());
        memberValues.put("value",s);
        log.debug(annotation.toString());
    }

    public static String resolveList(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("execution(public * ");
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            stringBuilder.append(key);
            stringBuilder.append(".*.*(..))");
            if (i != list.size() - 1) {
                stringBuilder.append(" || ");
            }
        }
        return stringBuilder.toString();
    }

    public static List<String> getYaml() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        ObjectMapper andRegisterModules = objectMapper.findAndRegisterModules();
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:application.yml");
        } catch (FileNotFoundException e) {
            try {
                file = ResourceUtils.getFile("classpath:application.yml");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
            e.printStackTrace();
        }

        LinkedHashMap<String,LinkedHashMap> hashMap = (LinkedHashMap<String, LinkedHashMap>) andRegisterModules.readValue(file, Object.class);
        return getValue(hashMap, "rest-controller-advice-exception-list");
    }
    public static List<String> getValue(LinkedHashMap<String,LinkedHashMap> hashMapLinkedHashMap,String key) {
        List<String> hashMap = new ArrayList<>();
        for (Map.Entry<String, LinkedHashMap> entry : hashMapLinkedHashMap.entrySet()) {
            String s = entry.getKey();
            if (s.equals(key)) {
                Object o = entry.getValue();
                if (o instanceof  List) {
                    List o1 = (List) o;
                    return o1;
                }
            } else {
                Object value = entry.getValue();
                if (value instanceof LinkedHashMap) {
                    LinkedHashMap linkedHashMap = (LinkedHashMap) value;
                    if (CollUtil.isNotEmpty(linkedHashMap)) {
                        hashMap =  getValue(linkedHashMap,key);
                    }
                }
            }
        }
        return hashMap;
    }
}

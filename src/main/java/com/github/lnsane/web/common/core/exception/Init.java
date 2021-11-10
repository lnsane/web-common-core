package com.github.lnsane.web.common.core.exception;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.lnsane.web.common.config.WebCommonConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.ControllerAdviceBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lnsane
 */
//@Configuration
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//@ConditionalOnExpression("'${web.common.config.rest-controller-advice-exception-enable}'== true ")
public class Init {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private WebCommonConfigure webCommonConfigure;

    @Bean
    public ControllerAdviceBean globalException() throws NoSuchFieldException, IllegalAccessException {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
        ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();
//        AnnotationAttributes annotationAttributes = new AnnotationAttributes(ControllerAdvice.class);
//        annotationAttributes.put("basePackages", Collections.singletonList("anc"));
//        Class<? extends Annotation> aClass = annotationAttributes.annotationType();
//        ControllerAdvice annotation = AnnotationUtils.getAnnotation(controllerExceptionHandler.getClass(), ControllerAdvice.class);
//        AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(controllerExceptionHandler.getClass(), annotation);
//        MergedAnnotation<ControllerAdvice> controllerAdviceMergedAnnotation = MergedAnnotations
//                .from(controllerExceptionHandler.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
//                .get(ControllerAdvice.class);
//        ControllerAdvice controllerAdvice = controllerAdviceMergedAnnotation.synthesize();
        ControllerAdvice annotation1 = ControllerExceptionHandler.class.getAnnotation(ControllerAdvice.class);
        if (annotation1 == null) {
            return null;
        }
        InvocationHandler invocationHandler1 = Proxy.getInvocationHandler(annotation1);
        Field hField = invocationHandler1.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        Map memberValues = (Map)hField.get(invocationHandler1);
        List<String> restControllerAdviceExceptionList = webCommonConfigure.getRestControllerAdviceExceptionList();
        String[] listenController = new String[restControllerAdviceExceptionList.size()];
        for (int i = 0, restControllerAdviceExceptionListSize = restControllerAdviceExceptionList.size(); i < restControllerAdviceExceptionListSize; i++) {
            listenController[i] = restControllerAdviceExceptionList.get(i);
        }
        memberValues.put("basePackages",listenController);
        memberValues.put("value",listenController);
        return new ControllerAdviceBean("controllerExceptionHandler", beanFactory, annotation1);
    }
}

package org.course.bpp;

import lombok.SneakyThrows;
import org.course.annotation.Trimmed;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Trimmed.class)) {
            return createProxy(bean);
        }
        return bean;
    }

    @SneakyThrows
    private Object createProxy(Object bean) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (instance, method, args, methodProxy) -> {
            trimArgs(args);

            var result = methodProxy.invokeSuper(instance, args);

            if (result instanceof String s) {
                return s.trim();
            }
            return result;
        });
        var proxyBean = enhancer.create();
        copyBeanFields(bean, proxyBean);
        return proxyBean;
    }

    private void trimArgs(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            var arg = args[i];
            if (arg instanceof String s) {
                args[i] = s.trim();
            }
        }
    }

    @SneakyThrows
    private void copyBeanFields(Object bean, Object proxyBean) {
        var fields = bean.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (!Modifier.isStatic(f.getModifiers())) {
                f.setAccessible(true);
                f.set(proxyBean, f.get(bean));
            }
        }
    }
}

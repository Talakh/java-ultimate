package org.course.context;

import org.course.beans.EveningService;
import org.course.beans.GreetingService;
import org.course.beans.MorningService;
import org.course.exceptions.NoSuchBeanException;
import org.course.exceptions.NoUniqueBeanException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextImplTest {

    @Test
    void getBeanByClass() {
        ApplicationContext context = new ApplicationContextImpl("org.course");
        var bean = context.getBean(MorningService.class);
    }

    @Test
    void getBeanReturnMoreThanOneShouldThrowNoUniqueBeanException() {
        ApplicationContext context = new ApplicationContextImpl("org.course");
        assertThrows(NoUniqueBeanException.class, () -> context.getBean(GreetingService.class));
    }

    @Test
    void getBeansInIncorrectPathMoreThanOneShouldThrowNoSuchBeanException() {
        ApplicationContext context = new ApplicationContextImpl("org.cours");
        assertThrows(NoSuchBeanException.class, () -> context.getBean(GreetingService.class));
    }

    @Test
    void getBeanByDefaultClassName() {
        ApplicationContext context = new ApplicationContextImpl("org.course");
        var bean = context.getBean("morningService", GreetingService.class);
        assertTrue(bean instanceof MorningService);
    }

    @Test
    void getBeanByCustomName() {
        ApplicationContext context = new ApplicationContextImpl("org.course");
        var bean = context.getBean("evening", GreetingService.class);
        assertTrue(bean instanceof EveningService);
    }

    @Test
    void getBeanByWrongNameShouldThrowNoSuchBeanException() {
        ApplicationContext context = new ApplicationContextImpl("org.course");
        assertThrows(NoSuchBeanException.class, () -> context.getBean("morningSee", GreetingService.class));
    }

    @Test
    void getAllBeans() {
        ApplicationContext context = new ApplicationContextImpl("org.course");
        var beans = context.getAllBeans(GreetingService.class);
        assertEquals(3, beans.values().size());
    }
}
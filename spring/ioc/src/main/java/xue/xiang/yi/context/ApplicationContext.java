package xue.xiang.yi.context;

import xue.xiang.yi.annotation.Autowired;
import xue.xiang.yi.annotation.Component;
import xue.xiang.yi.annotation.ComponentScan;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : 薛向毅
 * @date : 14:00 2022/7/12
 */
public class ApplicationContext {

    private final Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>(16);
    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<String, Class<?>>(16);


    public ApplicationContext(Class<?> configClass) throws URISyntaxException {
        // 1.扫描配置信息中指定包下的类
        this.scan(configClass);
        // 2.实例化扫描到的类
        this.instantiateBean();
    }


    public void instantiateBean() {
        for (String beanName : classMap.keySet()) {
            getBean(beanName);
        }
    }

    public Object getBean(String beanName){
        // 先从缓存中获取
        Object bean = beanMap.get(beanName);
        if(bean != null){
            return bean;
        }
        return this.createBean(beanName);
    }

    private Object createBean(String beanName){
        Class<?> clazz = classMap.get(beanName);
        try {
            // 创建bean
            Object bean = this.doCreateBean(clazz);
            // 将bean存到容器中
            beanMap.put(beanName, bean);
            return bean;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private Object doCreateBean(Class<?> clazz) throws IllegalAccessException {
        // 实例化bean
        Object bean = this.newInstance(clazz);
        // 填充字段，将字段设值
        this.populateBean(bean, clazz);
        return bean;
    }

    /**
     * 支持无参数构造器, 和 有参数(必须使用component的注解对象)的构造器
     * @param clazz
     * @return
     */
    private Object newInstance(Class<?> clazz){
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length  ==  0) {
                try {
                    return constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            Object[] params = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                Object bean = getBean(parameterType);
                params[i] = bean;
            }
            try {
                return constructor.newInstance(params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
    private void populateBean(Object bean, Class<?> clazz) throws IllegalAccessException {
        // 解析class信息，判断类中是否有需要进行依赖注入的字段
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if(autowired != null){
                // 获取bean
                Object value = this.resolveBean(field.getType());
                field.setAccessible(true);
                field.set(bean, value);
            }
        }
    }

    private Object resolveBean(Class<?> clazz){
        // 先判断clazz是否为一个接口，是则判断classMap中是否存在子类
        if(clazz.isInterface()){
            // 暂时只支持classMap只有一个子类的情况
            for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
                if (clazz.isAssignableFrom(entry.getValue())) {
                    return getBean(entry.getValue());
                }
            }
            throw new RuntimeException("找不到可以进行依赖注入的bean");
        }else {
            return getBean(clazz);
        }
    }
    public Object getBean(Class<?> clazz){
        // 生成bean的名称
        String beanName = this.generateBeanName(clazz);
        // 此处对应最开始的getBean方法
        return this.getBean(beanName);
    }



    private void scan(Class<?> configClass) throws URISyntaxException {
        // 解析配置类，获取到扫描包路径
        String basePackages = this.getScanPackages(configClass);
        // 使用扫描包路径进行文件遍历操作
        this.doScan(basePackages);
    }

    private void doScan(String basePackages) throws URISyntaxException {
        // 获取资源信息

        URL uri = this.getResource(basePackages);
        File dir = new File(uri.getPath());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                // 递归扫描
                doScan(basePackages + "." + file.getName());
            }
            else {
                // xue.xiang.yi + . + Test.class -> xue.xiang.yi.Test
                String className = basePackages + "." + file.getName().replace(".class", "");
                // 将class存放到classMap中
                this.registerClass(className);
            }
        }
    }

    private URL getResource(String basePackages) throws URISyntaxException {
         basePackages = basePackages.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(basePackages);

        return resource;

    }


    private void registerClass(String className){
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // 加载类信息
            Class<?> clazz = classLoader.loadClass(className);
            // 判断是否标识Component注解
            if(clazz.isAnnotationPresent(Component.class)){
                //
                String beanName = this.generateBeanName(clazz);
                classMap.put(beanName, clazz);
            }
        } catch (ClassNotFoundException ignore) {}
    }

    private String generateBeanName(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        return simpleName;
    }


    private String getScanPackages(Class<?> configClass) {
        // 从ComponentScan注解中获取扫描包路径
        ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
        return componentScan.scanPackages();
    }


}

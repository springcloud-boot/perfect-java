package xue.xiang.yi.boot.cyclic.dependency;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 薛向毅
 * @date : 14:48 2022/7/27
 */
public class Test {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class[] classes = {A.class, B.class};

        for (Class aClass : classes) {
            Object bean = getBean(aClass);
        }

        System.out.println(getBean(A.class).getB());
        System.out.println(getBean(A.class));
        System.out.println(getBean(B.class));
        System.out.println(getBean(B.class).getA());


    }

    private static Map<String, Object> map = new HashMap<>();
    //拿到当前类
    private static<T> T getBean(Class<T> aClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String simpleName = aClass.getSimpleName();
        if (map.containsKey(simpleName)) {
           return (T)map.get(simpleName);
        }

        Object object = aClass.getDeclaredConstructor().newInstance();
        map.put(simpleName, object);

        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);

            Class<?> type = field.getType();
            String simpleName1 = type.getSimpleName();
            field.set(object, map.containsValue(simpleName1) ? map.get(simpleName1) : getBean(type));
        }

        return (T) object;

    }
}

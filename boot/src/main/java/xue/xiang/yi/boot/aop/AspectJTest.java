package xue.xiang.yi.boot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectJTest {
 
	@Pointcut("execution(* *.say(..))")
	public void test(){}
	
	@Before("test()")
	public void before(){
		System.out.println("before test..");
	}
	
	@After("test()")
	public void after(){
		System.out.println("after test..");
	}
	
	@Around("test()")
	public Object around(ProceedingJoinPoint p){
		System.out.println("before1");
		Object o = null;
		try {
			o = p.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("after1");
		return o;
	}
}
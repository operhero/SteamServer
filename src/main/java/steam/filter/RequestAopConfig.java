package steam.filter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
@Slf4j
@NoArgsConstructor
public class RequestAopConfig {

    @Autowired
    private HttpServletRequest request;

    private static final Logger log=LoggerFactory.getLogger(RequestAopConfig.class);

    private static final ThreadLocal<Long> START_TIME_MILLIS = new ThreadLocal<>();

    @Pointcut("execution(* steam..SteamApplication.*(..)) ")
    public void controllerMethodPointcut() {
    }

    /**
     * 前置通知:在某连接点之前执行的通知，但这个通知不能阻止连接点之前的执行流程（除非它抛出一个异常）。
     *
     * @param joinPoint 参数
     */
    @Before("controllerMethodPointcut()")
    public void before(JoinPoint joinPoint) {
        START_TIME_MILLIS.set(System.currentTimeMillis());
    }

    /**
     * 后置通知:在某连接点正常完成后执行的通知，通常在一个匹配的方法返回的时候执行。
     *
     * @param joinPoint 参数
     */
    @AfterReturning(value = "controllerMethodPointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String logTemplate = "--------------- 执行成功 ---------------\n请求开始---Send Request URL: {}, Method: {}, Params: {} \n请求方法---ClassName: {}, [Method]: {}, execution time: {}ms \n请求结束---Send Response Result: {}";
        log.info(logTemplate, request.getRequestURL(), request.getMethod(), getParams(joinPoint), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), (System.currentTimeMillis() - START_TIME_MILLIS.get()), result.toString());
        START_TIME_MILLIS.remove();
    }

    /**
     * 异常通知:在方法抛出异常退出时执行的通知。
     *
     * @param joinPoint 参数
     */
    @AfterThrowing(value = "controllerMethodPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        String logTemplate = "--------------- 执行失败 ---------------\n异常请求开始---Send Request URL: {}, Method: {}, Params: {} \n异常请求方法---ClassName: {}, [Method]: {}, execution time: {}ms \n异常请求结束---Exception Message: {}";
        log.error(logTemplate, request.getRequestURL(), request.getMethod(), getParams(joinPoint), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), (System.currentTimeMillis() - START_TIME_MILLIS.get()), ex.getMessage());
        START_TIME_MILLIS.remove();
    }

    /**
     * 最终通知。当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。
     *
     * @param joinPoint
     */
    @After("controllerMethodPointcut()")
    public void after(JoinPoint joinPoint) {
    }

    private String getParams(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        try{
            Object[] args = joinPoint.getArgs();
            String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();

            for(int i =0;i<args.length;i++){
                if(args[i] instanceof HttpServletRequest){
                    continue;
                }
                if(sb.length()>0){
                    sb.append("&");
                }
                sb.append(argNames[i]).append("=").append((String) args[i]);
            }
        }catch (Exception e){

        }finally {
            return sb.toString();
        }
    }
}

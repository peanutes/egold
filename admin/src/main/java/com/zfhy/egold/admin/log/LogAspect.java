package com.zfhy.egold.admin.log;

import com.google.gson.Gson;
import com.zfhy.egold.admin.shiro.ShiroUtil;
import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.common.util.ThreadUtil;
import com.zfhy.egold.domain.sys.entity.AdminLog;
import com.zfhy.egold.domain.sys.service.AdminLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogAspect {


	@Autowired
	private AdminLogService adminLogService;


	@Pointcut("@annotation(com.zfhy.egold.admin.log.Log)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {


		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String ip = RequestUtil.getIpAddress(request);

		long beginTime = System.currentTimeMillis();
		
		Object result = point.proceed();
		
		long time = System.currentTimeMillis() - beginTime;
		
		ThreadUtil.getThreadPollProxy().execute(() -> saveLog(point, time, result, ip));




		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time, Object result, String ip) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		AdminLog sysLog = new AdminLog();

		Log syslog = method.getAnnotation(Log.class);
		if (syslog != null) {
			
			sysLog.setRemarks(syslog.value());
		}
		
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setOperateMethod(className + "." + methodName + "()");
		
		Object[] args = joinPoint.getArgs();
		try {
			sysLog.setOperateInput(new Gson().toJson(args));
		} catch (Exception e) {

		}
		
		sysLog.setLoginAccount(ShiroUtil.getLogginAccount());

		sysLog.setIp(ip);


		String outPut = new Gson().toJson(result);

		if (outPut.length() > 1000) {
			outPut = outPut.substring(0, 1000);
		}
		sysLog.setOperateOutput(outPut);

		sysLog.setTime(time);

		
		sysLog.setCreateDate(new Date());
		
		adminLogService.save(sysLog);
	}
}

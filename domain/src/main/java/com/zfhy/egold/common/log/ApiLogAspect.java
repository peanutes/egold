package com.zfhy.egold.common.log;

import com.google.gson.Gson;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.util.RequestUtil;
import com.zfhy.egold.common.util.ThreadUtil;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.OperateLog;
import com.zfhy.egold.domain.member.service.OperateLogService;
import com.zfhy.egold.domain.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ApiLogAspect {


	@Autowired
	private OperateLogService operateLogService;

	@Autowired
	private RedisService redisService;

	@Pointcut("@annotation(com.zfhy.egold.common.log.ApiLog)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {


		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String ip = RequestUtil.getIpAddress(request);

		String terminalId = request.getParameter("terminalId");
		String terminalType = request.getParameter("terminalType");
		String token = request.getParameter("token");
		String sessionId = request.getSession().getId();
		OperateLog operateLog = new OperateLog();

		operateLog.setTerminalType(terminalType);
		operateLog.setTerminalId(terminalId);
		operateLog.setSessionId(sessionId);
		operateLog.setToken(token);
		operateLog.setIp(ip);

		if (StringUtils.isNotBlank(token)) {
			MemberSession memberSession = this.redisService.getMemberSession(token, request);
			if (Objects.nonNull(memberSession)) {
				operateLog.setMobile(memberSession.getMobilePhone());
				operateLog.setMemberId(memberSession.getId());
			}

		}

		long beginTime = System.currentTimeMillis();
		
		Object result = null;
		Throwable ex = null;
		try {
			result = point.proceed();
		} catch (Throwable throwable) {
			ex = throwable;
			throw throwable;
		} finally {
			
			long time = System.currentTimeMillis() - beginTime;
			
			try {
				final Object finalResult = result;
				final Throwable finalEx = ex;




				ThreadUtil.getThreadPollProxy().execute(() -> saveLog(point, time, finalResult, finalEx, operateLog));
			} catch (Throwable throwable) {
				log.error("保存操作日志失败", throwable);
			}
		}

		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time, Object result, Throwable finalEx, OperateLog operateLog) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();




		ApiLog syslog = method.getAnnotation(ApiLog.class);
		boolean excludeInput = false;
		if (syslog != null) {
			
			operateLog.setOperateMethodStr(syslog.value());
			excludeInput = syslog.excludeInput();
		}
		
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		operateLog.setOperateMethod(String.join(".", className, methodName));

		
		Object[] args = joinPoint.getArgs();
		try {
			String argument = new Gson().toJson(args);
			if (!excludeInput) {
				operateLog.setOperateInput(argument);
			}
			log.info("方法:{}执行入参>>>>{}", String.join(".", className, methodName), argument);
		} catch (Exception e) {

		}
		




		/*if (outPut.length() > 1000) {
			outPut = outPut.substring(0, 1000);
		}*/

		if (Objects.nonNull(result)) {

			String outPut = new Gson().toJson(result);
			log.info("执行结果>>>>{}", outPut);

			if (result instanceof Result) {
				
				Result rs = (Result) result;

				operateLog.setOperateResult(rs.getCode());
			} else {
				operateLog.setOperateResult(ResultCode.SUCCESS.getCode());
			}
		}

		if (Objects.nonNull(finalEx)) {
			operateLog.setOperateResult(ResultCode.INTERNAL_SERVER_ERROR.getCode());
			String message = finalEx.getMessage();
			if (StringUtils.isNotBlank(message) && message.length() > 1000) {
				message = message.substring(0, 1000);
			}
			operateLog.setExceptionMsg(message);

		}




		operateLog.setExecuteTime(time);
		
		operateLog.setCreateDate(new Date());
		
		operateLogService.save(operateLog);
	}
}

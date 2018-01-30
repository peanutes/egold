<!DOCTYPE html>
<html>
<head lang="zh_CN" xmlns:th="http://www.thymeleaf.org"
      xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>系统工具</title>
<link rel="stylesheet" href="/css/bootstrap.min.css" />
</head>
<body>
<nav class="navbar navbar-default navbar-static-top">
	<ul class="nav navbar-nav">
		<li><a href="/job/list">调度管理</a></li>
		<li><a href="#">调度日志</a></li>
		<li><a href="/druid">数据源监控</a></li>
		<li class="active"><a href="#">系统工具</a></li>
	</ul>
</nav>
<div class="container-fluid">
	<form action="/system/invoke" method="POST" class="form-horizontal" onsubmit="return toInvoke()">
  		<div class="form-group">
			<label for="invokeName" class="col-sm-2 control-label">ID或类</label>
		    <div class="col-sm-10">
		      	<select class="form-control" id="invokeName" name="invokeName">
		      		<option value="">请选择</option>
		      		<#list map?keys as key>
		      			<option value="${key}">${map[key]}</option>
		      		</#list>
		      	</select>
		    </div>
  		</div>
  		
  		<div class="form-group">
		    <label for="invokeParams" class="col-sm-2 control-label">参数</label>
		    <div class="col-sm-10">
		      <input type="type" class="form-control" id="invokeParams" name="invokeParams" placeholder="参数(paramName=value)，多个用空格隔开">
		    </div>
		</div>
		
		<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		    	<#if (running <= 1)>
		    		<button type="submit" class="btn btn-default">执行</button>
		    	</#if>
		    </div>
	    </div>
	    
	    <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		    	<#if running == 1>
		    		<div>最后一次运行的任务：</div>
		    	<#elseif running == 2>
		    		<div style="color:red;">当前正在运行的任务是：</div>
		    	</#if>
		    	<#if (running>0)>
		    		<table class="table table-bordered">
		    			<tr>
		    				<td style="width:100px;">执行方法</td>
		    				<td>${info['runinfo'].name!}</td>
		    			</tr>
		    			<tr>
		    				<td>执行参数</td>
		    				<td>${info['runinfo'].params!}</td>
		    			</tr>
		    			<tr>
		    				<td>起始时间</td>
		    				<td>${info['runinfo'].startTime}</td>
		    			</tr>
		    			<tr>
		    				<td>结束时间</td>
		    				<td>${info['runinfo'].endTime!}</td>
		    			</tr>
		    			<tr>
		    				<td>状       态</td>
		    				<td>${info['runinfo'].status!}</td>
		    			</tr>
		    			<tr>
		    				<td>错误信息</td>
		    				<td>${info['runinfo'].errMsg!}</td>
		    			</tr>
		    		</table>
		    	</#if>
		    </div>
	    </div>
	</form>
</div>
<script type="text/javascript">
	function toInvoke()
	{
		var invokeName = document.getElementById('invokeName').value;
		if(invokeName == '')
		{
			alert("请先选择需要执行的类");
			return false;
		}
		return confirm("确定要执行该任务");
	}
</script>
</body>
</html>
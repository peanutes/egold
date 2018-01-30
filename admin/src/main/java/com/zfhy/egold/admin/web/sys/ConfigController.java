package com.zfhy.egold.admin.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.sys.entity.Config;
import com.zfhy.egold.domain.sys.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/sys/config")
@Controller
public class ConfigController {
	String prefix = "sys/config";
	@Autowired
	ConfigService configService;

	@RequiresPermissions("sys:config:config")
	@GetMapping()
	String config() {
		return prefix + "/config";
	}


	@RequiresPermissions("sys:config:config")
	@GetMapping("/list")
	@ResponseBody
	PageUtils list(@RequestParam Map<String, String> params) {

		PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
		Condition condition = new Condition(Config.class);
		Example.Criteria criteria = condition.createCriteria();
		List<Map.Entry<String, String>> parameters = params.entrySet().stream()
				.filter(entry -> StringUtils.isNotBlank(entry.getValue()))
				.filter(entry -> !"page".equalsIgnoreCase(entry.getKey()) && !"size".equalsIgnoreCase(entry.getKey()))
				.collect(Collectors.toList());

		for (Map.Entry<String, String> entry : parameters) {
			criteria.andEqualTo(entry.getKey(), entry.getValue());
		}

		List<Config> configList = this.configService.findByCondition(condition);
		PageInfo<Config> pageInfo = new PageInfo<>(configList);

		return new PageUtils(configList, (int)pageInfo.getTotal());
	}

	@RequiresPermissions("sys:config:add")
	@GetMapping("/add")
	String add() {
		return prefix + "/add";
	}

	@RequiresPermissions("sys:config:edit")
	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Integer id, Model model) {
		Config config = configService.findById(id);
		model.addAttribute("config", config);
		return prefix + "/edit";
	}

	@RequiresPermissions("sys:config:add")
	@PostMapping("/save")
	@ResponseBody()
	Result<String> save(Config config) {

		config.setCreateDate(new Date());
		this.configService.save(config);
		return ResultGenerator.genSuccessResult();
	}

	@RequiresPermissions("sys:config:edit")
	@PostMapping("/update")
	@ResponseBody()
	Result<String> update(Config config) {
		this.configService.update(config);

		return ResultGenerator.genSuccessResult();
	}

	@RequiresPermissions("sys:config:remove")
	@PostMapping("/remove")
	@ResponseBody()
	Result<String> remove(Integer id) {
		

		this.configService.deleteById(id);

		return ResultGenerator.genSuccessResult();
	}
}

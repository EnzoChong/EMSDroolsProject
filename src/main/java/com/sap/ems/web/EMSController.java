package com.sap.ems.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.ems.dto.EMSResult;
import com.sap.ems.dto.RuleDto;
import com.sap.ems.entity.Entitlement;
import com.sap.ems.entity.SalesOrder;
import com.sap.ems.service.EMSService;
import com.sap.ems.service.RuleEngine;

@Controller
@RequestMapping("/drools")
public class EMSController {

	// Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EMSService emsService;
	@Autowired
	private RuleEngine ruleEngine;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<List<Map>> getMappingFields() {
		EMSResult<List<Map>> result;
		List<Map> map = emsService.getMappingFields();
		result = new EMSResult<List<Map>>(true, map);
		return result;
	};

	@RequestMapping(value = "/allrules", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<List<RuleDto>> getAllRules() {
		EMSResult<List<RuleDto>> result;
		List<RuleDto> rules = emsService.getAllRules();
		result = new EMSResult<List<RuleDto>>(true, rules);
		return result;
	}

	@RequestMapping(value = "/rule/{ruleId}/detail", method = RequestMethod.GET, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<RuleDto> getRuleById(@PathVariable("ruleId") Long ruleId) {
		EMSResult<RuleDto> result;
		RuleDto rule = null;

		if (ruleId == null) {
			return result = new EMSResult<>(false, "Rule ID is not correct!");
		}

		rule = emsService.getRuleById(ruleId);

		if (rule == null) {
			return result = new EMSResult<>(false, "Rule ID is not correct!");
		}

		result = new EMSResult<RuleDto>(true, rule);
		return result;
	}

	@RequestMapping(value = "/rule/{ruleId}/detail", method = RequestMethod.DELETE, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<Integer> deleteRuleById(@PathVariable("ruleId") Long ruleId) {
		EMSResult<Integer> result;
		Integer num = 0;

		if (ruleId == null) {
			return result = new EMSResult<>(false, "Rule ID is not correct!");
		}

		num = emsService.deleteRule(ruleId);

		if (num == 0) {
			return result = new EMSResult<>(false, "Delete rule is not correct!");
		}

		result = new EMSResult<Integer>(true, num);
		return result;
	}

	@RequestMapping(value = "/rule", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<RuleDto> insertRule(@RequestBody RuleDto rule) {
		EMSResult<RuleDto> result;
		RuleDto ruleDto = emsService.insertRule(rule);
		result = new EMSResult<RuleDto>(true, ruleDto);
		return result;
	}

	@RequestMapping(value = "/rule", method = RequestMethod.PUT, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<RuleDto> updateRule(@RequestBody RuleDto rule) {
		EMSResult<RuleDto> result;
		RuleDto ruleDto = emsService.updateRule(rule);
		result = new EMSResult<RuleDto>(true, ruleDto);
		return result;
	}

	@RequestMapping(value = "/rule/appliance", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<Integer> applyRule() {
		EMSResult<Integer> result = ruleEngine.applyRuleChanges();
		return result;
	}

	@RequestMapping(value = "/rule/apply", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public EMSResult<Entitlement> applyAll(@RequestBody SalesOrder salesOrder) {
		EMSResult<Entitlement> result = ruleEngine.applyAllRules(salesOrder);
		return result;
	}

}
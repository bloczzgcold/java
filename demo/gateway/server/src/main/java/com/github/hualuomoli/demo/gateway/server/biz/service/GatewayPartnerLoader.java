package com.github.hualuomoli.demo.gateway.server.biz.service;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader;
import com.google.common.collect.Maps;

@Service(value = "com.github.hualuomoli.demo.gateway.server.biz.service.GatewayPartnerLoader")
@Transactional(readOnly = true)
public class GatewayPartnerLoader implements PartnerLoader {

	private static final Map<String, Partner> map = Maps.newHashMap();

	@Autowired
	private PartnerService partnerService;

	@Override
	public Partner load(String partnerId) {

		Partner p = map.get(partnerId);
		if (p != null) {
			return p;
		}

		com.github.hualuomoli.demo.gateway.server.biz.entity.Partner partner = partnerService.findByPartnerId(partnerId);
		if (partner == null) {
			return null;
		}

		Map<Key, String> configs = Maps.newHashMap();
		JSONObject cs = JSON.parseObject(partner.getConfigs());
		Set<String> keys = cs.keySet();
		for (String key : keys) {
			Key k = EnumUtils.getEnum(Key.class, key);
			configs.put(k, cs.getString(key));
		}

		p = new Partner(partnerId, partner.getName(), configs);
		map.put(partnerId, p);

		return p;
	}

}

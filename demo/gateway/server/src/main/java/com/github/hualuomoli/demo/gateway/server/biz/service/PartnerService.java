package com.github.hualuomoli.demo.gateway.server.biz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.demo.gateway.server.biz.entity.Partner;
import com.github.hualuomoli.demo.gateway.server.biz.mapper.PartnerMapper;

@Service(value = "com.github.hualuomoli.demo.gateway.server.biz.service.PartnerService")
@Transactional(readOnly = true)
public class PartnerService {

	@Autowired
	private PartnerMapper partnerMapper;

	public Partner get(String id) {
		return partnerMapper.get(id);
	}

	public Partner findByPartnerId(String partnerId) {
		Partner partner = new Partner();
		partner.setPartnerId(partnerId);
		List<Partner> list = partnerMapper.findList(partner);

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
	}

}

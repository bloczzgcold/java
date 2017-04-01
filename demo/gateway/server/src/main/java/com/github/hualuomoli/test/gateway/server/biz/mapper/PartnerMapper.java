package com.github.hualuomoli.test.gateway.server.biz.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.hualuomoli.test.gateway.server.biz.entity.Partner;

@Repository(value = "com.github.hualuomoli.test.gateway.server.biz.mapper.PartnerMapper")
public interface PartnerMapper {

	Partner get(String id);

	List<Partner> findList(Partner partner);

}

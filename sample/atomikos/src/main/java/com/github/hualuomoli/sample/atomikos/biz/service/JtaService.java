package com.github.hualuomoli.sample.atomikos.biz.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.sample.atomikos.ds1.base.entity.User;
import com.github.hualuomoli.sample.atomikos.ds1.base.mapper.UserBaseMapper;
import com.github.hualuomoli.sample.atomikos.ds2.base.entity.Address;
import com.github.hualuomoli.sample.atomikos.ds2.base.mapper.AddressBaseMapper;

@Service(value = "com.github.hualuomoli.sample.atomikos.biz.service.JtaService")
@Transactional(readOnly = true)
public class JtaService {

  @Autowired
  private UserBaseMapper userBaseMapper;
  @Autowired
  private AddressBaseMapper addressBaseMapper;

  public void execute() {
    User user = new User();
    user.setId(UUID.randomUUID().toString().replaceAll("[-]", ""));
    user.setAge(18);
    user.setUsername("hualuomoli");
    user.setNickname("花落莫离");
    userBaseMapper.insert(user);

    Address address = new Address();
    address.setId(UUID.randomUUID().toString().replaceAll("[-]", ""));
    address.setAreaCode("370203");
    address.setProvince("山东省");
    address.setCity("青岛市");
    address.setCounty("市北区");
    address.setDetailAddress("合肥路666号");
    addressBaseMapper.insert(address);
  }

  public void secondError() {
    User user = new User();
    user.setId(UUID.randomUUID().toString().replaceAll("[-]", ""));
    user.setAge(18);
    user.setUsername("hualuomoli");
    user.setNickname("花落莫离");
    userBaseMapper.insert(user);

    Address address = new Address();
    address.setAreaCode("370203");
    address.setProvince("山东省");
    address.setCity("青岛市");
    address.setCounty("市北区");
    address.setDetailAddress("合肥路666号");
    addressBaseMapper.insert(address);
  }

  public void firstError() {
    User user = new User();
    user.setAge(18);
    user.setUsername("hualuomoli");
    user.setNickname("花落莫离");
    userBaseMapper.insert(user);

    Address address = new Address();
    address.setId(UUID.randomUUID().toString().replaceAll("[-]", ""));
    address.setAreaCode("370203");
    address.setProvince("山东省");
    address.setCity("青岛市");
    address.setCounty("市北区");
    address.setDetailAddress("合肥路666号");
    addressBaseMapper.insert(address);
  }

}

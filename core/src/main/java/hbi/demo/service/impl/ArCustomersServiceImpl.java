package hbi.demo.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hbi.demo.dto.ArCustomers;
import hbi.demo.mapper.ArCustomersMapper;
import hbi.demo.service.IArCustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Transactional(rollbackFor = Exception.class)
public class ArCustomersServiceImpl extends BaseServiceImpl<ArCustomers> implements IArCustomersService{
    @Autowired
    private ArCustomersMapper arCustomersMapper;
    @Override
    public String getCustomerNameById(Long customerId) {
        return arCustomersMapper.getCustomerNameById(customerId);
    }
}
package hbi.demo.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hbi.demo.mapper.ArCustomersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hbi.demo.dto.ArCustomers;
import hbi.demo.service.IArCustomersService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArCustomersServiceImpl extends BaseServiceImpl<ArCustomers> implements IArCustomersService{
    @Autowired
    private ArCustomersMapper arCustomersMapper;
    @Override
    public String getCustomerNameById(Long customerId) {
        return arCustomersMapper.getCustomerNameById(customerId);
    }
}
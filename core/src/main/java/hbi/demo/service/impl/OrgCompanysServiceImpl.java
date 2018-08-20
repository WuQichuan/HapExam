package hbi.demo.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hbi.demo.mapper.OrgCompanysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hbi.demo.dto.OrgCompanys;
import hbi.demo.service.IOrgCompanysService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrgCompanysServiceImpl extends BaseServiceImpl<OrgCompanys> implements IOrgCompanysService{
    @Autowired
    private OrgCompanysMapper orgCompanysMapper;

    @Override
    public String getCompanyNameById(Long companyId) {
        return orgCompanysMapper.getCompanyNameById(companyId);
    }

}
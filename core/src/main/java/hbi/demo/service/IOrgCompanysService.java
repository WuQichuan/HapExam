package hbi.demo.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hbi.demo.dto.OrgCompanys;

public interface IOrgCompanysService extends IBaseService<OrgCompanys>, ProxySelf<IOrgCompanysService>{
    /* *
         * 通过公司ID获得公司名称
         * @author 武琦川@hand-china.com
         * @date  2018/8/11 14:13
         * @param 公司ID companyId
         * @return 公司名称
         */
    String getCompanyNameById(Long companyId);
}
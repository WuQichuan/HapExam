package hbi.demo.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hbi.demo.dto.ArCustomers;

public interface IArCustomersService extends IBaseService<ArCustomers>, ProxySelf<IArCustomersService>{

    /* *
     * 通过客户ID获得客户名称
     * @author 武琦川@hand-china.com
     * @date  2018/8/11 14:27
     * @param [customerId]
     * @return java.lang.String
     */
    String getCustomerNameById(Long customerId);
}
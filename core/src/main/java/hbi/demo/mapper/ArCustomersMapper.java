package hbi.demo.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hbi.demo.dto.ArCustomers;

public interface ArCustomersMapper extends Mapper<ArCustomers>{

    /* *
     * 通过客户ID获得客户名称
     * @author 武琦川@hand-china.com
     * @date  2018/8/11 14:27
     * @param 客户ID customerId
     * @return 客户名称
     */
    String getCustomerNameById(Long customerId);

}
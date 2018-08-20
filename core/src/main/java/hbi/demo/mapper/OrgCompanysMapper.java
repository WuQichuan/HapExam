package hbi.demo.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hbi.demo.dto.OrgCompanys;

public interface OrgCompanysMapper extends Mapper<OrgCompanys>{
    /* *
     * 通过公司ID获得公司名称
     * @author 武琦川@hand-china.com
     * @date  2018/8/11 14:13
     * @param [companyId]
     * @return java.lang.String
     */
    String getCompanyNameById(Long companyId);

}
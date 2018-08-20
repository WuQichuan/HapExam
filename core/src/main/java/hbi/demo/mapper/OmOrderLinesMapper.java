package hbi.demo.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hbi.demo.dto.OmOrderLines;

import java.util.List;

public interface OmOrderLinesMapper extends Mapper<OmOrderLines>{

    /* *
     *
     * 通过订单头的ID，获得订单头所属的订单行集合（只带id和金额，数量）
     * @author 武琦川@hand-china.com
     * @date  2018/8/11 14:43
     * @param 订单头的ID headerId
     * @return j订单行集合（只带id和金额，数量）
     */
    List<OmOrderLines> getSumPriceByHeaderId(Long headerId);

    /* *
       * 根据头订单ID和物料ID查询行订单的数量
       * @author 武琦川@hand-china.com
       * @date  2018/8/11 16:46
       * @param 存储着订单ID和物料ID的 orderLines
       * @return 订单的数量 java.lang.Integer
       */
    Integer getCountByHeaderIdAndInventoryItemId(OmOrderLines orderLines);

    /* *
     * 改变公司ID
     * @author 武琦川@hand-china.com
     * @date  2018/8/20 13:33
     * @param [orderLines]
     * @return void
     */
    void changeCompanyId (OmOrderLines orderLines);
    /* *
     * 通过头订单ID获得该头订单所有的行订单的最大行号
     * @author 武琦川@hand-china.com
     * @date  2018/8/12 13:35
     * @param 头订单ID headerId
     * @return 行订单的最大行号
     */
    Long getMaxLineNumber(Long headerId);
}
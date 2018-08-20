package hbi.demo.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hbi.demo.dto.InvInventoryItems;

public interface InvInventoryItemsMapper extends Mapper<InvInventoryItems>{

    /* *
     * 通过物料编码获得物料ID
     * @author 武琦川@hand-china.com
     * @date  2018/8/12 1:36
     * @param 物料编码 itemCode
     * @return 物料ID java.lang.Long
     */
    Long getInventoryItemIdByCode(String itemCode);
}
package hbi.demo.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hbi.demo.dto.InvInventoryItems;

public interface IInvInventoryItemsService extends IBaseService<InvInventoryItems>, ProxySelf<IInvInventoryItemsService>{

    /* *
     * 通过物料编码获得物料ID
     * @author 武琦川@hand-china.com
     * @date  2018/8/12 1:36
     * @param [itemCode]
     * @return java.lang.Long
     */
    Long getInventoryItemIdByCode(String itemCode);
}
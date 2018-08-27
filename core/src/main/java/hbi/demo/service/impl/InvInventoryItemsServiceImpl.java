package hbi.demo.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hbi.demo.dto.InvInventoryItems;
import hbi.demo.mapper.InvInventoryItemsMapper;
import hbi.demo.service.IInvInventoryItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Transactional(rollbackFor = Exception.class)
public class InvInventoryItemsServiceImpl extends BaseServiceImpl<InvInventoryItems> implements IInvInventoryItemsService{
    @Autowired
    private InvInventoryItemsMapper invInventoryItemsMapper;
    @Override
    public Long getInventoryItemIdByCode(String itemCode) {
        return invInventoryItemsMapper.getInventoryItemIdByCode(itemCode);
    }
}
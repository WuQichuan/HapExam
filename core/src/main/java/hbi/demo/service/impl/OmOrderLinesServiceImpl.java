package hbi.demo.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hbi.demo.dto.OmOrderLines;
import hbi.demo.mapper.OmOrderLinesMapper;
import hbi.demo.service.IOmOrderLinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OmOrderLinesServiceImpl extends BaseServiceImpl<OmOrderLines> implements IOmOrderLinesService{

    @Autowired
    private OmOrderLinesMapper omOrderLinesMapper;
    @Override
    public Long getSumPriceByHeaderId(Long headerId) {
        List<OmOrderLines> sumPriceByHeaderId = omOrderLinesMapper.getSumPriceByHeaderId(headerId);
        Long sumPrice = 0L;
        for (OmOrderLines orderLines: sumPriceByHeaderId) {
            sumPrice = sumPrice + orderLines.getUnitSellingPrice()*orderLines.getOrderdQuantity();
        }

        return sumPrice;
    }

    @Override
    public Integer getCountByHeaderIdAndInventoryItemId(OmOrderLines orderLines) {
        return omOrderLinesMapper.getCountByHeaderIdAndInventoryItemId(orderLines);
    }

    @Override
    public Long getMaxLineNumber(Long headerId) {
        return omOrderLinesMapper.getMaxLineNumber(headerId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeCompanyId(OmOrderLines orderLines) {
        omOrderLinesMapper.changeCompanyId(orderLines);
    }
}
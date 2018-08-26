package hbi.demo.service.impl;

import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hbi.demo.dto.InvInventoryItems;
import hbi.demo.dto.OmOrderHeaders;
import hbi.demo.dto.OmOrderLines;
import hbi.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OmOrderHeadersServiceImpl extends BaseServiceImpl<OmOrderHeaders> implements IOmOrderHeadersService{
    @Autowired
    private IOrgCompanysService orgCompanysService;
    @Autowired
    private IArCustomersService arCustomersService;
    @Autowired
    private IOmOrderLinesService omOrderLinesService;
    @Autowired
    private IOmOrderHeadersService orderHeadersService;
    @Autowired
    private IInvInventoryItemsService iInvInventoryItemsService;
    @Autowired
    ISysCodeRuleProcessService codeRuleProcessService;
    @Override
    public void getNamesAndAmount(List<OmOrderHeaders> orderHeadersList) {
        if(orderHeadersList != null && !orderHeadersList.isEmpty()){
            for (OmOrderHeaders orderHeaders: orderHeadersList) {
                //如果公司ID不为空的话，使其通过公司ID获得公司名称
                if(orderHeaders.getCompanyId()!=null){
                    String companyNameById = orgCompanysService.getCompanyNameById(orderHeaders.getCompanyId());
                    orderHeaders.setCompanyName(companyNameById);
                }
                //如果客户ID不为空的话，使其通过客户ID获得客户名称
                if(orderHeaders.getCustomerId()!=null){
                    String customerNameById = arCustomersService.getCustomerNameById(orderHeaders.getCustomerId());
                    orderHeaders.setCustomerName(customerNameById);
                }
                //统计头订单的总金额
                Long sumPriceByHeaderId = omOrderLinesService.getSumPriceByHeaderId(orderHeaders.getHeaderId());
                orderHeaders.setOrderAmount(sumPriceByHeaderId);


            }
        }
    }



    @Override
    public List<OmOrderHeaders> filterByInventoryItemId(List<OmOrderHeaders> orderHeadersList,Long inventoryItemId) {
       //用来存放筛选数据的集合
        List<OmOrderHeaders> list = new ArrayList<>();
        if(orderHeadersList != null && !orderHeadersList.isEmpty()){
            for (OmOrderHeaders orderHeaders: orderHeadersList){
                //创建OmOrderLines对象用来封装查询条件
                OmOrderLines condition = new OmOrderLines();
                condition.setHeaderId(orderHeaders.getHeaderId());
                condition.setInventoryItemId(inventoryItemId);
                //根据头订单ID和物料ID对行订单进行数量筛选
                Integer count = omOrderLinesService.getCountByHeaderIdAndInventoryItemId(condition);
                //如果数量大于0，说明该头订单下拥有着带该物料ID的行订单，则放入返回集合
                if(count>0){
                    list.add(orderHeaders);
                }
            }
        }
        //返回筛选好的集合
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<OmOrderHeaders> myBatchUpdate(IRequest iRequest, List<OmOrderHeaders> orderHeadersList) {
        if(orderHeadersList != null && !orderHeadersList.isEmpty()){
            for (OmOrderHeaders orderHeaders: orderHeadersList) {
                Long headerId = orderHeaders.getHeaderId();
                //根据是否有头ID来判断是更新还是新建
                if(headerId ==null || headerId == 0){
                    //说明是新建
                    //保存头,并且把订单状态设定为新建
                    orderHeaders.setOrderStatus("NEW");
                    //使用编码规则设置订单号
                    Map<String,String> map = new HashMap<>();
                    Long companyId = orderHeaders.getCompanyId();
                    String companyIdString = companyId.toString();
                    map.put("var",companyIdString);
                    try {
                        String demo = codeRuleProcessService.getRuleCode("ORDER_NUMBER", map);
                        orderHeaders.setOrderNumber(codeRuleProcessService.getRuleCode("ORDER_NUMBER",map));
                    } catch (CodeRuleException e) {
                        e.printStackTrace();
                    }
                    //保存头
                    orderHeadersService.insertSelective(iRequest,orderHeaders);
                    //insert之后就有主键了
                    headerId = orderHeaders.getHeaderId();
                    List<OmOrderLines> orderLinesList = orderHeaders.getOrderLinesList();
                    if(orderLinesList != null && !orderLinesList.isEmpty()){
                        //保存行
                        for (OmOrderLines orderLines: orderLinesList) {
                            //为新建的行设置头ID
                            orderLines.setHeaderId(headerId);

                            //为行设置公司ID
                            orderLines.setCompanyId(orderHeaders.getCompanyId());
                            //为新建的行设置物料ID
                            Long inventoryItemIdByCode = iInvInventoryItemsService.getInventoryItemIdByCode(orderLines.getItemCode());
                            orderLines.setInventoryItemId(inventoryItemIdByCode);
                            omOrderLinesService.insertSelective(iRequest,orderLines);
                        }
                    }

                }else{
                    //说明是更新
                    //判断头的公司ID是否更改
                    OmOrderHeaders orderHeadersCheck = orderHeadersService.selectByPrimaryKey(iRequest, orderHeaders);
                    if(orderHeaders.getCompanyId() != orderHeadersCheck.getCompanyId()){
                        //来进行查询对应的行
                        OmOrderLines orderLines = new OmOrderLines();
                        orderLines.setHeaderId(orderHeaders.getHeaderId());
                        List<OmOrderLines> select = omOrderLinesService.select(iRequest, orderLines, 1, 0);
                        //对头对应的行进行修改，因为头的公司ID修改了，所以行的公司ID也要秀海
                        if(select!= null &&!select.isEmpty()){
                            for (OmOrderLines omOrderLines: select) {
                                omOrderLines.setCompanyId(orderHeaders.getCompanyId());
                                omOrderLinesService.changeCompanyId(omOrderLines);
                            }
                        }
                    }
                    //更新头
                    orderHeadersService.updateByPrimaryKeySelective(iRequest,orderHeaders);
                    List<OmOrderLines> orderLinesList = orderHeaders.getOrderLinesList();
                    //保存行的时候用行主键来区分是更新还是新建保存
                    if(orderLinesList != null && !orderLinesList.isEmpty()){
                        for (OmOrderLines orderLines: orderLinesList) {
                            if(orderLines.getLineId() == null){
                                //行主键为空说明是新建
                                //为新建的行设置头ID
                                orderLines.setHeaderId(headerId);
                                //设置行号
                                Long maxLineNumber = omOrderLinesService.getMaxLineNumber(headerId);
                                //如果最大行号为空在行号复制为1
                                if(maxLineNumber == null){
                                    maxLineNumber =1L;
                                }else{
                                    //否则行号为最大行号+1
                                    maxLineNumber++;
                                }
                                orderLines.setLineNumber(maxLineNumber);
                                //为行设置公司ID
                                orderLines.setCompanyId(orderHeaders.getCompanyId());
                                //为新建的行设置物料ID
                                Long inventoryItemIdByCode = iInvInventoryItemsService.getInventoryItemIdByCode(orderLines.getItemCode());
                                orderLines.setInventoryItemId(inventoryItemIdByCode);
                                omOrderLinesService.insertSelective(iRequest,orderLines);
                            }else{
                                //拥有行主键说明是更新
                                //设置物料ID
                                Long inventoryItemIdByCode = iInvInventoryItemsService.getInventoryItemIdByCode(orderLines.getItemCode());
                                orderLines.setInventoryItemId(inventoryItemIdByCode);
                                //为行设置公司ID
                                orderLines.setCompanyId(orderHeaders.getCompanyId());
                                omOrderLinesService.updateByPrimaryKeySelective(iRequest,orderLines);
                            }
                        }
                    }

                }
            }
        }
        return orderHeadersList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int myBatchDelect(IRequest request, OmOrderHeaders orderHeaders) {
        int deleteRowCount = 0;
        if(orderHeaders == null ){
            try {
                throw new Exception("缺失数据");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            //来进行查询对应的行
            OmOrderLines orderLines = new OmOrderLines();
            orderLines.setHeaderId(orderHeaders.getHeaderId());
            List<OmOrderLines> select = omOrderLinesService.select(request, orderLines, 1, 0);
            if(select!= null &&!select.isEmpty()){
                //批量删除行
                deleteRowCount = omOrderLinesService.batchDelete(select);
            }
            //来删除头
            deleteRowCount += orderHeadersService.deleteByPrimaryKey(orderHeaders);

        return deleteRowCount;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStatus(IRequest request,OmOrderHeaders omOrderHeaders) {
        OmOrderHeaders orderHeaders = orderHeadersService.selectByPrimaryKey(request, omOrderHeaders);
        orderHeaders.setOrderStatus(omOrderHeaders.getOrderStatus());
        orderHeadersService.updateByPrimaryKeySelective(request,orderHeaders);
    }

    @Override
    public void setLines(IRequest request, List<OmOrderHeaders> orderHeaders) {
        if(orderHeaders != null && !orderHeaders.isEmpty()){
            for (OmOrderHeaders orderHeader: orderHeaders) {
                //根据头id获得对应的行的集合并封装
                OmOrderLines orderLine = new OmOrderLines();
                orderLine.setHeaderId(orderHeader.getHeaderId());
                List<OmOrderLines> select = omOrderLinesService.select(request, orderLine, 1, 0);
                for (OmOrderLines orderLines: select) {
                    //说明有物料，则封装物料信息
                    if(orderLines.getInventoryItemId() != null){
                        InvInventoryItems invInventoryItems = new InvInventoryItems();
                        invInventoryItems.setInventoryItemId(orderLines.getInventoryItemId());
                        InvInventoryItems invInventoryItemsSelect = iInvInventoryItemsService.selectByPrimaryKey(request, invInventoryItems);
                        if(invInventoryItemsSelect != null ){
                            orderLines.setItemUom(invInventoryItemsSelect.getItemUom());
                            orderLines.setItemDescription(invInventoryItemsSelect.getItemDescription());
                            orderLines.setItemCode(invInventoryItemsSelect.getItemCode());
                        }
                    }
                    //封装订单总金额
                    orderLines.setPrice(orderLines.getUnitSellingPrice()*orderLines.getOrderdQuantity());
                }
                orderHeader.setOrderLinesList(select);
            }
        }

    }
}

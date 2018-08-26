package hbi.demo.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hbi.demo.dto.InvInventoryItems;
import hbi.demo.dto.OmOrderLines;
import hbi.demo.service.IInvInventoryItemsService;
import hbi.demo.service.IOmOrderLinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

    @Controller
    public class OmOrderLinesController extends BaseController{

    @Autowired
    private IOmOrderLinesService service;
        @Autowired
        private IInvInventoryItemsService iInvInventoryItemsService;

    @RequestMapping(value = "/hap/om/order/lines/query")
    @ResponseBody
    public ResponseData query(OmOrderLines dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<OmOrderLines> orderLinesList = service.select(requestContext, dto, page, pageSize);
        for (OmOrderLines orderLines: orderLinesList) {
            //说明有物料，则封装物料信息
            if(orderLines.getInventoryItemId() != null){
                InvInventoryItems invInventoryItems = new InvInventoryItems();
                invInventoryItems.setInventoryItemId(orderLines.getInventoryItemId());
                InvInventoryItems invInventoryItemsSelect = iInvInventoryItemsService.selectByPrimaryKey(requestContext, invInventoryItems);
                if(invInventoryItemsSelect != null ){
                    orderLines.setItemUom(invInventoryItemsSelect.getItemUom());
                    orderLines.setItemDescription(invInventoryItemsSelect.getItemDescription());
                    orderLines.setItemCode(invInventoryItemsSelect.getItemCode());
                }
            }
            //封装订单总金额
            orderLines.setPrice(orderLines.getUnitSellingPrice()*orderLines.getOrderdQuantity());
        }

        return new ResponseData(orderLinesList);
    }

        @RequestMapping(value = "/hap/om/order/lines/getmaxlinenumber")
        @ResponseBody
        public ResponseData getMaxLineNumber(OmOrderLines dto) {
            OmOrderLines orderLines = new OmOrderLines();
            List<OmOrderLines> orderLinesList = new ArrayList<>();
            //设置行号为最大行号+1
            Long maxLineNumber = service.getMaxLineNumber(dto.getHeaderId());
            if(maxLineNumber == null){
                maxLineNumber =1L;
            }else{
                maxLineNumber++;
            }
            orderLines.setLineNumber(maxLineNumber);
            orderLinesList.add(orderLines);
            return new ResponseData(orderLinesList);
        }

    @RequestMapping(value = "/hap/om/order/lines/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmOrderLines> dto, BindingResult result, HttpServletRequest request){
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/om/order/lines/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<OmOrderLines> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }
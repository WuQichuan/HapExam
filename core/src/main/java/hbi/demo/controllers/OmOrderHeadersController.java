package hbi.demo.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hbi.demo.dto.OmOrderHeaders;
import hbi.demo.service.IOmOrderHeadersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class OmOrderHeadersController extends BaseController{

    @Autowired
    private IOmOrderHeadersService service;


    @RequestMapping(value = "/hap/om/order/headers/query")
    @ResponseBody
    public ResponseData query(OmOrderHeaders dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<OmOrderHeaders> orderList = service.select(requestContext, dto, page, pageSize);
        //获得公司名称，客户名称，销售订单对应订单的金额总和
        service.getNamesAndAmount(orderList);
        //如果要查询物料的话，二次筛选,筛选出头订单下行订单上还有该物料ID的头订单并返回
        if(dto.getInventoryItemId()!=null){
            List<OmOrderHeaders> omOrderHeaderss = service.filterByInventoryItemId(orderList, dto.getInventoryItemId());
            return new ResponseData(omOrderHeaderss);
        }
        //封装对应的行数据
        service.setLines(requestContext,orderList);

        return new ResponseData(orderList);
    }

    @RequestMapping(value = "/hap/om/order/headers/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmOrderHeaders> dto, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.myBatchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/om/order/headers/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<OmOrderHeaders> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

        @RequestMapping(value = "/hap/om/order/headers/removeall")
        @ResponseBody
        public ResponseData removeall(HttpServletRequest request,@RequestBody OmOrderHeaders dto){
            IRequest requestCtx = createRequestContext(request);
            //调用自己写的整单删除方法
            service.myBatchDelect(requestCtx,dto);
            return new ResponseData();
        }

        @RequestMapping(value = "/hap/om/order/headers/changestatus")
        @ResponseBody
        public ResponseData changesubmit(HttpServletRequest request,@RequestBody OmOrderHeaders dto){
            IRequest requestCtx = createRequestContext(request);
            service.changeStatus(requestCtx,dto);
            return new ResponseData();
        }

    }
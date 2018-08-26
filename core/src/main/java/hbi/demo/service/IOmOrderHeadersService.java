package hbi.demo.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hbi.demo.dto.OmOrderHeaders;

import java.util.List;

public interface IOmOrderHeadersService extends IBaseService<OmOrderHeaders>, ProxySelf<IOmOrderHeadersService>{

    /* *
     * 封装订单头的公司名称，客户名称，和所属订单行的总金额
     * @author 武琦川@hand-china.com
     * @date  2018/8/11 13:57
     * @param 需要被封装订单头的集合 orderHeadersList
     * @return
     */
    void getNamesAndAmount( List<OmOrderHeaders> orderHeadersList);

    /* *
     * 根据物料ID进行筛选订单头，筛选出订单头下订单行拥有该物料的订单头
     * @author 武琦川@hand-china.com
     * @date  2018/8/11 16:41
     * @param 未筛选订单头的集合 orderHeadersList
     * @return 筛选好的订单头的集合
     */
    List<OmOrderHeaders> filterByInventoryItemId(List<OmOrderHeaders> orderHeadersList,Long inventoryItemId);

    /* *
     * 保存销售订单头和销售订单行
     * @author 武琦川@hand-china.com
     * @date  2018/8/12 0:46
     * @param [iRequest, orderHeaders]
     * @return java.util.List<hbi.demo.dto.OmOrderHeaders>
     */
    List<OmOrderHeaders> myBatchUpdate(IRequest iRequest, List<OmOrderHeaders> orderHeaders);

    /* *
     * 整单删除
     * @author 武琦川@hand-china.com
     * @date  2018/8/12 18:40
     * @param [request, orderHeadersList]
     * @return int
     */
     int myBatchDelect(IRequest request,OmOrderHeaders orderHeaders);

     /* *
      * 改变订单状态
      * @author 武琦川@hand-china.com
      * @date  2018/8/12 21:13
      * @param [omOrderHeaders]
      * @return void
      */
     void changeStatus(IRequest request,OmOrderHeaders omOrderHeaders);

     /* *
      * 为头封装对应的行
      * @author 武琦川@hand-china.com
      * @date  2018/8/24 15:47
      * @param [request, orderHeaders]
      * @return void
      */
     void setLines(IRequest request,List<OmOrderHeaders> orderHeaders);
}
package com.park.controller.backend;

import com.google.common.collect.Maps;
import com.park.common.Const;
import com.park.common.ResponseCode;
import com.park.common.ServerResponse;
import com.park.pojo.Product;
import com.park.pojo.User;
import com.park.service.IFileService;
import com.park.service.IProductService;
import com.park.service.IUserService;
import com.park.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Park on 2018-11-30.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录!");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return  ServerResponse.createByErrorMessage("权限不足!");
        }
    }
    @RequestMapping(value = "set_sale_status.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session ,Integer productId,Integer status){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户位登录，请先登录!");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else{
            return ServerResponse.createByErrorMessage("权限不足!");
        }
    }
    @RequestMapping(value = "select_product.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse selectByProductId(HttpSession session,Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户位登录，请先登录!");
        }
        return iProductService.selectByProductId(productId);
    }
    @RequestMapping(value = "get_detail.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session ,Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户位登录，请先登录!");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return  iProductService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMessage("权限不足!");
        }
    }
    @RequestMapping(value = "get_list.do" ,method = RequestMethod.GET)
    @ResponseBody
    public  ServerResponse getList(HttpSession session , @RequestParam(value = "pageNum" ,defaultValue = "1") Integer pageNum ,
                                   @RequestParam(value = "pageSize" ,defaultValue = "10") Integer pageSize){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户位登录，请先登录!");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return  iProductService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("权限不足!");
        }
    }
    @RequestMapping(value = "search.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session ,String productName , Integer productId ,
                                        @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                        @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户位登录，请先登录!");
        }
        if (iUserService.checkAdminRole(currentUser).isSuccess()) {
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        } else {
            return ServerResponse.createByErrorMessage("权限不足!");
        }
    }
    @RequestMapping(value = "upload.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session ,@RequestParam(value = "upload_file",required = false)MultipartFile file , HttpServletRequest request){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户位登录，请先登录!");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map resultMap = Maps.newHashMap();
            resultMap.put("uri",targetFileName);
            resultMap.put("url",url);
            return ServerResponse.createBySuccess(resultMap);
        }else{
            return ServerResponse.createByErrorMessage("没有权限操作!");
        }
    }


    @RequestMapping(value = "richtext_img_upload.do" ,method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session , @RequestParam(value = "upload_file",required = false)MultipartFile file , HttpServletRequest request , HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败!");

            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.setHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }
}

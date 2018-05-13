package com.t201.cj.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.t201.cj.pojo.AppCategory;
import com.t201.cj.pojo.AppInfo;
import com.t201.cj.pojo.AppVersion;
import com.t201.cj.pojo.DataDictionary;
import com.t201.cj.pojo.DevUser;
import com.t201.cj.service.appcategory.AppCategoryService;
import com.t201.cj.service.appinfo.AppInfoService;
import com.t201.cj.service.appversion.AppVersionService;
import com.t201.cj.service.datadictionary.DataDictionaryService;
import com.t201.cj.tools.Constants;
import com.t201.cj.tools.PageSupport;

@Controller
@RequestMapping(value="/pre/appinfo")
public class AppInfoController {
	private Logger logger = Logger.getLogger(AppInfoController.class);
	
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppVersionService appVersionService;
	
	@RequestMapping(value="/{id}/sale.json",method=RequestMethod.PUT)
	@ResponseBody
	public Object sale(@PathVariable String id){
		HashMap<String , String > resultMap = new HashMap<String, String>();
		resultMap.put("errorCode", "0");
		logger.debug("sale  ====== > appid: " + id);
		
		AppInfo appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
		if (appInfo != null) {
			try {
				if (appInfo.getStatus() == 4) { //已上架 进行下架操作
					if (appInfoService.updStatus(Integer.parseInt(id), 5)) {
						resultMap.put("resultMsg", "success");
					}else {
						resultMap.put("resultMsg", "failed");
					}
				}else if (appInfo.getStatus() == 5 || appInfo.getStatus() == 2) { //未上架 进行上架操作
					logger.debug("sale  ====== > getStatus: " + id);
					if (appInfoService.updStatus(Integer.parseInt(id), 4)) {
						resultMap.put("resultMsg", "success");
					}else {
						resultMap.put("resultMsg", "failed");
					}
				}else {
					resultMap.put("errorCode", "param000001");
				}
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}
		}else {
			resultMap.put("errorCode", "param000001");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/delapp.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delApp(@RequestParam("id")String id){
		logger.debug("delApp ============= > appId : " + id);
		HashMap<String , String > resultMap = new HashMap<String, String>();
		AppInfo appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
		String fileLocPath = "";
		String fileApkPath = "";
		if (appInfo != null) {
			//删除本地图片
			try {
				fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null)).getLogoLocPath();
				if (fileLocPath != "" && fileLocPath != null) {
					File fileLoc = new File(fileLocPath);
					if(fileLoc.exists()){
						fileLoc.delete();
					}
				}
			} catch (Exception e) {
				logger.debug("delApp ===> appId : " + id + ",该id下无图片信息！");
			}
			//删除本地apk文件
			try {
				fileApkPath = (appVersionService.getAppVersionById(Integer.parseInt(id))).getApkLocPath();
				if (fileApkPath != "" && fileApkPath != null) {
					File fileApk = new File(fileApkPath);
					if(fileApk.exists()){
						fileApk.delete();
					}
				}
			} catch (Exception e) {
				logger.debug("delApp ===> appId : " + id + ",该id下无版本信息！");
			}
		    //删除app版本信息 及 app基础信息
			if (appInfoService.delAppInfo(Integer.parseInt(id))) {
				appVersionService.delAppVersion(Integer.parseInt(id));
				resultMap.put("delResult", "true");
			}else {
				resultMap.put("delResult", "false");
			}
		}else {
			resultMap.put("delResult", "notexist");
		}
		return resultMap;
	}
	
	@RequestMapping(value="/appview/{id}")
	public String appView(@PathVariable String id,Model model){
		logger.debug("appView  =====> appid :" + id);
		
		List<AppVersion> appVersionList = appVersionService.getAppVersionList(Integer.parseInt(id));
		model.addAttribute("appVersionList", appVersionList);
		
		AppInfo appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
		model.addAttribute(appInfo);
		
		return "developer/appinfoview";
	}
	
	@RequestMapping("/appversionmodifysave.html")
	public String appVersionModifySave(AppVersion appVersion,
									   HttpSession session,
									   HttpServletRequest request,
									   @RequestParam(value="attach",required= false) MultipartFile attach ){
		String downloadLink =  null;
		String apkLocPath = null;
		String apkFileName = null;
		
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){
				 String apkName = null;
				 try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
				 } catch (Exception e1) {
					e1.printStackTrace();
				 }
				 if(apkName == null || "".equals(apkName)){
					 return "redirect:/pre/appinfo/appversionmodify.html?vid="+appVersion.getId()
							 +"&aid="+appVersion.getAppId()
							 +"&error=error1";
				 }
				 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
				 File targetFile = new File(path,apkFileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/pre/appinfo/appversionmodify.html?vid="+appVersion.getId()
							 +"&aid="+appVersion.getAppId()
							 +"&error=error2";
				} 
				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else{
				return "redirect:/pre/appinfo/appversionmodify.html?vid="+appVersion.getId()
						 +"&aid="+appVersion.getAppId()
						 +"&error=error3";
			}
		}
		appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if(appVersionService.modify(appVersion)){
				return "redirect:/pre/appinfo/list.html";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appversionmodify";
	}
	
	@RequestMapping("/appversionmodify.html")
	public String appVersionModify(Model model,
								   @RequestParam("vid")String vid,
								   @RequestParam("aid")String aid,
								   @RequestParam(value="error",required=false)String error){
		if ("error1".equals(error)) {
			model.addAttribute("fileUploadError"," * APK信息不完整！");
		}else if ("error2".equals(error)) {
			model.addAttribute("fileUploadError","* 上传失败！");
		}else if ("error3".equals(error)) {
			model.addAttribute("fileUploadError"," * 上传文件格式不正确！");
		}else {
			model.addAttribute("fileUploadError",null);
		}
		//查询历史版本列表
		List<AppVersion> appVersionList = appVersionService.getAppVersionList(Integer.parseInt(aid));
		model.addAttribute("appVersionList", appVersionList);
		
		//查询版本信息
		AppVersion appVersion = appVersionService.getAppVersionById(Integer.parseInt(vid));
		model.addAttribute(appVersion);
		
		return "developer/appversionmodify";
	}
	
	@RequestMapping(value="/appversionaddsave.html",method=RequestMethod.POST)
	public String appVersionAddSave(AppVersion appVersion,
									HttpSession session,
									HttpServletRequest request,
									@RequestParam(value="a_downloadLink",required= false) MultipartFile attach ){
		String downloadLink =  null;
		String apkLocPath = null;
		String apkFileName = null;
		
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){
				 String apkName = null;
				 try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
				 } catch (Exception e1) {
					e1.printStackTrace();
				 }
				 if(apkName == null || "".equals(apkName)){
					 return "redirect:/pre/appinfo/appversionadd.html?id="+appVersion.getAppId()
							 +"&error=error1";
				 }
				 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
				 File targetFile = new File(path,apkFileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/pre/appinfo/appversionadd.html?id="+appVersion.getAppId()
							 +"&error=error2";
				} 
				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
				apkLocPath = path+File.separator+apkFileName;
			}else{
				return "redirect:/pre/appinfo/appversionadd.html?id="+appVersion.getAppId()
						 +"&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if(appVersionService.addAppVersion(appVersion)){ //新增版本信息
				Integer vid = appVersionService.getNewId();  //获取最新版本信息的id
				if (appInfoService.updVersionIdById(vid, appVersion.getAppId())) { //更新app的版本信息
						return "redirect:/pre/appinfo/list.html";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/pre/appinfo/appversionadd.html?id="+appVersion.getAppId();
	}
	
	@RequestMapping(value="/appversionadd.html")
	public String appVersionAdd(AppVersion appVersion,
								@RequestParam(value="id")String id,
								@RequestParam(value="error",required=false)String error,
								Model model){
		if ("error1".equals(error)) {
			model.addAttribute("fileUploadError"," * APK信息不完整！");
		}else if ("error2".equals(error)) {
			model.addAttribute("fileUploadError","* 上传失败！");
		}else if ("error3".equals(error)) {
			model.addAttribute("fileUploadError"," * 上传文件格式不正确！");
		}else {
			model.addAttribute("fileUploadError",null);
		}
		
		appVersion.setAppId(Integer.parseInt(id));
		appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(id),null)).getSoftwareName());
		
		List<AppVersion> appVersionList = appVersionService.getAppVersionList(Integer.parseInt(id));
		model.addAttribute("appVersionList", appVersionList);
		return "developer/appversionadd";
	}
	
	@RequestMapping(value="/appinfomodifysave.html",method=RequestMethod.POST)
	public String appInfoModifySave(
			 AppInfo appInfo,
			 HttpSession session,
			 HttpServletRequest request,
			 @RequestParam(value="attach",required=false)MultipartFile attach){
		
		String logoPicPath =  null;
		String logoLocPath =  null;
		String APKName = appInfo.getAPKName();
		
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){//上传大小不得超过 50k
            	 return "redirect:/pre/appinfo/appinfomodify.html?id="+appInfo.getId()
						 +"&error=error1";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
				 String fileName = APKName + ".jpg";
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/pre/appinfo/appinfomodify.html?id="+appInfo.getId()
						 +"&error=error2";
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
            }else{
            	return "redirect:/pre/appinfo/appinfomodify.html?id="+appInfo.getId()
						 +"&error=error3";
            }
		}
		appInfo.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		if(appInfoService.modify(appInfo)){
			return "redirect:/pre/appinfo/list.html";
		}
		return "redirect:/pre/appinfo/appinfomodify.html?id="+appInfo.getId();
	}
	
	@RequestMapping(value="/delfile.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delFile(@RequestParam(value="flag",required=false) String flag,
			 @RequestParam(value="id",required=false) String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String fileLocPath = null;
		if(flag == null || flag.equals("") || id == null || id.equals("")){
			resultMap.put("result", "failed");
		}else if("logo".equals(flag)){//删除logo图片
			try {
				fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null)).getLogoLocPath();
				File file = new File(fileLocPath);
			   if(file.exists()){
				    if(file.delete()){
						if(appInfoService.delAppLOGO(Integer.parseInt(id))){//更新表
							resultMap.put("result", "success");
						}
				   }
			   }
			   logger.debug("del LOGO File=======>fileLocPath file.exists():" + file.exists());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("apk".equals(flag)){//删除apk文件
			try {
			   fileLocPath = (appVersionService.getAppVersionById(Integer.parseInt(id))).getApkLocPath();
			   File file = new File(fileLocPath);
			   if(file.exists()){
				    if(file.delete()){
						if(appVersionService.deleteApkFile(Integer.parseInt(id))){//更新表
							resultMap.put("result", "success");
						}
				   }
			   }
			   logger.debug("del apk File=======>fileLocPath file.exists():" + file.exists());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}
	
	@RequestMapping(value="/appinfomodify.html")
	public String appInfoModify(
			@RequestParam(value="id",required=false)String id,
			@RequestParam(value="error",required=false)String error,Model model){
		if (id == null || id == "") {
			return "developer/appinfolist";
		}else {
			AppInfo appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
			if ("error1".equals(error)) {
				model.addAttribute("fileUploadError"," * 上传文件过大！");
			}else if ("error2".equals(error)) {
				model.addAttribute("fileUploadError","* 上传失败！");
			}else if ("error3".equals(error)) {
				model.addAttribute("fileUploadError"," * 上传文件格式不正确！");
			}else {
				model.addAttribute("fileUploadError",null);
			}
			model.addAttribute("appInfo", appInfo);
			return "developer/appinfomodify";
		}
	}
	
	@RequestMapping(value="/appinfoaddsave.html",method=RequestMethod.POST)
	public String appInfoAddSave(AppInfo appInfo,
								 HttpSession session,
								 HttpServletRequest request,
								 @RequestParam(value="a_logoPicPath",required=false)MultipartFile attach){
		
		String logoPicPath =  null;
		String logoLocPath =  null;
		
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){//上传大小不得超过 50k
				request.setAttribute("fileUploadError", " * 上传文件过大！");
				return "developer/appinfoadd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
				 String fileName = appInfo.getAPKName() + ".jpg";
				 File targetFile = new File(path,fileName);
				 if(!targetFile.exists()){
					 targetFile.mkdirs();
				 }
				 try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", "* 上传失败！");
					return "developer/appinfoadd";
				} 
				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				 logoLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError", " * 上传文件格式不正确！");
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setStatus(1);
		try {
			if(appInfoService.addAppInfo(appInfo)){
				return "redirect:/pre/appinfo/list.html";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfoadd";
	}
	
	@RequestMapping(value="/apkexist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object apkexist(@RequestParam("APKName")String APKName){
		HashMap<String , String > resultMap = new HashMap<String, String>();
		if (APKName == null || APKName == "") {
			resultMap.put("APKName", "empty");
		}else {
			AppInfo appInfo = appInfoService.getAppInfo(null, APKName);
			if (appInfo != null) {
				resultMap.put("APKName", "exist");
			}else {
				resultMap.put("APKName", "noexist");
			}
		}
		return resultMap;
	}
	
	@RequestMapping(value="/datadictionarylist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object dataDictionaryList(@RequestParam("tcode")String tcode){
		return dataDictionaryService.getDataDictionaries(tcode);
	}
	
	@RequestMapping(value="/appinfoadd.html")
	public String appInfoAdd(){
		return "developer/appinfoadd";
	}
	
	@RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object categoryLevelList(@RequestParam("pid")String pid){
		if (pid == null || pid == "") { //查询一级分类
			return appCategoryService.getAppCategories(null);
		}else {	//查询二、三级分类
			logger.debug("categoryLevelList============>pid:" + Integer.valueOf(pid));
			return appCategoryService.getAppCategories(Integer.valueOf(pid));
		}
	}
	
	@RequestMapping(value="/list.html")
	public String getAppInfoList(
			Model model,HttpSession session,
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) String _queryStatus,
			@RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
			@RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
			@RequestParam(value="pageIndex",required=false) String pageIndex){
		
		logger.info("getAppInfoList -- > querySoftwareName: " + querySoftwareName);
		logger.info("getAppInfoList -- > queryStatus: " + _queryStatus);
		logger.info("getAppInfoList -- > queryFlatformId: " + _queryFlatformId);
		logger.info("getAppInfoList -- > queryCategoryLevel1: " + _queryCategoryLevel1);
		logger.info("getAppInfoList -- > queryCategoryLevel2: " + _queryCategoryLevel2);
		logger.info("getAppInfoList -- > queryCategoryLevel3: " + _queryCategoryLevel3);
		logger.info("getAppInfoList -- > pageIndex: " + pageIndex);
		
		Integer devId = ((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId();
		List<AppInfo> appInfoList = null;
		List<DataDictionary> statusList = null;
		List<DataDictionary> flatFormList = null;
		List<AppCategory> categoryLevel1List = null;//列出一级分类列表，注：二级和三级分类列表通过异步ajax获取
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		//页面容量
		int pageSize = Constants.pageSize;
		//当前页码
		Integer currentPageNo = 1;
		
		if(pageIndex != null){
			try{
				currentPageNo = Integer.valueOf(pageIndex);
			}catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		Integer queryStatus = null;
		if(_queryStatus != null && !_queryStatus.equals("")){
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null;
		if(_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")){
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2 = null;
		if(_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")){
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null;
		if(_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")){
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null;
		if(_queryFlatformId != null && !_queryFlatformId.equals("")){
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}
		
		//总数量
		int totalCount = 0;
		try {
			totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus, queryFlatformId, queryCategoryLevel1,  queryCategoryLevel2, queryCategoryLevel3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		//控制首页和尾页
		if(currentPageNo < 1){
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount){
			currentPageNo = totalPageCount;
		}
		
		appInfoList = appInfoService.getAppInfoList(querySoftwareName, queryStatus, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, devId, currentPageNo, pageSize);
		statusList = dataDictionaryService.getDataDictionaries("APP_STATUS");
		flatFormList = dataDictionaryService.getDataDictionaries("APP_FLATFORM");
		categoryLevel1List = appCategoryService.getAppCategories(null);
		
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("pages", pages);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);
		
		//二级分类列表和三级分类列表
		if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
			logger.debug("=====================>queryCategoryLevel1:" + queryCategoryLevel1);
			categoryLevel2List = appCategoryService.getAppCategories(queryCategoryLevel1);
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if(queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")){
			logger.debug("=====================>queryCategoryLevel2:" + queryCategoryLevel2);
			categoryLevel3List = appCategoryService.getAppCategories(queryCategoryLevel2);
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}
		return "developer/appinfolist";
	}
}

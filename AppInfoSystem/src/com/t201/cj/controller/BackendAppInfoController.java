package com.t201.cj.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.t201.cj.pojo.AppCategory;
import com.t201.cj.pojo.AppInfo;
import com.t201.cj.pojo.AppVersion;
import com.t201.cj.pojo.BackendUser;
import com.t201.cj.pojo.DataDictionary;
import com.t201.cj.service.appcategory.AppCategoryService;
import com.t201.cj.service.appinfo.AppInfoService;
import com.t201.cj.service.appversion.AppVersionService;
import com.t201.cj.service.datadictionary.DataDictionaryService;
import com.t201.cj.tools.Constants;
import com.t201.cj.tools.PageSupport;

@Controller
@RequestMapping("/sys/appinfo")
public class BackendAppInfoController {
	private Logger logger = Logger.getLogger(BackendAppInfoController.class);
	
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;
	
	@RequestMapping(value="/checksave.html",method=RequestMethod.POST)
	public String checkSave(@RequestParam("id")String id,
							@RequestParam("status")String status){
		logger.debug("checkSave========>id:" + id + ",status:" + status);
		appInfoService.updStatus(Integer.parseInt(id), Integer.parseInt(status));
		return "redirect:/sys/appinfo/list.html";
	}
	
	@RequestMapping(value="check.html")
	public String check(@RequestParam("aid")String aid,
						@RequestParam("vid")String vid,
						Model model){
		logger.debug("check ===== > aid:" + aid + ",vid:"+vid);
		AppInfo appInfo = appInfoService.getAppInfo(Integer.parseInt(aid), null);
		model.addAttribute(appInfo);
		
		AppVersion appVersion = appVersionService.getAppVersionById(Integer.parseInt(vid));
		model.addAttribute(appVersion);
		
		return "backend/appcheck";
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
	public String list(Model model,HttpSession session,
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
		
		Integer devId = ((BackendUser)session.getAttribute(Constants.USER_SESSION)).getId();
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
			totalCount = appInfoService.getAppInfoCount(querySoftwareName, 1, queryFlatformId, queryCategoryLevel1,  queryCategoryLevel2, queryCategoryLevel3);
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
		
		appInfoList = appInfoService.getAppInfoList(querySoftwareName, 1, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, devId, currentPageNo, pageSize);
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
		return "backend/applist";
	}
	
}

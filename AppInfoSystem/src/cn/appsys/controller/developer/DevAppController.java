package cn.appsys.controller.developer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;

import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.app.AppInfoService;
import cn.appsys.service.version.AppVersionService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping(value = "/dev/flatform/app")
public class DevAppController {

	private final Logger logger = Logger.getLogger(DevLoginController.class);
	
	@Resource
	private AppInfoService appInfoService;
	
	@Resource
	private AppVersionService appVersionService;

	// ���APPά����ѯAPP�б�
	@RequestMapping(value = "/list")
	public String GetAppInfolist(
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) Integer queryStatus,
			@RequestParam(value = "queryFlatformId", required = false) Integer queryFlatformId,
			@RequestParam(value = "queryCategoryLevel1", required = false) Integer queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) Integer queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) Integer queryCategoryLevel3,
			@RequestParam(value = "pageIndex", required = false) Integer pageIndex,
			Model model) {
		logger.debug("GetAppInfolist");
		// ����ҳ������
		int pageSize = Constants.pageSize;
		// ��ǰҳ��
		int currentPageNo = 1;
		if (pageIndex != null) {
			currentPageNo = Integer.valueOf(pageIndex);
		}
		// ����������
		int totalCount = appInfoService.getAppInfoCount(querySoftwareName,
				queryStatus, queryFlatformId, queryCategoryLevel1,
				queryCategoryLevel2, queryCategoryLevel3);
		// ��ҳ��
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();

		// ������ҳ��βҳ
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		List<AppInfo> appInfoList = appInfoService.getAppInfoList(
				querySoftwareName, queryStatus, queryFlatformId,
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
				currentPageNo, pageSize);
		model.addAttribute("appInfoList", appInfoList);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("statusList",appInfoService.getDatadictionarylist("APP_STATUS"));
		model.addAttribute("flatFormList",appInfoService.getDatadictionarylist("APP_FLATFORM"));
		model.addAttribute("categoryLevel1List",appInfoService.getAppCategoryList(null));
		if (queryCategoryLevel1 != null)
			model.addAttribute("categoryLevel2List",appInfoService.getAppCategoryList(queryCategoryLevel1));
		if (queryCategoryLevel2 != null)
			model.addAttribute("categoryLevel3List",appInfoService.getAppCategoryList(queryCategoryLevel2));
		model.addAttribute("pages", pages);
		return "developer/appinfolist";
	}

	// �����������첽����
	@RequestMapping(value = "/categorylevellist.json")
	@ResponseBody
	public Object getCategorylevellist(
			@RequestParam(value = "pid", required = false) Integer pid) {
		return appInfoService.getAppCategoryList(pid);
	}

	// �������APP��Ϣ
	@RequestMapping(value = "/appinfoadd")
	public String appinfoadd() {
		return "developer/appinfoadd";
	}

	// ����APP��Ϣ
	@RequestMapping(value = "/appinfoaddsave", method = RequestMethod.POST)
	public String appinfosave(
			AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) {
		String logoPicPath = null;
		String logoLocPath = null;
		String path = request.getContextPath() + "/statics/uploadfiles/";
		String contextPath = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
		if (!attach.isEmpty()) {
			String oldFileName = attach.getOriginalFilename(); // ԭ�ļ���
			String prefix = FilenameUtils.getExtension(oldFileName); // ԭ�ļ���׺
			if (attach.getSize() > 500_000) {
				request.setAttribute("fileUploadError", " * �ϴ���С���ó���500KB");
				return "developer/appinfoadd";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")) {
				String fileName = appInfo.getAPKName() + ".jpg";
				File targetFile = new File(contextPath, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", " * �ϴ�ʧ�ܣ�");
					return "developer/appinfoadd";
				}
				logoLocPath = contextPath + File.separator + fileName;
				logoPicPath = path + fileName;
			} else {
				request.setAttribute("fileUploadError", " * �ϴ�ͼƬ��ʽ����ȷ");
				return "developer/appinfoadd";
			}
		}
		appInfo.setCreatedBy(((DevUser) session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setDevId(((DevUser) session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		if(appInfo.getStatus() != 1){
			request.setAttribute("statusError", " * ��⵽��ʹ�ÿ�����Ա�����޸�AAP״̬�������ύʧ��");
			return "developer/appinfoadd";
		}
		if(appInfoService.addAppInfo(appInfo)){
			return "redirect:/dev/flatform/app/list";
		}else{
			return "developer/appinfoadd";
		}
	}

	// �첽���������б�
	@RequestMapping(value = "/datadictionarylist.json")
	@ResponseBody
	public Object datadictionarylist(
			@RequestParam(value = "tcode", required = false) String tcode) {
		return appInfoService.getDatadictionarylist(tcode);
	}

	// �첽��֤APKName
	@RequestMapping(value = "/apkexist.json")
	@ResponseBody
	public Object apkexist(
			@RequestParam(value = "APKName", required = false) String APKName) {
		Map<String, String> result = new HashMap<String, String>();
		if (appInfoService.getApkexistByAPKName(APKName)) {
			result.put("APKName", "exist");
		} else {
			result.put("APKName", "noexist");
		}
		if ("".equals(APKName) || null == APKName) {
			result.put("APKName", "empty");
		}
		return result;
	}
	
	// �����޸�APP��Ϣ
	@RequestMapping(value = "/appinfomodify")
	public String appinfomodify(
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "fileUploadError",required = false) String fileUploadError,
			HttpServletRequest request,
			Model model) {
			if(fileUploadError != null && fileUploadError != ""){
				if("sizeErr".equals(fileUploadError)){
					request.setAttribute("fileUploadError", " * �ϴ���С���ó���500KB");
				}else if("upErr".equals(fileUploadError)){
					request.setAttribute("fileUploadError", " * �ϴ�ʧ�ܣ�");
				}else if("preErr".equals(fileUploadError)){
					request.setAttribute("fileUploadError", " * �ϴ�ͼƬ��ʽ����ȷ");
				}
			}
		model.addAttribute("appInfo",appInfoService.getAppInfoById(id));
		return "developer/appinfomodify";
	}
	
	// �����޸�APP��Ϣ
	@RequestMapping(value = "/appinfomodifysave", method = RequestMethod.POST)
	public String appinfomodifysave(
			AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach) {
		String logoPicPath = null;
		String logoLocPath = null;
		String path = request.getContextPath() + "/statics/uploadfiles/";
		String contextPath = request.getSession().getServletContext()
				.getRealPath("statics" + File.separator + "uploadfiles");
		if (!attach.isEmpty()) {
			String oldFileName = attach.getOriginalFilename(); // ԭ�ļ���
			String prefix = FilenameUtils.getExtension(oldFileName); // ԭ�ļ���׺
			if (attach.getSize() > 500000) {
				return "redirect://dev/flatform/app/appinfomodify?id=" + appInfo.getId() + "&fileUploadError=sizeErr";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")) {
				String fileName = appInfo.getAPKName() + ".jpg";
				File targetFile = new File(contextPath, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect://dev/flatform/app/appinfomodify?id=" + appInfo.getId() + "&fileUploadError=upErr";
				}
				logoLocPath = contextPath + File.separator + fileName;
				logoPicPath = path + fileName;
			} else {
				return "redirect:/dev/flatform/app/appinfomodify?id=" + appInfo.getId() + "&fileUploadError=preErr";
			}
		}
		appInfo.setModifyBy(((DevUser) session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		if(appInfo.getStatus() != null){
			if (appInfo.getStatus() != 1) {
				request.setAttribute("statusError", " * ��⵽��ʹ�ÿ�����Ա�����޸�AAP״̬�������޸�ʧ��");
				return "developer/appinfomodify";
			}
		}
		if (appInfoService.updateAppInfo(appInfo)) {
			return "redirect:/dev/flatform/app/list";
		} else {
			return "developer/appinfomodify";
		}
	}
	
	// �첽ɾ���ļ�
	@RequestMapping(value = "/delfile.json")
	@ResponseBody
	public Object delfile(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "flag", required = false) String flag) {
		Map<String, String> result = new HashMap<String, String>();
		File file = new File("");
		boolean check = false;
		if("logo".equals(flag)){
			AppInfo appInfo = appInfoService.getAppInfoById(id);
			file = new File(appInfo.getLogoLocPath());
			if (appInfoService.updatePathById(id)) {
				check = true;
			}
		}else if("apk".equals(flag)){
			AppVersion appVersion = appVersionService.getAppVersionById(id);
			file = new File(appVersion.getApkLocPath());
			if (appVersionService.updateAppVersionOfPathById(id)) {
				check = true;
			}
		}
		if(check){
			result.put("result", "success");
			if (file.exists() && file.isFile()) 
				file.delete();
		} else {
			result.put("result", "failed");
		}
		return result;
	}
	
	//����APP�汾��Ϣ
	@RequestMapping(value = "/appversionadd")
	public String appversionadd(@RequestParam(value="id")Integer id,
			@RequestParam(value = "fileUploadError",required = false) String fileUploadError,
			HttpServletRequest request,
			Model model){
		AppVersion appVersion = new AppVersion();
		appVersion.setAppId(id);
		List<AppVersion> appVersionList = appVersionService.getAppVersionListByAppId(id);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("appVersion",appVersion);
		if(fileUploadError != null && fileUploadError != ""){
			if("sizeErr".equals(fileUploadError)){
				request.setAttribute("fileUploadError", " * �ϴ���С���ó���500MB");
			}else if("upErr".equals(fileUploadError)){
				request.setAttribute("fileUploadError", " * �ϴ�ʧ�ܣ�");
			}else if("preErr".equals(fileUploadError)){
				request.setAttribute("fileUploadError", " * �ϴ�APP��ʽ����ȷ");
			}
		}
		return "developer/appversionadd";
	}
	
	//����APP�汾����
	@RequestMapping(value = "/addversionsave")
	public String addversionsave(AppVersion appVersion,Model model,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_downloadLink", required = false) MultipartFile attach){
		
		AppInfo appInfo = appInfoService.getAppInfoById(appVersion.getAppId());
		String downloadLink = null;
		String apkLocPath = null;
		String fileName = null;
		String path = request.getContextPath() + "/statics/uploadfiles/";
		String contextPath = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
		
		if (!attach.isEmpty()) {
			String oldFileName = attach.getOriginalFilename(); 			// ԭ�ļ���
			String prefix = FilenameUtils.getExtension(oldFileName); 	// ԭ�ļ���׺
			if (prefix.equalsIgnoreCase("apk")) {
				fileName = appInfo.getAPKName() + "-" + appVersion.getVersionNo() + ".apk";
				File targetFile = new File(contextPath, fileName);
				if (attach.getSize() > 5_000_000) {
					return "redirect:/dev/flatform/app/appversionadd?id=" + appVersion.getAppId() + "&fileUploadError=sizeErr";
				} else if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionadd?id=" + appVersion.getAppId() + "&fileUploadError=upErr";
				}
				apkLocPath = contextPath + File.separator + fileName;
				downloadLink = path + fileName;
			} else {
				return "redirect:/dev/flatform/app/appversionadd?id=" + appVersion.getAppId() + "&fileUploadError=preErr";
			}
		}
		appVersion.setCreatedBy(((DevUser) session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setPublishStatus(3);
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(fileName);
		if(appVersionService.addAppVersion(appVersion)){
			appVersion.setId(appVersionService.getNewId());
			appInfoService.updateAppInfoOfVersionIdById(appVersion.getId(), appInfo.getId());
			return "redirect:/dev/flatform/app/list";
		}else{
			return "developer/appversionadd";
		}
	}
	
	//�޸�APP�汾��Ϣ
	@RequestMapping(value = "/appversionmodify")
	public String appversionmodify(@RequestParam(value = "vid", required = false) Integer vid,
			@RequestParam(value = "aid", required = false) Integer aid,
			@RequestParam(value = "fileUploadError",required = false) String fileUploadError,
			HttpServletRequest request,
			Model model){
		AppVersion appVersion = appVersionService.getAppVersionById(vid);
		List<AppVersion> appVersionList = appVersionService.getAppVersionListByAppId(aid);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("appVersion",appVersion);
		
		if(fileUploadError != null && fileUploadError != ""){
			if("sizeErr".equals(fileUploadError)){
				request.setAttribute("fileUploadError", " * �ϴ���С���ó���500MB");
			}else if("upErr".equals(fileUploadError)){
				request.setAttribute("fileUploadError", " * �ϴ�ʧ�ܣ�");
			}else if("preErr".equals(fileUploadError)){
				request.setAttribute("fileUploadError", " * �ϴ�APP��ʽ����ȷ");
			}
		}
		return "developer/appversionmodify";
	}
	
	//����APP�汾��Ϣ
	@RequestMapping(value = "/appversionmodifysave")
	public String appversionmodifysave(AppVersion appVersion,Model model,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach){

		AppInfo appInfo = appInfoService.getAppInfoById(appVersion.getAppId());
		
		String downloadLink = null;
		String apkLocPath = null;
		String fileName = null;
		
		String path = request.getContextPath() + "/statics/uploadfiles/";
		String contextPath = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
		
		if (!attach.isEmpty()) {
			String oldFileName = attach.getOriginalFilename(); 			// ԭ�ļ���
			String prefix = FilenameUtils.getExtension(oldFileName); 	// ԭ�ļ���׺
			if (prefix.equalsIgnoreCase("apk")) {
				fileName = appInfo.getAPKName() + "-" + appVersion.getVersionNo() + ".apk";
				File targetFile = new File(contextPath, fileName);
				if (attach.getSize() > 5_000_000) {
					return "redirect:/dev/flatform/app/appversionmodify?vid=" + appVersion.getId() + "&aid=" + appVersion.getAppId() + "&fileUploadError=sizeErr";
				} else if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionmodify?vid=" + appVersion.getId() + "&aid=" + appVersion.getAppId() + "&fileUploadError=upErr";
				}
				apkLocPath = contextPath + File.separator + fileName;
				downloadLink = path + fileName;
				
			} else {
				return "redirect:/dev/flatform/app/appversionmodify?vid=" + appVersion.getId() + "&aid=" + appVersion.getAppId() + "&fileUploadError=preErr";
			}
		}
		appVersion.setModifyBy(((DevUser) session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		appVersion.setModifyDate(new Date());
		appVersion.setPublishStatus(3);
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(fileName);
		if(appVersionService.updateAppVersion(appVersion)){
			return "redirect:/dev/flatform/app/list";
		}else{
			return "developer/appversionmodify";
		}
	}
	
	//�鿴APP��Ϣ
	@RequestMapping(value = "/appview/{id}")
	public String appview(@PathVariable Integer id,Model model){
		AppInfo appInfo = appInfoService.getAppInfoById(id);
		List<AppVersion> appVersionList = appVersionService.getAppVersionListByAppId(id);
		model.addAttribute("appInfo",appInfo);
		model.addAttribute("appVersionList",appVersionList);
		return "developer/appinfoview";
	}
	
	// �첽ɾ���ļ�
	@RequestMapping(value = "/delapp.json")
	@ResponseBody
	public Object delapp(@RequestParam(value = "id", required = false) Integer id) {
		File file = null;
		Map<String, String> result = new HashMap<String, String>();
		boolean flag = false;
		//ɾ��APP��ϢLOG
		AppInfo appInfo = appInfoService.getAppInfoById(id);
		if(null == appInfo){
			result.put("delResult", "notexist");
			return result;
		}
		file = new File(appInfo.getLogoLocPath());
		if (file.exists() && file.isFile()) 
			file.delete();
		//ɾ�����а汾APK�ļ�
		List<AppVersion> appVersionList = appVersionService.getAppVersionListByAppId(id);
		for (AppVersion appVersion : appVersionList) {
			file = new File(appVersion.getApkLocPath());
			if (file.exists() && file.isFile())
				file.delete();
		}
		//����AIDɾ�����а汾 || ����IDɾ��APP��Ϣ  
		if(appInfoService.deleteAppInfoById(id)){
			appVersionService.deleteAppVersionByAId(id);
			flag = true;
		}
		result.put("delResult",Boolean.toString(flag));
		return result;
	}
	
	// �첽ɾ���ļ�
	@RequestMapping(value = "/{id}/sale.json")
	@ResponseBody
	public Object sale(@PathVariable Integer id) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("errorCode", "0");
		if(appInfoService.getAppInfoById(id).getStatus() == 4){
			if(appInfoService.updateAppInfoOfStatusById(5, id)){
				result.put("resultMsg", "success");
			}else{
				result.put("resultMsg", "failed");
			}
		}else{
			if(appInfoService.updateAppInfoOfStatusById(4, id)){
				result.put("resultMsg", "success");
			}else{
				result.put("resultMsg", "failed");
			}
		}
		return result;
	}
}

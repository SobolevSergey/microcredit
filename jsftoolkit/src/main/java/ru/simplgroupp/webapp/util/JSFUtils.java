package ru.simplgroupp.webapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.transfer.Reference;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;

public class JSFUtils {
	
	public static final Integer NULL_INT_VALUE = -9999;
	public static final String NULL_LABEL = "-- неважно --";
	
	public static String executeAction(RuntimeServices runServ, FacesContext facesCtx, UIComponent btnCurrent, AbstractContext ctx) {
		String businessObjectClass = (String) btnCurrent.getAttributes().get("businessObjectClass");
		String signalRef = (String) btnCurrent.getAttributes().get("signalRef");
		String procDefKey = (String) btnCurrent.getAttributes().get("procDefKey"); 
		String plugin = (String) btnCurrent.getAttributes().get("plugin"); 
		Number businessObjectId = (Number ) btnCurrent.getAttributes().get("businessObjectId"); 
		String redirectUrl = (String) btnCurrent.getAttributes().get("redirectUrl"); 
		
		try {
			ExceptionInfo res = null;
			if (StringUtils.isBlank(procDefKey)) {
				res = runServ.getBizProc().executeBizAction(businessObjectClass, signalRef, procDefKey, plugin, businessObjectId.intValue(), ctx);
			} else {
				WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef(true);
				actionDef.setSignalRef(SignalRef.toString(procDefKey, plugin, signalRef));
				runServ.getWorkflowBean().goProc(businessObjectClass, businessObjectId, actionDef, signalRef, null);
			}
			if (res != null) {
	    		JSFUtils.handleAsError(facesCtx, null, new Exception(res.getMessage()));
	    		return null;				
			}
		} catch (ActionException e) {
    		JSFUtils.handleAsError(facesCtx, null, e);
    		return null;
		} catch (WorkflowException e) {
    		JSFUtils.handleAsError(facesCtx, null, e);
    		return null;
		}
		
		return redirectUrl + "?faces-redirect=true";
	}
	
	/**
	 * отображаем ошибку (предупреждение) на форме
	 * @param facesCtx - контекст faces
	 * @param svr - уровень предупреждения
	 * @param clientId - где пишем
	 * @param ex - сообщение
	 */
	public static void handleException(FacesContext facesCtx, Severity svr, String clientId, Exception ex) {
		if (facesCtx == null)
			facesCtx = FacesContext.getCurrentInstance();
		
		String sclientId = null;
		if (clientId != null) {
			UIComponent acomp = findComponent(facesCtx, clientId);
			sclientId = acomp.getClientId();
		}
				
        FacesMessage message = new FacesMessage();
        message.setDetail(ex.getMessage());
        message.setSummary(ex.getMessage());
        message.setSeverity(svr);
        facesCtx.addMessage(sclientId, message);	
	}
	
	public static void handleAsError(FacesContext facesCtx, String clientId, Exception ex){
		handleException(facesCtx, FacesMessage.SEVERITY_ERROR, clientId, ex);
	}

	public static void handleAsWarning(FacesContext facesCtx, String clientId, Exception ex){
		handleException(facesCtx, FacesMessage.SEVERITY_WARN, clientId, ex);
	}	
	
	public static void handleAsInfo(FacesContext facesCtx, String clientId, Exception ex){
		handleException(facesCtx, FacesMessage.SEVERITY_INFO, clientId, ex);
	}	
	
	/**
	 * показываем сообщение
	 * @param facesCtx - контекст faces
	 * @param svr - уровень предупреждения
	 * @param bClear - очищать сообщения
	 * @param smessage - сообщение
	 */
	public static void addMessage(FacesContext facesCtx, Severity svr,boolean bClear, String smessage){
		if (facesCtx == null){
			facesCtx = FacesContext.getCurrentInstance();
		}
		if (bClear){
			facesCtx.getMessageList().clear();
		}

		FacesMessage message = new FacesMessage();
        message.setDetail(smessage);
        message.setSummary(smessage);
        message.setSeverity(svr);
        facesCtx.addMessage(null, message);	
		
	}
	
	public static void addAsInfo(FacesContext facesCtx, boolean bClear, String smessage){
		addMessage(facesCtx,FacesMessage.SEVERITY_INFO,bClear,smessage);
	}
	
	public static void addAsError(FacesContext facesCtx, boolean bClear, String smessage){
		addMessage(facesCtx,FacesMessage.SEVERITY_ERROR,bClear,smessage);
		
	}
	
	public static void addAsWarning(FacesContext facesCtx, boolean bClear, String smessage) {
		addMessage(facesCtx,FacesMessage.SEVERITY_WARN,bClear,smessage);
		
	}	
	
	public static List<SelectItem> referenceToSelect(List<Reference> source, Object nullValue, String nullLabel) {
		List<SelectItem> lstRes = referenceToSelect(source);
		
		SelectItem si = new SelectItem();
		si.setValue(nullValue);
		si.setLabel(nullLabel);
		lstRes.add(0, si);
		
		return lstRes;
	}
	
	public static boolean isNullReference(Reference ref) {
		return (ref == null || ref.getId() == null || ref.getId().equals(NULL_INT_VALUE));
	}
	
	public static List<SelectItem> referenceToSelectRef(List<Reference> source) {
		ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size()); 
		for (Reference ref: source) {
			SelectItem si = new SelectItem();
			si.setValue(ref);
			si.setLabel(ref.getName());
			lstRes.add(si);
		}
		return lstRes;
	}
	
	
	public static List<SelectItem> referenceToSelectRef(List<Reference> source, Integer nullValue, String nullLabel) {
		List<SelectItem> lstRes = referenceToSelectRef(source);
		
		SelectItem si = new SelectItem();
		Reference nref = new Reference();
		nref.setId(nullValue);
		nref.setName(nullLabel);
		si.setValue(nref );
		si.setLabel(nullLabel);
		lstRes.add(0, si);
		
		return lstRes;
	}	
	
	public static List<SelectItem> referenceToSelect(List<Reference> source) {
		ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size()); 
		for (Reference ref: source) {
			SelectItem si = new SelectItem();
			si.setValue(ref.getId());
			si.setLabel(ref.getName());
			lstRes.add(si);
		}
		return lstRes;
	}
	
	public static List<SelectItem> referenceToSelectCodeInteger(List<Reference> source, Object nullValue, String nullLabel) {
		List<SelectItem> lstRes = referenceToSelectCodeInteger(source);
		
		SelectItem si = new SelectItem();
		si.setValue(nullValue);
		si.setLabel(nullLabel);
		lstRes.add(0, si);
		
		return lstRes;
	}
	
	public static List<SelectItem> referenceToSelectCodeInteger(List<Reference> source) {
		ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size()); 
		for (Reference ref: source) {
			SelectItem si = new SelectItem();
			si.setValue(ref.getCodeInteger());
			si.setLabel(ref.getName());
			lstRes.add(si);
		}
		return lstRes;
	}	
	
	public static UIComponent findComponent(FacesContext facesCtx, String id) {
		return findComponent(id, facesCtx.getViewRoot() );
	}
	
	/**
	 * ищем компонент в форме
	 * @param id - id компонента
	 * @param where - где ищем
	 * @return
	 */
	public static UIComponent findComponent(String id, UIComponent where) {
		UIComponent uic=null;
		
		if (where!=null)
		{
			if (where.getId().equals(id)) 
		      uic=where;
		    else 
		    {
		       List<UIComponent> childrenList = where.getChildren();
		       if ((childrenList != null) && !childrenList.isEmpty()) 
		   
		       for (UIComponent child : childrenList) 
		       {
		          uic = findComponent(id, child);
                  if (uic!=null)
            	      break;
		       }

		    }
		}
		return uic;
	}
	
	/**
	 * возвращает текст компонента
	 * @param uic - input
	 * @return
	 */
	public static String getComponentText(UIInput uic) {
		String st="";
		if ((uic!=null)&&(uic.isLocalValueSet())&&(uic.getLocalValue()!=null)){
			st=uic.getLocalValue().toString();
		}
		return st;
	}
	
	/**
	 * возвращает id компонента
	 * @param uic - input
	 * @return
	 */
	public static String getComponentId(UIInput uic) {
		String st="";
		if (uic!=null){
			st=uic.getClientId();
		}
		return st;
	}
	
	/**
	 * выгружаем файл
	 * @param file - файл
	 * @param contentType - вид контента
	 * @throws IOException
	 */
	public static void downloadFile(File file, String contentType) throws IOException{
	
		FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

        response.reset();
        response.setContentType(contentType); 

        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\""); 

        OutputStream output = response.getOutputStream();
        FileInputStream fin =null;
        try {
    	    fin = new FileInputStream(file);
    	    IOUtils.copy(fin, output);
        } finally {
    	    IOUtils.closeQuietly(fin);
        }

        fc.responseComplete();
	}

}

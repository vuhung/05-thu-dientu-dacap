<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	if(request.getAttribute("path") == null){
		request.setAttribute("path", request.getContextPath());
	}
	
	// escapse the common domain name.
	if(request.getAttribute("j_domain") == null){
		final String[] cds = new String[]{"www","mail","ftp"};
		
		// extract the domain name.
		String domain = request.getServerName().toLowerCase() ;
		final int rpos = domain.indexOf(".") ;
		if(rpos > 0){
			final String rpart = domain.substring(0, rpos) ;
			
			// finding common domain.
			int index = 0;
			for(;index < cds.length; index++){
				if(rpart.equals(cds[index])) break ;
			}
			
			if(index < cds.length){
				domain = domain.substring(rpos + 1) ;
			}
		}
		
		// set attribute.
		request.setAttribute("j_domain", domain) ;
	}
%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="ua.com.ncu.portlet.TableauViewConfiguration"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>


<liferay-theme:defineObjects />

<portlet:defineObjects />

<% 
TableauViewConfiguration tableauViewConfiguration = (TableauViewConfiguration) renderRequest.getAttribute(TableauViewConfiguration.class.getName());
	
	String tableauHostname = StringPool.BLANK;
 	String tableauUserName = StringPool.BLANK;
	String tableauVisualization = StringPool.BLANK;
	String tableauSchema = StringPool.BLANK;
	String tableauHideTabs = StringPool.BLANK;
	String tableauHideToolbar = StringPool.BLANK;
	String tableauTimeRefresh = StringPool.BLANK;
	String tableauTrustSSC = StringPool.BLANK;
	
	if(Validator.isNotNull(tableauViewConfiguration)){
		_log.debug("Inside init.jsp validator....");
		tableauHostname = portletPreferences.getValue("tableauHostname", tableauViewConfiguration.tableauHostname());
 		tableauUserName = portletPreferences.getValue("tableauUserName", tableauViewConfiguration.tableauUserName());
		tableauVisualization = portletPreferences.getValue("tableauVisualization", tableauViewConfiguration.tableauVisualization());
		tableauSchema = portletPreferences.getValue("tableauSchema", tableauViewConfiguration.tableauSchema());
		tableauHideTabs = portletPreferences.getValue("tableauHideTabs", tableauViewConfiguration.tableauHideTabs());
		tableauHideToolbar = portletPreferences.getValue("tableauHideToolbar", tableauViewConfiguration.tableauHideToolbar());
		tableauTimeRefresh = portletPreferences.getValue("tableauTimeRefresh", tableauViewConfiguration.tableauTimeRefresh());
		tableauTrustSSC = portletPreferences.getValue("tableauTrustSSC", tableauViewConfiguration.tableauTrustSSC());
	}
	
	_log.debug("tableauHostname : " + tableauHostname);
	_log.debug("tableauUserName : " + tableauUserName);
	_log.debug("tableauVisualization : " + tableauVisualization);
	_log.debug("tableauSchema : " + tableauSchema);
	_log.debug("tableauHideTabs : " + tableauHideTabs);
	_log.debug("tableauHideToolbar : " + tableauHideToolbar);
	_log.debug("tableauTimeRefresh : " + tableauTimeRefresh);
	_log.debug("tableauTrustSSC : " + tableauTrustSSC);
	
%>

<%! private static Log _log = LogFactoryUtil.getLog("ua.com.ncu.tableau"); %>

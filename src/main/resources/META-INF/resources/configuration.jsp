<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>

<liferay-portlet:actionURL portletConfiguration="<%= true %>"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="<%= true %>"
    var="configurationRenderURL" />
    
<aui:form action="<%= configurationActionURL %>" method="post" name="fm">   

<aui:input name="<%= Constants.CMD %>" type="hidden"
        value="<%= Constants.UPDATE %>" />

    <aui:input name="redirect" type="hidden"
        value="<%= configurationRenderURL %>" />
        
<aui:fieldset cssClass="testfieldset">
  <aui:row>
  	<aui:col span="6">
		<aui:input
			label="Tableau Hostname"
			name="tableauHostname"
			value="<%= tableauHostname %>" >
		</aui:input>
	</aui:col>
	<aui:col span="2" style="top: 30px;">	
		<aui:input
			label="Trust a Self-Signed Certificate"
			name="tableauTrustSSC" 
			type="checkbox"
			value="<%= tableauTrustSSC %>">
		</aui:input>
	</aui:col>
	<aui:col span="4">	
		<aui:input
			label="Tableau User Name"
			name="tableauUserName"
			value="<%= tableauUserName %>" >
		</aui:input>
	</aui:col>		
 </aui:row>
<%--	<aui:input
		label = "Tableau Password"
		name = "tableauPassword"
		value = "<%= tableauPassword %>" 
		type = "password" >
	</aui:input>
	
	<aui:input
		label="Tableau ContentUrl"
		name="tableauContentUrl"
		value="<%= tableauContentUrl %>" >
	</aui:input>   --%>
 <aui:row>
 	<aui:col span="12">
		<aui:input
			label="Tableau Visualization"
			name="tableauVisualization"
			value="<%= tableauVisualization %>" >
		</aui:input>
	</aui:col>	
 </aui:row>
 
 <aui:row>
 	<aui:col span="3">	
		<aui:select label="Tableau Schema" name="tableauSchema" value="<%= tableauSchema %>">
				<aui:option value="2.4">2.4</aui:option>
				<aui:option value="2.3">2.3</aui:option>
		</aui:select>
	</aui:col>
	<aui:col span="3">
		<aui:select label="Tableau Hide Tabs" name="tableauHideTabs" value="<%= tableauHideTabs %>">
				<aui:option value="true">true</aui:option>
				<aui:option value="false">false</aui:option>
		</aui:select>
	</aui:col>
	<aui:col span="3">
		<aui:select label="Tableau Hide Toolbar" name="tableauHideToolbar" value="<%= tableauHideToolbar %>">
				<aui:option value="true">true</aui:option>
				<aui:option value="false">false</aui:option>
		</aui:select>
	</aui:col>
	<aui:col span="3">
		<aui:input
			label="Tableau Time Refresh"
			name="tableauTimeRefresh"
			value="<%= tableauTimeRefresh %>" >
		</aui:input>
	</aui:col>
 </aui:row>
</aui:fieldset>

    <aui:button-row>
        <aui:button type="submit"></aui:button>
    </aui:button-row>
</aui:form>
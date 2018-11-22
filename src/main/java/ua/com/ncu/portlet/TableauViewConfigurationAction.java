package ua.com.ncu.portlet;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

import ua.com.ncu.constants.TableauViewPortletKeys;

@Component(
		configurationPid = "ua.com.ncu.portlet.TableauViewConfiguration",
		configurationPolicy = ConfigurationPolicy.OPTIONAL,
		immediate = true,
		property = "javax.portlet.name=" + TableauViewPortletKeys.TableauView,
		service = ConfigurationAction.class
	)
public class TableauViewConfigurationAction extends DefaultConfigurationAction{
	
	@Override
	public void include(PortletConfig portletConfig, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		_log.debug("TableauConfigurationAction include");
		
		request.setAttribute(TableauViewConfiguration.class.getName(), _tableauViewConfiguration);
		
		super.include(portletConfig, request, response);
	}
	
	@Override
	public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		
		_log.debug("TableauConfigurationAction processActions");
		
		String tableauHostname = ParamUtil.getString(actionRequest, "tableauHostname");
		String tableauUserName = ParamUtil.getString(actionRequest, "tableauUserName");
		String tableauVisualization = ParamUtil.getString(actionRequest, "tableauVisualization");
		String tableauSchema = ParamUtil.getString(actionRequest, "tableauSchema");
		String tableauHideTabs = ParamUtil.getString(actionRequest, "tableauHideTabs");
		String tableauHideToolbar = ParamUtil.getString(actionRequest, "tableauHideToolbar");
		String tableauTimeRefresh = ParamUtil.getString(actionRequest, "tableauTimeRefresh");
		String tableauTrustSSC = ParamUtil.getString(actionRequest, "tableauTrustSSC");
		
/*		_log.info("TableauConfigurationAction tableauHostname: " + tableauHostname);
		_log.info("TableauConfigurationAction tableauUserName: " + tableauUserName);
		_log.info("TableauConfigurationAction tableauPassword: " + tableauPassword);
		_log.info("TableauConfigurationAction tableauContentUrl: " + tableauContentUrl);
		_log.info("TableauConfigurationAction tableauVisualization: " + tableauVisualization);
		_log.info("TableauConfigurationAction tableauSchema: " + tableauSchema);
		_log.info("TableauConfigurationAction tableauHideTabs: " + tableauHideTabs);
		_log.info("TableauConfigurationAction tableauHideToolbar: " + tableauHideToolbar);
		_log.info("TableauConfigurationAction tableauTimeRefresh: " + tableauTimeRefresh);*/
		
		setPreference(actionRequest, "tableauHostname", tableauHostname);
		setPreference(actionRequest, "tableauUserName", tableauUserName);
		setPreference(actionRequest, "tableauVisualization", tableauVisualization);
		setPreference(actionRequest, "tableauSchema", tableauSchema);
		setPreference(actionRequest, "tableauHideTabs", tableauHideTabs);
		setPreference(actionRequest, "tableauHideToolbar", tableauHideToolbar);
		setPreference(actionRequest, "tableauTimeRefresh", tableauTimeRefresh);
		setPreference(actionRequest, "tableauTrustSSC", tableauTrustSSC);
		
		super.processAction(portletConfig, actionRequest, actionResponse);
	}
	
	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		_tableauViewConfiguration = ConfigurableUtil.createConfigurable(TableauViewConfiguration.class, properties);
	}
	
	private volatile TableauViewConfiguration _tableauViewConfiguration;
	
	private static Log _log = LogFactoryUtil.getLog("ua.com.ncu.tableau");

}

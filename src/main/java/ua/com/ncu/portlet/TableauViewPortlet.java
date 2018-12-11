package ua.com.ncu.portlet;

import ua.com.ncu.constants.TableauViewPortletKeys;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author jam
 */
@Component(
	configurationPid = "ua.com.ncu.portlet.TableauViewConfiguration",
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=NCU",
		"com.liferay.portlet.instanceable=true",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"javax.portlet.display-name=Tableau View Portlet",
		"javax.portlet.init-param.config-template=/configuration.jsp",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + TableauViewPortletKeys.TableauView,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class TableauViewPortlet extends MVCPortlet {
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		
		_log.debug("Tableau View Portlet render");
				
		renderRequest.setAttribute("tableaFilters", getFilter(renderRequest));
		renderRequest.setAttribute(TableauViewConfiguration.class.getName(), _tableauViewConfiguration);
		
		super.doView(renderRequest, renderResponse);
	}
	
		
	@Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
		_tableauViewConfiguration = ConfigurableUtil.createConfigurable(
				TableauViewConfiguration.class, properties);
    }
	
	//Create Filter for Tableau.
	//Exclude parameters that is start with ":" , "p_" , "currentURL" , "portletAjaxable"
	private String getFilter(RenderRequest renderRequest){
		_log.debug("inside getFilter ...");
		StringJoiner tableauFilter = new StringJoiner("," , "{" , "}");
		HttpServletRequest httpservletrequest = PortalUtil.getHttpServletRequest(renderRequest);
		
		Enumeration<String> enumeration = PortalUtil.getOriginalServletRequest(httpservletrequest).getParameterNames();
		Map<String,String> parameters = new HashMap<>();
		
		while(enumeration.hasMoreElements()){
			 String name = enumeration.nextElement();
			 parameters.put(name, PortalUtil.getOriginalServletRequest(httpservletrequest).getParameter(name));
		 }
		
		for(Map.Entry<String, String> e : parameters.entrySet()){
			if(!e.getKey().startsWith(":") && 
					!e.getKey().startsWith("p_") &&
					!e.getKey().equals("currentURL") &&
					!e.getKey().equals("portletAjaxable")){
			
				tableauFilter.add(e.getKey() + ":" + splitParametrValue(e.getValue()));
			}
		}
		_log.debug("tableauFilter = " + tableauFilter.toString());
		return tableauFilter.toString();
	}
	
	//Split parameter value string by "," 
	private String splitParametrValue(String v){
		String [] value = v.split(",");
		
		for(int i=0; i < value.length; i++){
			value[i] = "\"" + value[i] + "\""; 
		}
		
		return Arrays.toString(value);
	}
	
	private static Log _log = LogFactoryUtil.getLog("ua.com.ncu.tableau");

    private volatile TableauViewConfiguration _tableauViewConfiguration;
}
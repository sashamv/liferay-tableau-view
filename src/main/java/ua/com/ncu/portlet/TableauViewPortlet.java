package ua.com.ncu.portlet;

import ua.com.ncu.constants.TableauViewPortletKeys;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
		renderRequest.setAttribute(TableauViewConfiguration.class.getName(), _tableauViewConfiguration);
		
		super.doView(renderRequest, renderResponse);
	}
	
		
	@Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
		_tableauViewConfiguration = ConfigurableUtil.createConfigurable(
				TableauViewConfiguration.class, properties);
    }
	
	private static Log _log = LogFactoryUtil.getLog("ua.com.ncu.tableau");

    private volatile TableauViewConfiguration _tableauViewConfiguration;
}
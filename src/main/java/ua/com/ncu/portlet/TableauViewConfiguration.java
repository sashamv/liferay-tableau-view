package ua.com.ncu.portlet;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import aQute.bnd.annotation.metatype.Meta;
import aQute.bnd.annotation.metatype.Meta.Type;

@ExtendedObjectClassDefinition(
		category = "Tableau Default Configuration",
		scope = ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE
		)

@Meta.OCD(id = "ua.com.ncu.portlet.TableauViewConfiguration")
public interface TableauViewConfiguration {
	
	@Meta.AD(deflt = "https://localhost", required = false, name = "Tableau Hostname")
	public String tableauHostname();
	
	@Meta.AD(deflt = "admin", required = false, name = "Tableau User Name")
	public String tableauUserName();
	
	@Meta.AD(deflt = "Add Visualization", required = false, name = "Tableau Visualization")
	public String tableauVisualization();
	
	@Meta.AD(deflt = "2.4", required = false, name = "Tableau Schema")
	public String tableauSchema();
	
	@Meta.AD(deflt = "true", required = false, name = "Tableau Hide Tabs")
	public String tableauHideTabs();
	
	@Meta.AD(deflt = "true", required = false, name = "Tableau Hide Toolbar")
	public String tableauHideToolbar();
	
	@Meta.AD(deflt = "5", required = false, name = "Tableau Time Refresh")
	public String tableauTimeRefresh();
	
	@Meta.AD(type = Type.Boolean, required = false, name = "Trust a Self-Signed Certificate", deflt = "true")
	public String tableauTrustSSC();

}

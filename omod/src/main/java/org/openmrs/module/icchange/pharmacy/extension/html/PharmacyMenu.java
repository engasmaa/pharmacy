package org.openmrs.module.icchange.pharmacy.extension.html;

import org.openmrs.module.icchange.pharmacy.config.PharmacyConstants;
import org.openmrs.module.web.extension.LinkExt;

public class PharmacyMenu extends LinkExt {

	@Override
	public MEDIA_TYPE getMediaType() {
		return MEDIA_TYPE.html;
	}
	
	
	@Override
	public String getLabel() {
		return "icchange.pharmacy.menu";
	}

	
	@Override
	public String getUrl() {
		return "/module/icchange/pharmacy/pharmacy.form";
	}

	@Override
	public String getRequiredPrivilege() {
		return PharmacyConstants.EDITPRIVILEGE;
	}

}

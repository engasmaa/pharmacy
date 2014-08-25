package org.openmrs.module.icchange.pharmacy.web.model;

import java.text.SimpleDateFormat;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.context.Context;

public class DWRBaseOpenmrsData {

	private Integer id;
	private String uuid;

	private Integer creator;
	private String creatorName;
	private String creationDate;
	
	private Integer voider;
	private String voiderName;
	private String voidDate;
	
	private Integer modifier;
	private String modifierName;
	private String modificationDate;
	
	private Integer retirer;
	private String retirerName;
	
	public DWRBaseOpenmrsData() {
	}

	public DWRBaseOpenmrsData(BaseOpenmrsData data) {
		this.fromBaseOpenmrsData(data);
	}

	
	public void fromBaseOpenmrsData (BaseOpenmrsData data) {
		SimpleDateFormat sdf = Context.getDateFormat();
		
		this.id = data.getId();
		this.uuid = data.getUuid();
		
		this.creator = data.getCreator().getId();
		this.creatorName = data.getCreator().getPersonName().getFullName();
		this.creationDate = sdf.format(data.getDateCreated());
		
		if (data.getVoided()) {
			this.voider = data.getVoidedBy().getId();
			this.voiderName = data.getVoidedBy().getPersonName().getFullName();
			this.voidDate = sdf.format(data.getDateVoided());
		}
		
		if (data.getChangedBy() != null) { 
			this.modifier = data.getChangedBy().getId();
			this.modifierName = data.getChangedBy().getPersonName().getFullName();
			this.modificationDate = sdf.format(data.getDateChanged());
		}		
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Integer getVoider() {
		return voider;
	}
	public void setVoider(Integer voider) {
		this.voider = voider;
	}
	public String getVoiderName() {
		return voiderName;
	}
	public void setVoiderName(String voiderName) {
		this.voiderName = voiderName;
	}
	public Integer getModifier() {
		return modifier;
	}
	public void setModifier(Integer modifier) {
		this.modifier = modifier;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public Integer getRetirer() {
		return retirer;
	}
	public void setRetirer(Integer retirer) {
		this.retirer = retirer;
	}
	public String getRetirerName() {
		return retirerName;
	}
	public void setRetirerName(String retirerName) {
		this.retirerName = retirerName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getVoidDate() {
		return voidDate;
	}

	public void setVoidDate(String voidDate) {
		this.voidDate = voidDate;
	}

	public String getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}


}

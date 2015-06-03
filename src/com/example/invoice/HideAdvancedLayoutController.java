package com.example.invoice;

import com.vaadin.ui.AbstractOrderedLayout;

public class HideAdvancedLayoutController {
	protected boolean isHidden = false;
	protected AbstractOrderedLayout parent;
	
	public HideAdvancedLayoutController(AbstractOrderedLayout parent){
		this.parent = parent;
	}
	
	public void showDetail(){
		parent.removeAllComponents();
		parent.addStyleName("form-detail");
		parent.addStyleName("space-margin");
		setHidden(false);
	}
	
	public void hideDetail(){
		parent.removeAllComponents();
		parent.removeStyleName("form-detail");
		parent.removeStyleName("space-margin");
		setHidden(true);
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public AbstractOrderedLayout getParent() {
		return parent;
	}

	public void setParent(AbstractOrderedLayout parent) {
		this.parent = parent;
	}
	
	
}

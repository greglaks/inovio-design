package com.example.invoice;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class InvoiceDetailFormLayout extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8908574894356257749L;
	private boolean isHidden = true;
	
	public InvoiceDetailFormLayout(){
		addStyleName("form-detail");
		FormLayout f1 = createForm1();
		FormLayout f2 = createForm2();
		
		addComponent(f1);
		addComponent(f2);
		
		setSpacing(true);
		setMargin(false);
		setWidth("100%");
		addStyleName("faktura-section-layout");
	}

	private FormLayout createForm2() {
		FormLayout form = new FormLayout();
		
		TextField iban = new TextField("IBAN:");
		TextField swift = new TextField("SWIFT:");
		ComboBox country = new ComboBox("Country:");
		ComboBox currency = new ComboBox("Currency:");
		TextField quantity = new TextField("Quantity:");
		DateField date = new DateField("Date of taxable supply");
		
		form.addComponent(iban);
		form.addComponent(swift);
		form.addComponent(country);
		form.addComponent(currency);
		form.addComponent(quantity);
		form.addComponent(date);
		return form;
	}

	private FormLayout createForm1() {
		FormLayout form = new FormLayout();
		
		TextField ss = new TextField("SS:");
		DateField date = new DateField("Issue date:");
		TextField exchangeRate = new TextField("Exchange rate:");
		ComboBox paymentMethod = new ComboBox("Payment method:");
		TextField cs = new TextField("CS:");
		TextField orderNr = new TextField("Order nr.:");
		
		form.addComponent(orderNr);
		form.addComponent(date);
		form.addComponent(exchangeRate);
		form.addComponent(paymentMethod);
		form.addComponent(cs);
		form.addComponent(ss);
		return form;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	
	

}

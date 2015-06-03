package com.example.invoice;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class CustomerDetailLayoutController extends HideAdvancedLayoutController {
	
	private Button ares;
	private ComboBox customerId;
	private ComboBox customer;
	private TextField address;
	private ComboBox country;
	private TextField zipCode;
	private TextField city;

	public CustomerDetailLayoutController(VerticalLayout parent, ComboBox customer, ComboBox customerId, Button ares){
		super(parent);
		this.customer = customer;
		this.customerId = customerId;
		this.ares = ares;
	}

	@Override
	public void showDetail() {
		super.showDetail();
		address = new TextField();
		HorizontalLayout zipCityLayout = createZipCityLayout();
		HorizontalLayout aresCustomerLayout = createAresCustomerLayout();
		country = new ComboBox();
		
		address.setWidth("100%");
		zipCityLayout.setWidth("100%");
		country.setWidth("100%");
		
		parent.addComponent(customer);
		parent.addComponent(aresCustomerLayout);
		parent.addComponent(address);
		parent.addComponent(zipCityLayout);
		parent.addComponent(country);
		
		
	}
	
	private HorizontalLayout createAresCustomerLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setMargin(false);
		layout.setWidth("100%");
		
		layout.addComponent(customerId);
		layout.addComponent(ares);
		layout.setExpandRatio(customerId, 1.75f);
		layout.setExpandRatio(ares, 0.8f);
		return layout;
	}

	@Override
	public void hideDetail(){
		super.hideDetail();
		HorizontalLayout custIdAresLayout = createCustomerIdAreslayout();
		
		parent.addComponent(customer);
		parent.addComponent(custIdAresLayout);
	
	}

	

	private HorizontalLayout createCustomerIdAreslayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setMargin(false);
		layout.setWidth("100%");
		
		layout.addComponent(customerId);
		layout.addComponent(ares);
		layout.setExpandRatio(customerId, 1.75f);
		layout.setExpandRatio(ares, 1.0f);
		
		return layout;
	}

	private HorizontalLayout createZipCityLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		
		zipCode = new TextField();
		city = new TextField();
		
		city.setInputPrompt("City");
		zipCode.setInputPrompt("ZIP");
		
		zipCode.setWidth("55px");
		city.setWidth("100%");
		
		layout.addComponent(zipCode);
		layout.addComponent(city);
		
		layout.setExpandRatio(zipCode, 0.35f);
		layout.setExpandRatio(city, 0.65f);
		
		return layout;
	}

	public Button getAres() {
		return ares;
	}

	public void setAres(Button ares) {
		this.ares = ares;
	}

	public ComboBox getCustomerId() {
		return customerId;
	}

	public void setCustomerId(ComboBox customerId) {
		this.customerId = customerId;
	}

	public ComboBox getCustomer() {
		return customer;
	}

	public void setCustomer(ComboBox customer) {
		this.customer = customer;
	}

	public TextField getAddress() {
		return address;
	}

	public void setAddress(TextField address) {
		this.address = address;
	}

	public ComboBox getCountry() {
		return country;
	}

	public void setCountry(ComboBox country) {
		this.country = country;
	}

	public TextField getZipCode() {
		return zipCode;
	}

	public void setZipCode(TextField zipCode) {
		this.zipCode = zipCode;
	}

	public TextField getCity() {
		return city;
	}

	public void setCity(TextField city) {
		this.city = city;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	
	

}

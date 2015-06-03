package com.example.invoice;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PaymentLayoutController extends HideAdvancedLayoutController{
	
	private TextField exchangeRate;
	private TextField exchangeAmount;
	private ComboBox currencyCombo;
	private TextField orderNumberText;
	private ComboBox paymentCombo;
	private DateField dateOfTaxableSupply;
	private DateField maturityDate;
	private DateField issueDate;
	private TextField ssSymbol;
	private TextField ksSymbol;
	private TextField vsSymbol;

	public PaymentLayoutController(HorizontalLayout parent, TextField vsSymbol, TextField ksSymbol, TextField ssSymbol, 
								   DateField issueDate, DateField maturityDate, DateField dateOfTaxableSupply, 
								   ComboBox paymentCombo, TextField orderNumberText, ComboBox currencyCombo,
								   TextField exchangeAmount, TextField exchangeRate){
		super(parent);
		this.vsSymbol = vsSymbol;
		this.ksSymbol = ksSymbol;
		this.ssSymbol = ssSymbol;
		this.issueDate = issueDate;
		this.maturityDate = maturityDate;
		this.dateOfTaxableSupply = dateOfTaxableSupply;
		this.paymentCombo = paymentCombo;
		this.orderNumberText = orderNumberText;
		this.currencyCombo = currencyCombo;
		this.exchangeAmount = exchangeAmount;
		this.exchangeRate = exchangeRate;
	}
	
	
	@Override
	public void showDetail() {
		super.showDetail();
		VerticalLayout layout1 = createLayout1();
		VerticalLayout layout2 = createLayout2();
		VerticalLayout layout3 = createLayout3();
		VerticalLayout layout4 = createLayout4();
		
		parent.addComponent(layout1);
		parent.addComponent(layout2);
		parent.addComponent(layout3);
		parent.addComponent(layout4);
		
		parent.setExpandRatio(layout1, 17.0f);
		parent.setExpandRatio(layout2, 28.5f);
		parent.setExpandRatio(layout3, 26.5f);
		parent.setExpandRatio(layout4, 28.0f);
	}

	@Override
	public void hideDetail() {
		super.hideDetail();
		vsSymbol.setWidth("100px");
		issueDate.setWidth("180px");
		paymentCombo.setWidth("165px");
		
		parent.addComponent(vsSymbol);
		parent.addComponent(issueDate);
		parent.addComponent(paymentCombo);
			
		parent.setComponentAlignment(vsSymbol, Alignment.TOP_LEFT);
		parent.setComponentAlignment(issueDate, Alignment.TOP_LEFT);
		parent.setComponentAlignment(paymentCombo, Alignment.TOP_LEFT);
		
		parent.setExpandRatio(vsSymbol, 16.5f);
		parent.setExpandRatio(issueDate, 28.0f);
		parent.setExpandRatio(paymentCombo, 55.5f);
	}


	private VerticalLayout createLayout4() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100px");
		
		exchangeAmount.setWidth("50px");
		exchangeRate.setWidth("50px");
		currencyCombo.setWidth("75px");
		
//		exchangeAmount.setWidth("100%");
//		exchangeRate.setWidth("100%");
//		currencyCombo.setWidth("100%");
		
		layout.addComponent(currencyCombo);
		layout.addComponent(exchangeAmount);
		layout.addComponent(exchangeRate);
		
		return layout;
	}


	private VerticalLayout createLayout3() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("165px");
		
		paymentCombo.setWidth("100%");
		orderNumberText.setWidth("100px");
		
		layout.addComponent(paymentCombo);
		layout.addComponent(orderNumberText);
		
		return layout;
	}


	private VerticalLayout createLayout2() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("175px");
		
		issueDate.setWidth("100%");
		maturityDate.setWidth("100%");
		dateOfTaxableSupply.setWidth("100%");
		
		layout.addComponent(maturityDate);
		layout.addComponent(issueDate);
		layout.addComponent(dateOfTaxableSupply);
		
		return layout;
	}


	private VerticalLayout createLayout1() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100px");
		
		vsSymbol.setWidth("100%");
		ksSymbol.setWidth("100%");
		ssSymbol.setWidth("100%");
		
		layout.addComponent(vsSymbol);
		layout.addComponent(ksSymbol);
		layout.addComponent(ssSymbol);
		
		return layout;
	}



	private VerticalLayout createChildLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(true);
		layout.setWidth("100px");
		
		exchangeAmount.setWidth("50px");
		exchangeRate.setWidth("50px");
		currencyCombo.setWidth("75px");
		
		layout.addComponent(currencyCombo);
		layout.addComponent(exchangeAmount);
		layout.addComponent(exchangeRate);
		
		return layout;
	}

	public TextField getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(TextField exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public TextField getExchangeAmount() {
		return exchangeAmount;
	}

	public void setExchangeAmount(TextField exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
	}

	public ComboBox getCurrencyCombo() {
		return currencyCombo;
	}

	public void setCurrencyCombo(ComboBox currencyCombo) {
		this.currencyCombo = currencyCombo;
	}

	public TextField getOrderNumberText() {
		return orderNumberText;
	}

	public void setOrderNumberText(TextField orderNumberText) {
		this.orderNumberText = orderNumberText;
	}

	public ComboBox getPaymentCombo() {
		return paymentCombo;
	}

	public void setPaymentCombo(ComboBox paymentCombo) {
		this.paymentCombo = paymentCombo;
	}

	public DateField getDateOfTaxableSupply() {
		return dateOfTaxableSupply;
	}

	public void setDateOfTaxableSupply(DateField dateOfTaxableSupply) {
		this.dateOfTaxableSupply = dateOfTaxableSupply;
	}

	public DateField getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(DateField maturityDate) {
		this.maturityDate = maturityDate;
	}

	public DateField getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(DateField issueDate) {
		this.issueDate = issueDate;
	}

	public TextField getSsSymbol() {
		return ssSymbol;
	}

	public void setSsSymbol(TextField ssSymbol) {
		this.ssSymbol = ssSymbol;
	}

	public TextField getKsSymbol() {
		return ksSymbol;
	}

	public void setKsSymbol(TextField ksSymbol) {
		this.ksSymbol = ksSymbol;
	}

	public TextField getVsSymbol() {
		return vsSymbol;
	}

	public void setVsSymbol(TextField vsSymbol) {
		this.vsSymbol = vsSymbol;
	}
	
	

}

package com.example.invoice;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BankDetailLayoutController  extends HideAdvancedLayoutController{

	private TextField swift;
	private TextField iban;
	private ComboBox bankCode;
	private ComboBox bankAccount;
	private Label bankCodeLabel;
	private Label bankAccountLabel;
	private VerticalLayout wrapper;
	private Button controllButton;

	public BankDetailLayoutController(AbstractOrderedLayout parent, VerticalLayout wrapper, Label bankAccountLabel, Label bankCodeLabel, ComboBox bankAccount, ComboBox bankCode, TextField iban, TextField swift, Button controllButton) {
		super(parent);
		this.bankAccount = bankAccount;
		this.bankCode = bankCode;
		this.bankAccountLabel = bankAccountLabel;
		this.bankCodeLabel = bankCodeLabel;
		this.iban = iban;
		this.swift = swift;
		this.controllButton = controllButton;
		this.wrapper = wrapper;
		
		wrapper.addComponent(parent);
		wrapper.addComponent(controllButton);
	}


	@Override
	public void showDetail() {
		super.showDetail();
		VerticalLayout leftLayout = createLeftLayout();
		VerticalLayout rightLayout = createRightLayout();
		
		parent.addComponent(leftLayout);
		parent.addComponent(rightLayout);
		parent.setExpandRatio(leftLayout, 2.0f);
		parent.setExpandRatio(rightLayout, 1f);
		
	}



	@Override
	public void hideDetail() {
		super.hideDetail();
		
		VerticalLayout leftLayout = createLeftLayout();
		VerticalLayout rightLayout = createRightLayout();
		
		parent.addComponent(leftLayout);
		parent.addComponent(rightLayout);
		parent.setExpandRatio(leftLayout, 2.0f);
		parent.setExpandRatio(rightLayout, 1.0f);
	}



	private VerticalLayout createRightLayout() {

		VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setMargin(false);
		rightLayout.setSpacing(true);
		rightLayout.setWidth("95px");
		
		rightLayout.addComponent(bankCodeLabel);
		rightLayout.addComponent(bankCode);
		if(!isHidden()){
			rightLayout.addComponent(swift);			
		}
		
		return rightLayout;
	}


	private VerticalLayout createLeftLayout() {
		VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.setMargin(false);
		leftLayout.setSpacing(true);
		leftLayout.setWidth("295px");
		
		leftLayout.addComponent(bankAccountLabel);
		leftLayout.addComponent(bankAccount);
		if (!isHidden()) {
			leftLayout.addComponent(iban);			
		}
		
		return leftLayout;
	}


	public TextField getSwift() {
		return swift;
	}

	public void setSwift(TextField swift) {
		this.swift = swift;
	}

	public TextField getIban() {
		return iban;
	}

	public void setIban(TextField iban) {
		this.iban = iban;
	}

	public ComboBox getBankCode() {
		return bankCode;
	}

	public void setBankCode(ComboBox bankCode) {
		this.bankCode = bankCode;
	}

	public ComboBox getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(ComboBox bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	

}

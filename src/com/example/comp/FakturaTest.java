package com.example.comp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;



public class FakturaTest extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7240357354175047102L;
	public static final Object LOCAL_CURRENCY = "In local currency";
	public static final Object BASIS = "Basis";
	public static final Object VAT = "Amount of VAT";
	public static final Object TOTAL =  "Total";
	private GridLayout itemTable;
	private Table sumTotalTable;
	private List<FakturaItem> itemList = new ArrayList();
	private DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
	private DecimalFormat decimalFormat = new DecimalFormat("# ##0.00;-# ##0.00", decimalSymbols);
	private double rate0;
	private double rate10;
	private double rate15;
	private double rate21;
	private double basic21;
	private double total21;
	private double basic15;
	private double total15;
	private double total10;
	private double basic10;
	private double total0;
	private double basic0;
	private TextField additionalAmountText;
	private Label amountLabel;
	private double subTotal;
	private ComboBox currencyCombo;
	private FormAdvancedLayout advancedLayout = new FormAdvancedLayout();
	private ValueChangeListener amountTextListener = new ValueChangeListener() {
		
		@Override
		public void valueChange(ValueChangeEvent event) {
			calculateTotalPrice();
		}
	};
	
	private Validator validator = new Validator() {
		
		@Override
		public void validate(Object value) throws InvalidValueException {
			if(value.equals("")){
				throw new InvalidValueException("This field is required");
			}
			
		}
	};

	public FakturaTest(){
		addStyleName("page-invoice-padding");
		craeteContents();
	}

	private void craeteContents() {
		createFakturLayout();
	}

	private void createFakturLayout() {
		
		setSizeUndefined();
		setSpacing(true);
		setWidth("100%");
		
		HorizontalLayout layout1 = createInvoiceLayout1();
		HorizontalLayout layout2 = createInvoiceLayout2();
		VerticalLayout layout3 = createInvoiceLayout3();
//		VerticalLayout layout4 = createInvoiceLayout4();
		VerticalLayout layout4 = createInvoiceLayout4b();
		VerticalLayout layout5 = createInvoiceLayout5();

		addComponent(layout1);
		addComponent(layout2);
		addComponent(layout3);
		addComponent(layout4);
		addComponent(layout5);
		
		setWidth("100%");
		
	}

	private VerticalLayout createInvoiceLayout4b() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addStyleName("faktura-section-layout");
		
		itemTable = new GridLayout();
		itemTable.setSpacing(true);
		itemTable.setWidth("100%");
		itemTable.setColumns(7);
		
		Label l0 = new Label("");
		Label l1 = new Label("Item");
		Label l2 = new Label("Quantity");
		Label l3 = new Label("Unit");
		Label l4 = new Label("Unit price");
		Label l5 = new Label("VAT");
		Label l6 = new Label("Total");
		
		l1.addStyleName("text-align-center");
		l2.addStyleName("text-align-center");
		l3.addStyleName("text-align-center");
		l4.addStyleName("text-align-center");
		l5.addStyleName("text-align-center");
		l6.addStyleName("text-align-center");
		
		itemTable.addComponent(l0);
		itemTable.addComponent(l1);
		itemTable.addComponent(l2);
		itemTable.addComponent(l3);
		itemTable.addComponent(l4);
		itemTable.addComponent(l5);
		itemTable.addComponent(l6);
		
		itemTable.setColumnExpandRatio(0, 0.5f);
		itemTable.setColumnExpandRatio(1, 1.5f);
		itemTable.setColumnExpandRatio(2, 0.5f);
		itemTable.setColumnExpandRatio(3, 0.5f);
		itemTable.setColumnExpandRatio(4, 1.0f);
		itemTable.setColumnExpandRatio(5, 1.0f);
		itemTable.setColumnExpandRatio(6, 1.0f);
		
		Button addButton = new Button("Add item");
		addButton.setPrimaryStyleName("friendly");
		addButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				itemList.add(new FakturaItem(itemTable, FakturaTest.this));
				itemTable.markAsDirty();
			}
			
		});
		
		layout.addComponent(itemTable);
		layout.addComponent(addButton);
		layout.setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);
		addButton.click();
		return layout;
	}



	private VerticalLayout createInvoiceLayout5() {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("faktura-section-layout");
		layout.setMargin(false);
		layout.setSpacing(true);
		
		HorizontalLayout layout1 = new HorizontalLayout();
		layout1.setWidth("100%");
		layout1.setMargin(false);
		layout1.setSpacing(true);
		
		Table calcTable = createCalcTable();
		VerticalLayout checkboxQrCodeLayout = new VerticalLayout();
		checkboxQrCodeLayout.setWidth("100%");
		checkboxQrCodeLayout.setMargin(false);
		checkboxQrCodeLayout.setSpacing(true);
		
		CheckBox manualCheckbox = new CheckBox("Manual input");
		manualCheckbox.setWidth("100%");
		manualCheckbox.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Collection cols = sumTotalTable.getItemIds();
				Boolean value = (Boolean) event.getProperty().getValue();
				
				for(Object c : cols){
					Object o = sumTotalTable.getItem(c);
					TextField basis = (TextField) sumTotalTable.getContainerProperty(o, BASIS).getValue();
					TextField vat = (TextField) sumTotalTable.getContainerProperty(o,VAT).getValue();
					TextField total = (TextField) sumTotalTable.getContainerProperty(o, TOTAL).getValue();
					
					basis.setReadOnly(!value);
					vat.setReadOnly(!value);
					total.setReadOnly(!value);
				}
				
			}
		});
		Image qrcode = new Image();
		qrcode.setWidth("100px");
		qrcode.setHeight("100px");
		qrcode.setSource(new ThemeResource("image/jpeg.jpg"));
		checkboxQrCodeLayout.addComponent(manualCheckbox);
		checkboxQrCodeLayout.addComponent(qrcode);
		checkboxQrCodeLayout.setComponentAlignment(manualCheckbox, Alignment.TOP_RIGHT);
		checkboxQrCodeLayout.setComponentAlignment(qrcode, Alignment.MIDDLE_LEFT);
		
		layout1.addComponent(checkboxQrCodeLayout);
		layout1.addComponent(calcTable);
		
		layout1.setComponentAlignment(calcTable, Alignment.TOP_RIGHT);
		layout1.setComponentAlignment(checkboxQrCodeLayout, Alignment.TOP_LEFT);
		layout1.setExpandRatio(checkboxQrCodeLayout, 1.0f);
		layout1.setExpandRatio(calcTable, 2.0f);
		
		HorizontalLayout layout2 = new HorizontalLayout();
		layout2.setWidth("100%");
		layout2.setMargin(false);
		layout2.setSpacing(true);
		
		HorizontalLayout leftlayout = createLeftLayout();
		HorizontalLayout rightLayout = createRightLayout();
		layout2.addComponent(leftlayout);
		layout2.addComponent(rightLayout);
		layout2.setComponentAlignment(leftlayout,Alignment.TOP_LEFT);
		layout2.setComponentAlignment(rightLayout,Alignment.TOP_RIGHT);
		
		layout.addComponent(layout1);
		layout.addComponent(layout2);
		
		return layout;
	}

	private HorizontalLayout createRightLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setMargin(false);
		layout.setSpacing(true);
		
		VerticalLayout layout1 = new VerticalLayout();
		VerticalLayout layout2 = new VerticalLayout();
		
		layout1.setWidth("100%");
		layout2.setWidth("100%");
		
		layout1.setMargin(false);
		layout1.setSpacing(true);
		
		layout2.setMargin(false);
		layout2.setSpacing(true);
		
		TextField noteText = new TextField();
		additionalAmountText = new TextField();
		Label totalLabel = new Label("Total to pay:");
		amountLabel = new Label("");
		Label paidLabel = new Label("Paid");
		paidLabel.addStyleName("text-align-right");
		paidLabel.addStyleName("distance-small");
		
		noteText.setWidth("100%");
		additionalAmountText.setWidth("100%");
		totalLabel.setWidth("100%");
		amountLabel.setWidth("100%");
		paidLabel.setWidth("100%");
		
		noteText.setInputPrompt("Note");
		additionalAmountText.setValue("0,0");
		additionalAmountText.addValueChangeListener(amountTextListener );
		
//		noteText.addStyleName("text-in-form");
//		amountText.addStyleName("text-in-form");
		additionalAmountText.addStyleName("text-align-right");
		amountLabel.addStyleName("text-align-right");
		
		layout1.addComponent(noteText);
		layout1.addComponent(totalLabel);
		layout2.addComponent(additionalAmountText);
		layout2.addComponent(amountLabel);
		
		totalLabel.addStyleName("label-big");
		amountLabel.addStyleName("label-big");
		paidLabel.addStyleName("label-big");
		
		layout.addComponent(paidLabel);
		layout.addComponent(layout1);
		layout.addComponent(layout2);
		
		layout.setExpandRatio(paidLabel, 1.0f);
		layout.setExpandRatio(layout1, 2.0f);
		layout.setExpandRatio(layout2, 1.0f);
		
		return layout;
	}

	private HorizontalLayout createLeftLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setMargin(false);
		layout.setSpacing(true);
		
		Label label1 = new Label("Note.:amounts are inclusive of value rounding");
		
		label1.addStyleName("label-big");
		
		layout.addComponent(label1);
		
		layout.setComponentAlignment(label1, Alignment.TOP_LEFT);
		
		return layout;
	}

	private Table createCalcTable() {
		sumTotalTable = new Table();
		sumTotalTable.setSortEnabled(false);
		sumTotalTable.setWidth("80%");
		sumTotalTable.addStyleName("total-calculate-table");
		sumTotalTable.addContainerProperty(LOCAL_CURRENCY, Component.class, null);
		sumTotalTable.addContainerProperty(BASIS, Component.class, null);
		sumTotalTable.addContainerProperty(VAT, Component.class, null);
		sumTotalTable.addContainerProperty(TOTAL, Component.class, null);
		
		sumTotalTable.setColumnExpandRatio(LOCAL_CURRENCY, 1.5f);
		sumTotalTable.setColumnExpandRatio(BASIS, 1.0f);
		sumTotalTable.setColumnExpandRatio(VAT, 1.5f);
		sumTotalTable.setColumnExpandRatio(TOTAL, 1.0f);
		
		sumTotalTable.setColumnAlignment(BASIS, Align.RIGHT);
		sumTotalTable.setColumnAlignment(VAT, Align.RIGHT);
		sumTotalTable.setColumnAlignment(TOTAL, Align.RIGHT);

		return sumTotalTable;
	}

	private VerticalLayout createInvoiceLayout3() {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("faktura-section-layout");
		layout.setMargin(false);
		layout.setSpacing(true);
		
		HorizontalLayout wrapperLayout = new HorizontalLayout();
		wrapperLayout.addStyleName("horizontal-spacing-big");
		wrapperLayout.setMargin(false);
		wrapperLayout.setSpacing(true);
		wrapperLayout.setWidth("100%");

		// VS, CS, SS
		VerticalLayout childLayout1 = new VerticalLayout();
		childLayout1.setMargin(false);
		childLayout1.setSpacing(true);
		childLayout1.setWidth("100px");

        TextField vsSymbol = new TextField("vs");
        TextField ksSymbol = new TextField("cs");
        TextField ssSymbol = new TextField("ss");
		
//		vsText.addStyleName("text-in-form");
//		csText.addStyleName("text-in-form");
//		ssText.addStyleName("text-in-form");

        vsSymbol.setWidth("100%");
        ksSymbol.setWidth("100%");
        ssSymbol.setWidth("100%");
		
//		childLayout1.addComponent(vsSymbol);
//		childLayout1.addComponent(ksSymbol);
		childLayout1.addComponent(ssSymbol);
		
		VerticalLayout childLayout2 = new VerticalLayout();
		childLayout2.setMargin(false);
		childLayout2.setSpacing(true);
		childLayout2.setWidth("150px");

        DateField issueDate = new DateField("Issue date");
        DateField maturityDate = new DateField("Due date");
        DateField dateOfTaxableSupply = new DateField("Date of taxable supply");
		
//		exposureDate.addStyleName("text-in-form");
//		maturityDate.addStyleName("text-in-form");
//		taxableDate.addStyleName("text-in-form");

        issueDate.setWidth("100%");
		maturityDate.setWidth("100%");
        dateOfTaxableSupply.setWidth("100%");
		
//		childLayout2.addComponent(issueDate);
		childLayout2.addComponent(maturityDate);
//		childLayout2.addComponent(dateOfTaxableSupply);
		
		VerticalLayout childLayout3 = new VerticalLayout();
		childLayout3.setMargin(false);
		childLayout3.setSpacing(true);
		childLayout3.setWidth("140px");
		
		ComboBox paymentCombo = new ComboBox("Payment");
		TextField orderNumberText = new TextField("Order number");
		currencyCombo = new ComboBox("Currency");
		currencyCombo.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				calculateTotalPrice();
			}
		});
		insertCurrency(currencyCombo);
		currencyCombo.setNullSelectionAllowed(false);
		
		paymentCombo.setWidth("100%");
		orderNumberText.setWidth("100%");
		currencyCombo.setWidth("100%");

        TextField orderNumber = new TextField ("orderNumber");
        ComboBox payment = new ComboBox("Payment");;
        ComboBox currency = new ComboBox("Currency");

        payment.setWidth("100%");
        orderNumber.setWidth("100%");
        currency.setWidth("100%");
		
//		paymentCombo.addStyleName("text-in-form");
//		orderNumberText.addStyleName("text-in-form");
//		currencyCombo.addStyleName("text-in-form");
		
		childLayout3.addComponent(payment);
//		childLayout3.addComponent(orderNumber);
//		childLayout3.addComponent(currency);
		
		VerticalLayout childLayout4 = new VerticalLayout();
		childLayout4.setMargin(false);
		childLayout4.setSpacing(true);
		childLayout4.setWidth("170px");

        TextField exchangeAmount = new TextField("Amount");
        TextField exchangeRate = new TextField("Exchange rate");
		
//		quantityText.addStyleName("text-in-form");
//		exchangeRateText.addStyleName("text-in-form");

        exchangeAmount.setWidth("100%");
        exchangeRate.setWidth("100%");
		
//		childLayout4.addComponent(exchangeAmount);
		childLayout4.addComponent(exchangeRate);
//		childLayout4.setComponentAlignment(exchangeAmount, Alignment.TOP_RIGHT);
		childLayout4.setComponentAlignment(exchangeRate, Alignment.TOP_RIGHT);
		
		wrapperLayout.addComponent(childLayout1);
		wrapperLayout.addComponent(childLayout2);
		wrapperLayout.addComponent(childLayout3);
		wrapperLayout.addComponent(childLayout4);		
		
		wrapperLayout.setComponentAlignment(childLayout1, Alignment.TOP_LEFT);
		wrapperLayout.setComponentAlignment(childLayout2, Alignment.TOP_CENTER);
		wrapperLayout.setComponentAlignment(childLayout3, Alignment.TOP_CENTER);
		wrapperLayout.setComponentAlignment(childLayout4, Alignment.TOP_RIGHT);		

		TextArea descriptionUpItemLabel = new TextArea();
        descriptionUpItemLabel.setRows(3);
//		noteText.addStyleName("text-in-form");
        descriptionUpItemLabel.addStyleName("text-center");
        descriptionUpItemLabel.setInputPrompt("Insert comment here");
        descriptionUpItemLabel.setWidth("100%");
		
		layout.addComponent(wrapperLayout);
		layout.addComponent(descriptionUpItemLabel);

		
		return layout;
	}

	private void insertCurrency(ComboBox currencyCombo) {
		currencyCombo.addItem("USD");
		currencyCombo.addItem("EUR");
		currencyCombo.addItem("CSK");
		currencyCombo.select("CSK");
	}

	private HorizontalLayout createInvoiceLayout2() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addStyleName("faktura-section-layout");
		layout.setSpacing(true);
		layout.setMargin(false);
		layout.setWidth("100%");
		
		//Faktura choice
		VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.addStyleName("faktura-middle-spacing");
		leftLayout.setMargin(false);
		leftLayout.setSpacing(true);
		leftLayout.setHeight("100%");
		
		ComboBox fakturaChoice = new ComboBox();
		final Button hideShowButton = new Button("Show advanced");
		hideShowButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(advancedLayout.isHidden()){
					advancedLayout.removeStyleName("hide-transition");
					advancedLayout.addStyleName("show-transition");
					advancedLayout.setHidden(false);
					addComponent(advancedLayout,2);
					hideShowButton.setCaption("Hide advanced");
				}else{
					advancedLayout.removeStyleName("show-transition");
					advancedLayout.addStyleName("hide-transition");
					advancedLayout.setHidden(true);
					removeComponent(advancedLayout);
					hideShowButton.setCaption("Show advanced");
				}
	
				
			}
		});
		hideShowButton.setPrimaryStyleName("borderless-colored");
//		fakturaChoice.addStyleName("text-in-form");
		fakturaChoice.setWidth("180px");
		
		HorizontalLayout bankLayout = createBankLayout();
		
		fakturaChoice.setInputPrompt("Please choose invoice");
		
		leftLayout.addComponent(fakturaChoice);
//		leftLayout.addComponent(bankLayout);
		leftLayout.addComponent(hideShowButton);
		leftLayout.setComponentAlignment(fakturaChoice, Alignment.TOP_LEFT);
//		leftLayout.setComponentAlignment(bankLayout, Alignment.MIDDLE_LEFT);
		leftLayout.setComponentAlignment(hideShowButton, Alignment.BOTTOM_LEFT);
		
//		leftLayout.setExpandRatio(fakturaChoice, 0.25f);
//		leftLayout.setExpandRatio(bankLayout, 0.75f);
		
		//Customer
		VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setCaption("Customer");
		rightLayout.setWidth("190px");
		rightLayout.setMargin(false);
		rightLayout.setSpacing(true);
		
		VerticalLayout customerLayout = new VerticalLayout();
		customerLayout.setMargin(false);
		customerLayout.setSpacing(true);
		customerLayout.setWidth("190px");
		
		ComboBox customerCombo = new ComboBox();
		TextField addressText = new TextField();
		
		HorizontalLayout zipCityLayout = new HorizontalLayout();
		
		zipCityLayout.setMargin(false);
		zipCityLayout.setSpacing(true);
		zipCityLayout.setWidth("100%");
		
		TextField zipText = new TextField();
		TextField cityText = new TextField();
		ComboBox countryText = new ComboBox();
		
		zipCityLayout.addComponent(zipText);
		zipCityLayout.addComponent(cityText);
		
		customerCombo.setInputPrompt("Customer");
		addressText.setInputPrompt("Address");
		zipText.setInputPrompt("Zip code");
		cityText.setInputPrompt("City");
		countryText.setInputPrompt("Country");
		
		customerCombo.setWidth("100%");
		addressText.setWidth("100%");
		zipText.setWidth("100%");
		cityText.setWidth("100%");
		countryText.setWidth("100%");
		
//		customerCombo.addStyleName("text-in-form");
//		addressText.addStyleName("text-in-form");
//		zipText.addStyleName("text-in-form");
//		cityText.addStyleName("text-in-form");
//		countryText.addStyleName("text-in-form");
		
		customerLayout.addComponent(customerCombo);
		customerLayout.addComponent(addressText);
		customerLayout.addComponent(zipCityLayout);
//		customerLayout.addComponent(countryText);
		
		rightLayout.addComponent(customerLayout);
		
		//Tax ARES
		HorizontalLayout taxAres = new HorizontalLayout();
		taxAres.setMargin(false);
		taxAres.setSpacing(true);
		taxAres.setWidth("190px");
		
		VerticalLayout taxLayout = new VerticalLayout();
		taxLayout.setMargin(false);
		taxLayout.setSpacing(true);
		taxLayout.setWidth("100%");
		
		TextField taxText = new TextField();
		ComboBox taxIdCombo = new ComboBox();
		
		taxText.setWidth("100%");
		taxIdCombo.setWidth("100%");
		
//		taxIdCombo.addStyleName("text-in-form");
//		taxText.addStyleName("text-in-form");
		
		taxLayout.addComponent(taxIdCombo);
		taxLayout.addComponent(taxText);
		
		Button aresButton = new Button("ARES");
		aresButton.setPrimaryStyleName("borderless-colored");
		
		taxAres.addComponent(taxLayout);
		taxAres.addComponent(aresButton);
		taxAres.setComponentAlignment(taxLayout, Alignment.TOP_LEFT);
		taxAres.setComponentAlignment(aresButton, Alignment.TOP_LEFT);
		
		rightLayout.addComponent(taxAres);
		
		layout.addComponent(leftLayout);
		layout.addComponent(rightLayout);
		
		layout.setComponentAlignment(leftLayout, Alignment.TOP_LEFT);
		layout.setComponentAlignment(rightLayout, Alignment.TOP_RIGHT);
		layout.setExpandRatio(leftLayout, 2.5f);
		layout.setExpandRatio(rightLayout, 1.0f);
		
		return layout;
	}

	private HorizontalLayout createBankLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.setSpacing(true);
		layout.setWidth("340px");
		
		VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.setMargin(false);
		leftLayout.setSpacing(true);
		
		Label bankAccount = new Label("Bank account");
		bankAccount.addStyleName("text-align-center");
		ComboBox bankAccountText = new ComboBox();
		TextField ibanText = new TextField();
		
		bankAccountText.setInputPrompt("Bank account");
		ibanText.setInputPrompt("IBAN");
		
//		bankAccountText.addStyleName("text-in-form");
//		ibanText.addStyleName("text-in-form");
		
		bankAccount.setWidth("100%");
		bankAccountText.setWidth("100%");
		ibanText.setWidth("100%");
		
		leftLayout.addComponent(bankAccount);
		leftLayout.addComponent(bankAccountText);
		leftLayout.addComponent(ibanText);
		
		VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setMargin(false);
		rightLayout.setSpacing(true);
		
		Label bankCode  = new Label("Bank code");
		bankCode.addStyleName("text-align-center");
		ComboBox bankCodeCombo = new ComboBox();
		TextField swiftText = new TextField();
		
		bankCodeCombo.setInputPrompt("Bank code");
		swiftText.setInputPrompt("SWIFT");
		
		bankCode.setWidth("100%");
		bankCodeCombo.setWidth("100%");
		swiftText.setWidth("100%");
		
//		bankCodeCombo.addStyleName("text-in-form");
//		swiftText.addStyleName("text-in-form");
		
		rightLayout.addComponent(bankCode);
		rightLayout.addComponent(bankCodeCombo);
		rightLayout.addComponent(swiftText);
		
		layout.addComponent(leftLayout);
		layout.addComponent(rightLayout);
		layout.setExpandRatio(leftLayout, 1.5f);
		layout.setExpandRatio(rightLayout, 1f);
		
		return layout;
	}

	private HorizontalLayout createInvoiceLayout1() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.addStyleName("faktura-section-layout");
		layout.setMargin(false);
		layout.setSpacing(true);
		
		HorizontalLayout leftLayout = new HorizontalLayout();
		VerticalLayout recipientLayout = new VerticalLayout();
		VerticalLayout vendorLayout = new VerticalLayout();
		VerticalLayout aresLayout = new VerticalLayout();
		VerticalLayout documentLayout = new VerticalLayout();
		
		leftLayout.setMargin(false);
		leftLayout.setSpacing(true);
		aresLayout.setHeight("50%");
		
		Button aresButton = new Button("ARES");
		aresButton.setPrimaryStyleName("borderless-colored");
		aresLayout.addComponent(aresButton);
		aresLayout.addComponent(new Label(" "));
		aresLayout.setComponentAlignment(aresButton, Alignment.TOP_LEFT);
		
		recipientLayout.setMargin(false);
		vendorLayout.setMargin(false);
		aresLayout.setMargin(false);
		
		recipientLayout.setSpacing(true);
		vendorLayout.setSpacing(true);
		aresLayout.setSpacing(true);
		
		leftLayout.addComponent(recipientLayout);
		leftLayout.addComponent(vendorLayout);
		leftLayout.addComponent(aresLayout);
		
		leftLayout.setComponentAlignment(recipientLayout, Alignment.BOTTOM_CENTER);
		leftLayout.setComponentAlignment(vendorLayout, Alignment.BOTTOM_CENTER);
		leftLayout.setComponentAlignment(aresLayout, Alignment.BOTTOM_LEFT);
		
		layout.addComponent(leftLayout);
		layout.addComponent(documentLayout);
		layout.setExpandRatio(leftLayout, 2.0f);
		layout.setExpandRatio(documentLayout, 0.7f);
		
		layout.setComponentAlignment(leftLayout, Alignment.BOTTOM_LEFT);
		layout.setComponentAlignment(documentLayout, Alignment.BOTTOM_RIGHT);
		
//		leftLayout.setExpandRatio(recipientLayout, 1.0f);
//		leftLayout.setExpandRatio(vendorLayout, 0.5f);
//		leftLayout.setExpandRatio(aresLayout, 0.5f);
		recipientLayout.setWidth("180px");
		
		//Recipient
		recipientLayout.addStyleName("default-v-spacing");
		ComboBox supplierName = new ComboBox();
		final TextField supplierStreet = new TextField();
		HorizontalLayout zipCityLayout = new HorizontalLayout();
		TextField supplierZip = new TextField();
		TextField supplierCity = new TextField();
		ComboBox supplierState = new ComboBox();
		
		supplierStreet.setValidationVisible(false);
		supplierStreet.addValidator(validator);
		supplierStreet.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				supplierStreet.validate();
			}
		});
		
		supplierName.setWidth("100%");
		supplierStreet.setWidth("100%");
		supplierState.setWidth("100%");
		zipCityLayout.setWidth("100%");
		supplierZip.setWidth("100%");
		supplierCity.setWidth("100%");
		
		supplierName.setInputPrompt("Supplier name");
		supplierStreet.setInputPrompt("Street and no. p.");
		supplierZip.setInputPrompt("Zip code");
		supplierCity.setInputPrompt("City");
		supplierState.setInputPrompt("State");
		
//		supplierName.addStyleName("text-in-form");
//		supplierStreet.addStyleName("text-in-form");
//		supplierZip.addStyleName("text-in-form");
//		supplierCity.addStyleName("text-in-form");
//		supplierState.addStyleName("text-in-form");
		
		zipCityLayout.setSpacing(true);
		
		zipCityLayout.addComponent(supplierZip);
		zipCityLayout.addComponent(supplierCity);
		
		recipientLayout.addComponent(supplierName);
		recipientLayout.addComponent(supplierStreet);
		recipientLayout.addComponent(zipCityLayout);
		recipientLayout.addComponent(supplierState);
		
		//Vendor
		ComboBox vendorId = new ComboBox();
		TextField vendorTax = new TextField();
		
		vendorId.setInputPrompt("IR");
		vendorTax.setInputPrompt("Tax");
		
		vendorId.setWidth("100%");
		vendorTax.setWidth("100%");
		
		vendorId.addStyleName("text-in-form");
		vendorTax.addStyleName("text-in-form");
		
		vendorLayout.setWidth("100px");
		vendorLayout.addComponent(vendorId);
		vendorLayout.addComponent(vendorTax);
		
		//Document
		documentLayout.setMargin(false);
		documentLayout.setSpacing(true);
		
		TextField seqDocument = new TextField();
		TextField docNumber = new TextField();
		
		seqDocument.setWidth("100%");
		docNumber.setWidth("100%");
		
//		seqDocument.addStyleName("text-in-form");
//		docNumber.addStyleName("text-in-form");
		
		documentLayout.setWidth("180px");
		documentLayout.addComponent(seqDocument);
		documentLayout.addComponent(docNumber);
		
		return layout;
	}

	public void calculateTotal() {
		double basis = 0;
		double vatPrice = 0;
		double total = 0;
		
		resetAllPrice();
		
		for(FakturaItem item: itemList){
			ComboBox vatComboBox = item.getVatComboBox();
			Integer rate = (Integer) vatComboBox.getValue();
			
			basis = item.getPriceCalc();
			total = item.getTotalCalc();
			
			vatPrice = total - basis; 			
			calculateRate(rate, vatPrice, basis, total);
			
		}
		
		insertRateValueIntoTable();
		calculateSubTotal();
		calculateTotalPrice();
		
	}

	private void resetAllPrice() {
		rate0 = 0;
		rate10 = 0;
		rate15 = 0;
		rate21 = 0;
		basic21 = 0;
		total21 = 0;
		basic15 = 0;
		total15 = 0;
		total10 = 0;
		basic10 = 0;
		total0 = 0;
		basic0 = 0;
		
	}

	private void calculateSubTotal() {
		double basicTotal = basic0 + basic10 + basic15 + basic21;
		double vatTotal = rate0 + rate10 + rate15 + rate21;
		subTotal = total0 + total10 + total15 +total21;
		
		sumTotalTable.setFooterVisible(true);
		sumTotalTable.setColumnFooter(LOCAL_CURRENCY, "Total");
		sumTotalTable.setColumnFooter(BASIS, decimalFormat.format(basicTotal));
		sumTotalTable.setColumnFooter(VAT, decimalFormat.format(vatTotal));
		sumTotalTable.setColumnFooter(TOTAL, decimalFormat.format(subTotal));
	}

	private void calculateTotalPrice() {
		if(additionalAmountText == null)
			return;
		
		String additionalAmount = additionalAmountText.getValue().trim();
		double additionValue = 0;
		try{
			if(additionalAmount.equals("-")){
				return;
			}
			if(additionalAmount.equals("")){
				additionalAmount = "0";
			}
			double addition = Double.parseDouble(additionalAmount);
			additionValue = addition;
			double totalOfTotal = additionValue + subTotal;
			
			String currency = (String) currencyCombo.getValue();
			this.amountLabel.setValue(decimalFormat.format(totalOfTotal)+currency);
		}catch(Exception e){
			additionalAmountText.setValue("");
		}
		
	}

	private void insertRateValueIntoTable() {
		sumTotalTable.removeAllItems();
		Item i = null;
		
		if(total0 > 0){
			i = sumTotalTable.addItem(1);
			insertRateItemIntoTable(i, "Basic rate 0%", basic0, rate0, total0);
		}
		if(total10 > 0){
			i = sumTotalTable.addItem(10);
			insertRateItemIntoTable(i,"Rate 10%", basic10, rate10, total10);			
		}
		if(total15 > 0){
			i = sumTotalTable.addItem(15);
			insertRateItemIntoTable(i,"Rate 15%", basic15, rate15, total15);
		}
		if(total21 > 0){
			i = sumTotalTable.addItem(21);
			insertRateItemIntoTable(i,"Rate 21%", basic21, rate21, total21);
		}
		
		sumTotalTable.markAsDirty();
	}

	private void insertRateItemIntoTable(Item i, String caption, double basic,
			double rate, double total) {
		
		TextField basis = new TextField();
		TextField vat = new TextField();
		TextField totalText = new TextField();
		
		Label l = new Label();
		l.addStyleName("distance-small");
		
		basis.setWidth("100%");
		vat.setWidth("100%");
		totalText.setWidth("100%");
		
		basis.addStyleName("text-no-focus-effect");
		vat.addStyleName("text-no-focus-effect");
		totalText.addStyleName("text-no-focus-effect");
		
		basis.addStyleName("text-align-right");
		vat.addStyleName("text-align-right");
		totalText.addStyleName("text-align-right");
		
		basis.addStyleName("distance-right");
		vat.addStyleName("distance-right");
		totalText.addStyleName("distance-right");
		
		l.setValue(caption);

		basis.setValue(decimalFormat.format(basic));
		vat.setValue(String.valueOf(decimalFormat.format(rate)));
		totalText.setValue(String.valueOf(decimalFormat.format(total)));
		
		basis.setReadOnly(true);
		vat.setReadOnly(true);
		totalText.setReadOnly(true);
		
		i.getItemProperty(LOCAL_CURRENCY).setValue(l);
		i.getItemProperty(BASIS).setValue(basis);
		i.getItemProperty(VAT).setValue(vat);
		i.getItemProperty(TOTAL).setValue(totalText);
	}

	private void calculateRate(Integer rate, double vatPrice, double basis, double total) {
		
		if (rate==21){
			rate21 = rate21 + vatPrice;
			basic21 = basic21 + basis;
			total21 = total21 + total;
        }
		else if (rate==15){
        	rate15 = rate15 + vatPrice;
        	basic15 = basic15 + basis;
			total15 = total15 + total;
        }
        else if (rate==10){
        	rate10 = rate10 + vatPrice;
        	basic10 = basic10 + basis;
			total10 = total10 + total;
        }
        else{
        	basic0 = basic0 + basis;
			total0 = total0 + total;
        	rate0 = rate0 + vatPrice;
        }
	}

	public void removeRow(FakturaItem fakturaItem) {
		itemList.remove(fakturaItem);
		
	}
}
 
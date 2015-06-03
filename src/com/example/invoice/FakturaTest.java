package com.example.invoice;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
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
				supplierStreet.addStyleName("validation-error");
				throw new InvalidValueException("This field is required");
			}
			else{
				supplierStreet.removeStyleName("validation-error");
			}
			
		}
	};
	private BankDetailLayoutController bankController;
	private CustomerDetailLayoutController customerController;
	private PaymentLayoutController paymentController;
	private TextField supplierStreet;
	private Button hideButton;
	private ClickListener hideTextDescriptionListener = new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				String caption = hideButton.getCaption();
				for(FakturaItem item: itemList){
					TextArea descriptionText = item.getDescriptionText();
					if(caption.equals("Show")){
						descriptionText.setVisible(true);
					}
					else{
						descriptionText.setVisible(false);
					}
				}
				if(caption.equals("Show")){
					hideButton.setCaption("Hide");
				}
				else{
					hideButton.setCaption("Show");
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
		itemTable.setColumns(8);
		
		Label l1 = new Label("");
		Label l2 = new Label("Item");
		Label l3 = new Label("Quantity");
		Label l4 = new Label("Unit");
		Label l5 = new Label("Unit price");
		Label l6 = new Label("VAT");
		Label l7 = new Label("Total");
		
		l1.addStyleName("text-align-center");
		l2.addStyleName("text-align-center");
		l3.addStyleName("text-align-center");
		l4.addStyleName("text-align-center");
		l5.addStyleName("text-align-center");
		l6.addStyleName("text-align-center");
		l7.addStyleName("text-align-center");
		
		itemTable.addComponent(l1);
		itemTable.addComponent(l2);
		itemTable.addComponent(l3);
		itemTable.addComponent(l4);
		itemTable.addComponent(l5);
		itemTable.addComponent(l6);
		itemTable.addComponent(l7);
		
		l1.setWidth("35px");
		l2.setWidth("100%");
		l3.setWidth("45px");
		l4.setWidth("43px");
		l5.setWidth("80px");
		l6.setWidth("80px");
		l7.setWidth("85px");
		
		itemTable.setColumnExpandRatio(0, 0.0f);
		itemTable.setColumnExpandRatio(1, 1.0f);
		itemTable.setColumnExpandRatio(2, 0.0f);
		itemTable.setColumnExpandRatio(3, 0.0f);
		itemTable.setColumnExpandRatio(4, 0.0f);
		itemTable.setColumnExpandRatio(5, 0.0f);
		itemTable.setColumnExpandRatio(6, 0.0f);
		
		hideButton = new Button("Show");
		hideButton.setPrimaryStyleName("borderless-colored");
		hideButton.addClickListener(hideTextDescriptionListener);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		
		Button addButton = new Button();
		addButton.setPrimaryStyleName("friendly");
		addButton.addStyleName("add");
		addButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				itemList.add(new FakturaItem(itemTable, FakturaTest.this, hideButton));
				itemTable.markAsDirty();
			}
			
		});
		
		buttonLayout.addComponent(addButton);
		buttonLayout.addComponent(hideButton);
		
		layout.addComponent(itemTable);
		layout.addComponent(buttonLayout);
		layout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);
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
		manualCheckbox.addStyleName("checbox-colored");
		manualCheckbox.addStyleName("distance-little-top");
		manualCheckbox.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Container container = sumTotalTable.getContainerDataSource();
				Collection cols = container.getItemIds();
				Boolean value = (Boolean) event.getProperty().getValue();
				
				for(Object c : cols){
					TextField basis = (TextField) container.getContainerProperty(c, BASIS).getValue();
					TextField vat = (TextField) container.getContainerProperty(c, VAT).getValue();
					TextField total = (TextField) container.getContainerProperty(c, TOTAL).getValue();
					
					basis.setReadOnly(!value);
					vat.setReadOnly(!value);
					total.setReadOnly(!value);
				}
				
			}
		});
		Image qrcode = new Image();
		qrcode.setWidth("125px");
		qrcode.setHeight("125px");
		qrcode.setSource(new ThemeResource("images/barcode.png"));
		checkboxQrCodeLayout.addComponent(qrcode);
		checkboxQrCodeLayout.setComponentAlignment(qrcode, Alignment.MIDDLE_LEFT);
		
		layout1.addComponent(checkboxQrCodeLayout);
		layout1.addComponent(manualCheckbox);
		layout1.addComponent(calcTable);
		
		layout1.setComponentAlignment(calcTable, Alignment.TOP_RIGHT);
		layout1.setComponentAlignment(manualCheckbox, Alignment.TOP_RIGHT);
		layout1.setComponentAlignment(checkboxQrCodeLayout, Alignment.TOP_LEFT);
		
		layout1.setExpandRatio(checkboxQrCodeLayout, 2.5f);
		layout1.setExpandRatio(calcTable, 5.5f);
		layout1.setExpandRatio(manualCheckbox, 1.5f);
		
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
		layout2.setExpandRatio(leftlayout, 4.0f);
		layout2.setExpandRatio(rightLayout, 6.0f);
		
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
		
		totalLabel.addStyleName("bigger-bold");
		amountLabel.addStyleName("bigger-bold");
		
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
		
		layout.setExpandRatio(paidLabel, 13.0f);
		layout.setExpandRatio(layout1, 57.0f);
		layout.setExpandRatio(layout2, 30.0f);
		
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
		sumTotalTable.setWidth("100%");
		sumTotalTable.addStyleName("total-calculate-table");
		sumTotalTable.addContainerProperty(LOCAL_CURRENCY, Component.class, null);
		sumTotalTable.addContainerProperty(BASIS, Component.class, null);
		sumTotalTable.addContainerProperty(VAT, Component.class, null);
		sumTotalTable.addContainerProperty(TOTAL, Component.class, null);
		
		sumTotalTable.setColumnExpandRatio(LOCAL_CURRENCY, 1.5f);
		sumTotalTable.setColumnExpandRatio(BASIS, 1.2f);
		sumTotalTable.setColumnExpandRatio(VAT, 1.5f);
		sumTotalTable.setColumnExpandRatio(TOTAL, 0.8f);
		
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

        TextField vsSymbol = new TextField("VS");
        TextField ksSymbol = new TextField("CS");
        TextField ssSymbol = new TextField("SS");
        
        DateField issueDate = new DateField("Issue date");
        DateField maturityDate = new DateField("Due date");
        DateField dateOfTaxableSupply = new DateField("Date of taxable supply");
        
        ComboBox paymentCombo = new ComboBox("Payment");
        
        TextField orderNumberText = new TextField("Order number");
        currencyCombo = new ComboBox("Currency");
        currencyCombo.addValueChangeListener(new ValueChangeListener() {
        	
        	@Override
        	public void valueChange(ValueChangeEvent event) {
        		calculateTotalPrice();
        	}
        });
        
        TextField exchangeAmount = new TextField("Amount");
        TextField exchangeRate = new TextField("Exchange rate");
        
        paymentController = new PaymentLayoutController(wrapperLayout, vsSymbol, ksSymbol, ssSymbol, issueDate, maturityDate, dateOfTaxableSupply, paymentCombo, orderNumberText, currencyCombo, exchangeAmount, exchangeRate);
        paymentController.hideDetail();

		insertCurrency(currencyCombo);
		currencyCombo.setNullSelectionAllowed(false);
		
		TextArea descriptionUpItemLabel = new TextArea();
        descriptionUpItemLabel.setRows(3);
        descriptionUpItemLabel.addStyleName("text-center");
        descriptionUpItemLabel.setInputPrompt("Insert comment here");
        descriptionUpItemLabel.setWidth("100%");
        
		final Button hideShowButton = new Button("Show payment detail");
		hideShowButton.setPrimaryStyleName("borderless-colored");
		hideShowButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(paymentController.isHidden()){
					paymentController.showDetail();
					hideShowButton.setCaption("Hide payment detail");
				}
				else{
					paymentController.hideDetail();
					hideShowButton.setCaption("Show payment detail");
				}
				
			}
		});
		
		layout.addComponent(wrapperLayout);
		layout.addComponent(hideShowButton);
		//layout.addComponent(descriptionUpItemLabel);

		
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
		layout.setHeight("270px");
		
		//Faktura choice
		VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.addStyleName("faktura-middle-spacing");
		leftLayout.setMargin(false);
		leftLayout.setSpacing(true);
		leftLayout.setHeight("100%");
		
		ComboBox fakturaChoice = new ComboBox();

		fakturaChoice.setWidth("180px");
		
		VerticalLayout bankLayout = createBankLayout();
		
		
		fakturaChoice.setInputPrompt("Please choose invoice");
		
		leftLayout.addComponent(fakturaChoice);
		leftLayout.addComponent(bankLayout);
		leftLayout.setComponentAlignment(fakturaChoice, Alignment.TOP_LEFT);
		leftLayout.setComponentAlignment(bankLayout, Alignment.MIDDLE_LEFT);
		
		leftLayout.setExpandRatio(fakturaChoice, 0.45f);
		leftLayout.setExpandRatio(bankLayout, 0.55f);
		
		//Customer
		final VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setCaption("Customer");
		rightLayout.setWidth("180px");
		rightLayout.setMargin(false);
		rightLayout.setSpacing(true);
		
		final VerticalLayout customerLayout = new VerticalLayout();
		customerLayout.setMargin(false);
		customerLayout.setSpacing(true);
		customerLayout.setWidth("100%");
		
		ComboBox customerCombo = new ComboBox();
		HorizontalLayout customerIdLayout = new HorizontalLayout();
		
		customerIdLayout.setMargin(false);
		customerIdLayout.setSpacing(true);
		customerIdLayout.setWidth("100%");

		Button aresButton = new Button("ARES");
		aresButton.setPrimaryStyleName("borderless-colored");
		aresButton.addStyleName("text-center");
		ComboBox taxIdCombo = new ComboBox();
		taxIdCombo.setWidth("110px");
		
		customerIdLayout.addComponent(taxIdCombo);
		customerIdLayout.addComponent(aresButton);
		
		customerCombo.setInputPrompt("Customer");	
		customerCombo.setWidth("100%");
		
		final Button customerAdvancedButton = new Button("Show customer detail");
		customerAdvancedButton.setWidth("45px");
		customerController = new CustomerDetailLayoutController(customerLayout, customerCombo, taxIdCombo, aresButton);
		customerAdvancedButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(customerController.isHidden()){
					customerController.showDetail();
					customerAdvancedButton.setCaption("Hide customer detail");
				}else{
					customerController.hideDetail();
					customerAdvancedButton.setCaption("Show customer detail");
				}
			}
		});
		
		customerController.hideDetail();
		customerAdvancedButton.setPrimaryStyleName("borderless-colored");
		customerAdvancedButton.setWidth("100%");
	
		rightLayout.addComponent(customerLayout);
		rightLayout.addComponent(customerAdvancedButton);

		
		layout.addComponent(leftLayout);
		layout.addComponent(rightLayout);
		
		layout.setComponentAlignment(leftLayout, Alignment.TOP_LEFT);
		layout.setComponentAlignment(rightLayout, Alignment.TOP_RIGHT);
		layout.setExpandRatio(leftLayout, 2.0f);
		layout.setExpandRatio(rightLayout, 1.0f);
		
		return layout;
	}

	private VerticalLayout createBankLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.setSpacing(true);
		//layout.setWidth("340px");
		
		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setMargin(false);
		wrapper.setSpacing(true);
		wrapper.setWidth("100%");
		
		Label bankAccount = new Label("Bank account");
		Label bankCode  = new Label("Bank code");
		ComboBox bankAccountCombo = new ComboBox();
		ComboBox bankCodeCombo = new ComboBox();
		TextField ibanText = new TextField();
		TextField swiftText = new TextField();
		
		bankAccount.addStyleName("text-align-center");
		bankAccountCombo.setInputPrompt("Bank account");
		ibanText.setInputPrompt("IBAN");
		
		bankAccount.setWidth("100%");
		bankAccountCombo.setWidth("100%");
		ibanText.setWidth("100%");
		
		bankAccountCombo.addStyleName("bigger-bold");
		
		bankCode.addStyleName("text-align-center");
		bankCodeCombo.setInputPrompt("Bank code");
		swiftText.setInputPrompt("SWIFT");
		
		bankCode.setWidth("100%");
		bankCodeCombo.setWidth("100%");
		swiftText.setWidth("100%");
		
		final Button bankControllButton = new Button("Show bank detail");
		bankControllButton.setPrimaryStyleName("borderless-colored");
		bankControllButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(bankController.isHidden()){
					bankController.showDetail();
					bankControllButton.setCaption("Hide bank detail");
				}else{
					bankController.hideDetail();
					bankControllButton.setCaption("Show bank detail");
				}
				
			}
		});
		
		bankController = new BankDetailLayoutController(layout, wrapper, bankAccount, bankCode, bankAccountCombo, bankCodeCombo, ibanText, swiftText, bankControllButton);
		
		bankController.hideDetail();
		
		return wrapper;
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
		layout.setExpandRatio(documentLayout, 1.0f);
		
		layout.setComponentAlignment(leftLayout, Alignment.BOTTOM_LEFT);
		layout.setComponentAlignment(documentLayout, Alignment.BOTTOM_RIGHT);
		
//		leftLayout.setExpandRatio(recipientLayout, 1.0f);
//		leftLayout.setExpandRatio(vendorLayout, 0.5f);
//		leftLayout.setExpandRatio(aresLayout, 0.5f);
		recipientLayout.setWidth("180px");
		
		//Recipient
		recipientLayout.addStyleName("default-v-spacing");
		ComboBox supplierName = new ComboBox();
		supplierStreet = new TextField();
		HorizontalLayout zipCityLayout = new HorizontalLayout();
		TextField supplierZip = new TextField();
		TextField supplierCity = new TextField();
		ComboBox supplierState = new ComboBox();
		
		supplierName.setDescription("Suppiler name");
		supplierZip.setDescription("Suppiler zip");
		supplierCity.setDescription("Suppiler city");
		supplierState.setDescription("Suppiler country");
		supplierStreet.setDescription("Supplier address");
		
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
		supplierZip.setWidth("55px");
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
		zipCityLayout.setExpandRatio(supplierZip, 0.3f);
		zipCityLayout.setExpandRatio(supplierCity, 0.7f);
		
		recipientLayout.addComponent(supplierName);
		recipientLayout.addComponent(supplierStreet);
		recipientLayout.addComponent(zipCityLayout);
		recipientLayout.addComponent(supplierState);
		
		//Vendor
		ComboBox vendorId = new ComboBox();
		TextField vendorTax = new TextField();
		
		vendorId.setInputPrompt("IR");
		vendorTax.setInputPrompt("Tax");
		
		vendorId.setWidth("110px");
		vendorTax.setWidth("100%");
		
		vendorId.addStyleName("text-in-form");
		vendorTax.addStyleName("text-in-form");
		
		vendorLayout.setWidth("110px");
		vendorLayout.addComponent(vendorId);
		vendorLayout.addComponent(vendorTax);
		
		//Document
		documentLayout.setMargin(false);
		documentLayout.setSpacing(true);
		
		TextField seqDocument = new TextField();
		TextField docNumber = new TextField();
		
		seqDocument.addStyleName("bigger-bold");
		
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
 
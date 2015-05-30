package com.example.comp;

import java.text.DecimalFormat;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout.Area;


public class FakturaItem {

	private GridLayout parent;
	private Button hideButton;
	private ComboBox item;
	private TextField quantity;
	private TextField unit;
	private TextField priceUnit;
	private ComboBox vatComboBox;
	private TextField total;
	private TextArea descriptionText;
	private boolean calcQuantity = false;
	private boolean calcAmount = false;
	private boolean calcUnitPrice = false;	
	private DecimalFormat decimalFormat = new DecimalFormat("# ##0.00");
	private ValueChangeListener calculateItemListener = new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				calc();
			}
		};
	
	
	private FakturaTest fakturaTest;

	private double totalCalc;
	private double priceCalc;
	
	public FakturaItem(GridLayout parent, FakturaTest fakturaTest){
		this.parent = parent;
		this.fakturaTest = fakturaTest;
		init();
	}
	
	private boolean isValueNumeric(TextField textField){
		String value = textField.getValue();
		try{
			Double.parseDouble(value);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	  public void calc(){
			
			String quantityV = quantity.getValue().trim().replaceAll(",", ".");
			String unitPriceV = priceUnit.getValue().trim().replaceAll(",", ".");
			String amountV = total.getValue().trim().replaceAll(",", ".");

	        quantityV = quantityV.replaceAll("\\s+","");
	        unitPriceV = unitPriceV.replaceAll("\\s+","");
	        amountV = amountV.replaceAll("\\s+","");
			
			formatValue(quantityV, unitPriceV, amountV);
			
			if(!calcQuantity && !calcUnitPrice && !calcAmount){
				if(quantityV.equals("") && !unitPriceV.equals("") && !amountV.equals(""))
					calcQuantity = true;
				else if(unitPriceV.equals("") && !quantityV.equals("") && !amountV.equals(""))
					calcUnitPrice = true;
				else if(amountV.equals("") && !quantityV.equals("") && !unitPriceV.equals(""))
					calcAmount = true;		
			}
			
			if(quantityV.equals("") && !unitPriceV.equals("") && !amountV.equals("")){
				calcQuantity=true;
                calcAmount=false;
                calcUnitPrice=false;
			}
			else if(unitPriceV.equals("") && !quantityV.equals("") && !amountV.equals("")){
				calcQuantity=false;
                calcAmount=false;
                calcUnitPrice=true;
			}else if(amountV.equals("") && !quantityV.equals("") && !quantityV.equals("")){
				 calcQuantity=false;
	             calcAmount=true;
	             calcUnitPrice=false;
			}

			String tax = "";
	        if (vatComboBox.getValue().equals(21)){
	            tax = "21";
	        }
	        if (vatComboBox.getValue().equals(15)){
	            tax = "15";
	        }
	        if (vatComboBox.getValue().equals(10)){
	            tax = "10";
	        }
	        if (vatComboBox.getValue().equals(0)){
	            tax = "0";
	        }
			
			if(calcAmount)
				calcAmount(quantityV, unitPriceV, tax);
			else if(calcQuantity)
				calcQuantity(unitPriceV, amountV, tax);
			else if(calcUnitPrice)
				calcUnitPrice(quantityV, amountV, tax);
			else{
				//Do nothing			
			}
		}
		private void formatValue(String quantityV, String unitPriceV, String amountV) {
			try{			
				if(!quantityV.equals("")){
					double value = Double.parseDouble(quantityV.replace(',', '.'));
	                String finalValue = decimalFormat.format(value).replace('.', ',');
	                if (value<0){
	                    finalValue="-"+finalValue;
	                }
	                quantity.setValue(finalValue);
				}
				if(!unitPriceV.equals("")){
					double value = Double.parseDouble(unitPriceV.replace(',', '.'));
	                String finalValue = decimalFormat.format(value).replace('.', ',');
	                if (value<0){
	                    finalValue="-"+finalValue;
	                }
	                priceUnit.setValue(finalValue);
				}
				if(!amountV.equals("")){
					double value = Double.parseDouble(amountV.replace(',', '.'));
	                String finalValue = decimalFormat.format(value).replace('.', ',');
	                if (value<0){
	                    finalValue="-"+finalValue;
	                }
	                total.setValue(finalValue);
				}
			}catch(Exception e){
				return;
			}
		}
		  private void calcQuantity(String unitPriceV, String amountV, String taxV)  {
				this.quantity.setEnabled(true);
				if(unitPriceV.equals("") || amountV.equals(""))
					return;
				try{
					double unitPrice = Double.parseDouble(unitPriceV);
					double amount = Double.parseDouble(amountV);
					double tax = 0;
					if(!taxV.equals(""))
						tax = Double.parseDouble(taxV);
					double taxCalc = 1+(tax / 100);
					double basePrice = amount / taxCalc;
					double quantityCalc =  basePrice/unitPrice;
	
					priceCalc = unitPrice * quantityCalc;
					totalCalc = basePrice;
					
	              String finalValue = decimalFormat.format(quantityCalc).replace('.', ',');
	              if (quantityCalc<0){
	                  finalValue="-"+finalValue;
	              }
	              this.quantity.setValue(finalValue);
	              this.quantity.setEnabled(false);
				  triggerSubtotalOperation();
				}catch(Exception e){
					return;
				}
		}	  
	  
		private void calcAmount(String quantityV, String unitPriceV, String taxV) {
			this.total.setEnabled(true);
			if(quantityV.equals("") || unitPriceV.equals(""))
				return;
			try{
				double unitPrice = Double.parseDouble(unitPriceV);
				double quantity = Double.parseDouble(quantityV);
				double tax = 0;	
				if(!taxV.equals(""))
					tax = Double.parseDouble(taxV);
				double taxCalc = (unitPrice * quantity) * (tax / 100);
				totalCalc =  ((quantity * unitPrice) + taxCalc);
				priceCalc = unitPrice * quantity;
	            String finalValue = decimalFormat.format(totalCalc).replace('.', ',');
	            if (totalCalc<0){
	                finalValue="-"+finalValue;
	            }
	            this.total.setValue(finalValue);
	            if (totalCalc!=0){
	                this.total.setEnabled(false);
				    triggerSubtotalOperation();
	            }
			}catch(Exception e){
				return;
			}
		}
		private void triggerSubtotalOperation(){
	        if (fakturaTest !=null){
			    fakturaTest.calculateTotal();
	        }
		}

		private void calcUnitPrice(String quantityV, String amountV, String taxV) {
			this.priceUnit.setEnabled(true);
			if(quantityV.equals("") || amountV.equals(""))
				return;
			try{
				double quantity = Double.parseDouble(quantityV);
				double totalAmount = Double.parseDouble(amountV);
				double tax = 0;
				if(!taxV.equals(""))
					tax = Double.parseDouble(taxV);
				double taxCalc = tax / 100;
				double taxOne = taxCalc + 1;
				double totalR = totalAmount / taxOne;
				
				priceCalc =  totalR / quantity;
				totalCalc = totalAmount;
				
                String finalValue = decimalFormat.format(priceCalc).replace('.', ',');
                if (priceCalc<0){
                    finalValue="-"+finalValue;
                }
                priceUnit.setValue(finalValue);
				this.priceUnit.setEnabled(false);
				
				triggerSubtotalOperation();
			}catch(Exception e){
				return;
			}
	}		
	
	private void deleteRowOnItemTable(Button deleteButton){
		Area a = parent.getComponentArea(deleteButton);
		int rowNum = a.getRow1();
		parent.removeRow(rowNum);
		parent.removeRow(rowNum);
	}
	
	private void init() {
		hideButton = new Button("Hide");
		hideButton.setPrimaryStyleName("danger");
		item = new ComboBox();
		quantity = new TextField();
		unit = new TextField();
		priceUnit = new TextField();
		vatComboBox = new ComboBox();
		total = new TextField();
		descriptionText = new TextArea();
		hideButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteRowOnItemTable(event.getButton());
				fakturaTest.removeRow(FakturaItem.this);
				triggerSubtotalOperation();
			}
		});
		
		quantity.addValueChangeListener(calculateItemListener);
		priceUnit.addValueChangeListener(calculateItemListener);
		vatComboBox.addValueChangeListener(calculateItemListener);
		total.addValueChangeListener(calculateItemListener);
		
		quantity.setImmediate(true);
		priceUnit.setImmediate(true);
		vatComboBox.setImmediate(true);
		total.setImmediate(true);
		
		vatComboBox.addItem(0);
		vatComboBox.addItem(10);
		vatComboBox.addItem(15);
		vatComboBox.addItem(21);
		vatComboBox.setItemCaption(10, "10%");
		vatComboBox.setItemCaption(15, "15%");
		vatComboBox.setItemCaption(21, "21%");
		vatComboBox.setItemCaption(0, "0%");
		vatComboBox.select(10);
		vatComboBox.setNullSelectionAllowed(false);
		
//		item.addStyleName("text-in-form");
//		quantity.addStyleName("text-in-form");
//		unit.addStyleName("text-in-form");
//		priceUnit.addStyleName("text-in-form");
//		vat.addStyleName("text-in-form");
//		total.addStyleName("text-in-form");
//		descriptionText.addStyleName("text-in-form");
		descriptionText.setRows(2);
		descriptionText.setWidth("100%");
		
		hideButton.setWidth("100%");
		item.setWidth("100%");
		quantity.setWidth("100%");
		unit.setWidth("100%");
		priceUnit.setWidth("100%");
		vatComboBox.setWidth("100%");
		total.setWidth("100%");
		descriptionText.setWidth("100%");
		
		insertBlankRow();
				
	}

	
	private void insertBlankRow() {
		int rowAmount = parent.getRows();
		int currentRow = rowAmount; 
		int descriptionRow = currentRow + 1;
		
		parent.insertRow(currentRow);
		parent.insertRow(descriptionRow);
		
		parent.addComponent(hideButton, 0, currentRow);
		parent.addComponent(item, 1, currentRow);
		parent.addComponent(quantity, 2, currentRow);
		parent.addComponent(unit, 3, currentRow);
		parent.addComponent(priceUnit, 4, currentRow);
		parent.addComponent(vatComboBox, 5, currentRow);
		parent.addComponent(total, 6, currentRow);
		parent.addComponent(descriptionText, 0 , descriptionRow, 6, descriptionRow);

	}

	public TextField getQuantity() {
		return quantity;
	}

	public void setQuantity(TextField quantity) {
		this.quantity = quantity;
	}

	public TextField getUnit() {
		return unit;
	}

	public void setUnit(TextField unit) {
		this.unit = unit;
	}

	public TextField getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(TextField priceUnit) {
		this.priceUnit = priceUnit;
	}

	public ComboBox getVatComboBox() {
		return vatComboBox;
	}

	public void setVatComboBox(ComboBox vatComboBox) {
		this.vatComboBox = vatComboBox;
	}

	public TextField getTotal() {
		return total;
	}

	public void setTotal(TextField total) {
		this.total = total;
	}

	public double getTotalCalc() {
		return totalCalc;
	}

	public double getPriceCalc() {
		return priceCalc;
	}

	
	
}

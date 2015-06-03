package com.example.filtertable;

import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class FilterTableLayout extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4543176720574393666L;
	public static final String ID = "Id";
	public static final String SYMBOL = "Symbol";
	public static final String CB = "()";
	public static final String DODAVATEL = "Dodavatel";
	public static final String DATUM1 = "Datum 1";
	public static final String CENA = "Cena";
	public static final String ACTION = "Action";
	public static final String DATUM2 = "Datum2";
	private FilterTable table;
	public FilterTableLayout(){
		createContents();
		
	}

	private void createContents() {
		table = new FilterTable();
		table.setFilterBarVisible(true);
		
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(ID, Component.class, null);
		container.addContainerProperty(CB, Component.class, null);
		container.addContainerProperty(SYMBOL, String.class, null);
		container.addContainerProperty(DODAVATEL, String.class, null);
		container.addContainerProperty(DATUM1, String.class, null);
		container.addContainerProperty(CENA, String.class, null);
		container.addContainerProperty(ACTION, Component.class, null);
		container.addContainerProperty(DATUM2, Component.class, null);
		

		
		table.setContainerDataSource(container);
		
		table.setSizeFull();
		addComponent(table);
		
		insertItem(1);
		insertItem(2);
	}

	private void insertItem(int id) {
		Item i =table.addItem(id);
		i.getItemProperty(ID).setValue(new Button("Test"));
		i.getItemProperty(CB).setValue(new CheckBox());
		i.getItemProperty(SYMBOL).setValue("test");
		i.getItemProperty(DODAVATEL).setValue("test@mail.com");
		i.getItemProperty(DATUM1).setValue("11.01.2015");
		i.getItemProperty(CENA).setValue("12,30");
		i.getItemProperty(ACTION).setValue(new Button("Action"));
		i.getItemProperty(DATUM2).setValue(new DateField());
		
	}
}


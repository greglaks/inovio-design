package com.example.app;

import javax.servlet.annotation.WebServlet;

import com.example.filtertable.FilterTableLayout;
import com.example.invoice.FakturaTest;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("inoviostyle")
@StyleSheet({"//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"})
public class CompUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = CompUI.class, widgetset = "com.example.app.widgetset.CompWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("white");
		layout.setWidth("720px");
		
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
		
		
		
		layout.addComponent(new FakturaTest());
	}

}
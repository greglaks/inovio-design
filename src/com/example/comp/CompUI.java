package com.example.comp;

import javax.servlet.annotation.WebServlet;

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
public class CompUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = CompUI.class)
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
		
		final TextField text = new TextField("Test");
		text.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				text.validate();
			}
		});
		text.setValidationVisible(true);
		text.addValidator(new Validator() {
			
			@Override
			public void validate(Object value) throws InvalidValueException {
				if(value instanceof String){
					if(value.equals("")){
						text.addStyleName("red-border");
						throw new InvalidValueException("Message must not 0");
					}
					else{
						text.removeStyleName("red-border");
					}
				}
			}
		});
		
		layout.addComponent(new FakturaTest());
	}

}
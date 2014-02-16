package com.mmm.findtherythm;

import com.mmm.findtherythm.controller.Controller;
import com.mmm.findtherythm.model.Model;

public class Factory {
	Model model;
	
	
	Controller controlleur;
	private static volatile Factory instance = null;
	private  Factory() {
		 model = new Model();
		controlleur = new Controller(model);
		
	}
	public final static Factory getInstance() {      
        if (Factory.instance == null) {
           synchronized(Factory.class) {
             if (Factory.instance == null) {
               Factory.instance = new Factory();
             }
           }
        }
        return Factory.instance;
    }
	
	public Controller getController()
	{
		return controlleur;
	}
	public Model getModel()
	{
		return model;
	}
	
	public void destroy() {
		model = null;
		controlleur = null;
	}
}

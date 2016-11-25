package ru.simplgroupp.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

public class ErrorTransformCollector implements ErrorListener {

    private List<TransformerException> errors = new ArrayList<TransformerException>();

    public ErrorTransformCollector(){
    	super();
    }
    
    @Override
    public void error(TransformerException exception) throws TransformerException {
        errors.add(exception);
    }

	
	@Override
	public void fatalError(TransformerException arg0)
			throws TransformerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warning(TransformerException arg0) throws TransformerException {
		// TODO Auto-generated method stub
		
	}

    public List<TransformerException> getErrors(){
    	return errors;
    }
    
    public void setErrors(List<TransformerException> errors){
    	this.errors=errors;
    }
}

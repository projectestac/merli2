package edu.xtec.merli.agrega.objects;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;


public abstract class ObjectMerli {
    
    /** Creates a new instance of ObjectMerli */
    
    protected static SOAPFactory soapFactory;
    private int operation;
    
    static
    {
        try
        {
            soapFactory = SOAPFactory.newInstance();
        }
        catch(SOAPException e) 
        {
            e.printStackTrace();
        }
    }
 
    abstract public SOAPElement toXml() throws SOAPException;
	
	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

    public void parseResponse(SOAPMessage sm) {
		// TODO Auto-generated method stub
	}
	
	public SOAPElement getResponse(SOAPMessage sm, String response) throws SOAPException {
		parseResponse(sm);
		return ((Node)((SOAPElement) sm.getSOAPBody().getChildElements(soapFactory.createName(response)).next()).getFirstChild().getFirstChild()).getParentElement();
		//return ((Node)((SOAPElement) sm.getSOAPBody().getChildElements(soapFactory.createName(response)).next()).getFirstChild().getFirstChild()).getParentElement();
	}

}
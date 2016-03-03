package edu.xtec.merli.ws.objects;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;


public abstract class ObjectMerli {
    
    /** Creates a new instance of ObjectMerli */
    
    protected static SOAPFactory soapFactory;
    
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
    
}
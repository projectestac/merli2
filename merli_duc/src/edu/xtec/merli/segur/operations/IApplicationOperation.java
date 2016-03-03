package edu.xtec.merli.segur.operations;

import java.util.ArrayList;

public interface IApplicationOperation {
	public ArrayList getListOperations();
	
	public String getOperationName(int idOp);
	
	public String getName();
}

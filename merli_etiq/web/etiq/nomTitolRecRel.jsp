<%@ page import="edu.xtec.merli.basedades.RecursBD, edu.xtec.merli.RecursMerli" %>
<%	
	
	RecursBD rbd = new RecursBD();
	String sRecRel = request.getParameter("idRecRel");
	String oju="";
	String nomRecurs="Recurs no trobat";
	try
	{
		Integer idRecRel = Integer.valueOf(sRecRel);
		RecursMerli r = rbd.getRecurs(idRecRel.intValue());
		if(!r.getTitle().equals("")) 	nomRecurs = r.getTitle();
		else							oju = "style='color:red;'";
	}
	catch(NumberFormatException e)
	{
		oju="style='color:red;'";
	}
%>
<span <%=oju %>><%=nomRecurs%></span>
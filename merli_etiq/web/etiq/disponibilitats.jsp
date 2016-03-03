<%@ page import= "edu.xtec.merli.basedades.RecursBD" %>
<%	
	RecursBD rbd = new RecursBD();
	String idRec=request.getParameter("idRec");
	String res;
	
	int nbDisps = rbd.nombreDisponibilitatsByIdRecurs(Integer.parseInt(idRec));
	
	if(nbDisps==1)	res = "true";
	else			res = "false";
%>
<idRec><%= idRec %></idRec>
<disp><%= res %></disp>

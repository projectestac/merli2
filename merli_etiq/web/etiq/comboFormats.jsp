<%@ page import="java.util.ArrayList, edu.xtec.merli.basedades.RecursBD" %>
<%	
	
	RecursBD rbd = new RecursBD();
	String esfisic=request.getParameter("esfisic");
	ArrayList ids=new ArrayList();
	ArrayList labels=new ArrayList();
	
	if(esfisic.equals("true"))
	{
	ids.add("fisics");
	labels.add("fisics");
		ids= rbd.getFormatFisicLimitat(RecursBD.IDENTIFIERS);
		labels = rbd.getFormatFisicLimitat(RecursBD.LABELS);
	}
	else if(esfisic.equals("false"))
	{
	ids.add("online");
	labels.add("online");
		ids= rbd.getFormatLimitat(RecursBD.IDENTIFIERS);
		labels = rbd.getFormatLimitat(RecursBD.LABELS);
	}
	else
	{}	
	for(int i=0;i<labels.size();i++)
	{	
		Integer id=(Integer)ids.get(i);
		String label=(String)labels.get(i);
%>
	<option value="<%=id %>"><%=label %></option>
	<%}%>
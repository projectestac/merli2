<%@ page import="java.util.ArrayList,  simpple.xtec.web.util.TipusFitxer, simpple.xtec.web.util.UtilsCercador, java.util.Enumeration, java.util.Hashtable" %>

<%@page import="simpple.xtec.web.analisi.EducacioAnalyzer"%>
<%@page import="java.util.Comparator"%>
<%@page import="simpple.xtec.web.util.XMLCollection"%>

<% String sLang = XMLCollection.getLang(request); %>

<option value="" selected="true">Qualsevol</option>
<%	
	String esfisic=request.getParameter("esfisic");
	Hashtable labelsF=new Hashtable();
	Hashtable labelsO=new Hashtable();
	if (TipusFitxer.isVoid())	TipusFitxer.carregaTipusFitxer();
	
	if(esfisic.equals("fisics") || esfisic.equals("ambdos"))
		labelsF = TipusFitxer.labelsTipusFisics;
	if(esfisic.equals("online") || esfisic.equals("ambdos"))
		labelsO = TipusFitxer.labelsTipusOnline;
	if(esfisic.equals("ambdos"))
	{
	%>
		<optgroup label="<%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.enlinia", sLang)%>">
	<%
	}
	
	final class StringComparator implements Comparator  {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			
			s1 = EducacioAnalyzer.filtra(s1);
			s2 = EducacioAnalyzer.filtra(s2);
			
			return s1.compareTo(s2);
		}
	}
	
	ArrayList orderedLabelsO = new ArrayList(labelsO.values());
	ArrayList orderedKeysO = new ArrayList();
	java.util.Collections.sort(orderedLabelsO, new StringComparator());
	
	for(java.util.Iterator it = orderedLabelsO.iterator(); it.hasNext(); ) {
		String value = (String) it.next();
		// Look for the value's key
		 Enumeration allKeys = labelsO.keys();
		 while (allKeys.hasMoreElements()) {
			String id = (String)allKeys.nextElement();
			String name = (String)labelsO.get(id);
			if (name.equals(value)) {
				orderedKeysO.add(id);
				break;
			}
		 }				
	}
	
	for(int j = 0; j < orderedLabelsO.size(); j++) {
		String name = (String) orderedLabelsO.get(j);
		String id = (String)orderedKeysO.get(j);
	%>
		<option value="<%=id %>"><%=name%></option>
	<%
	}
	if(esfisic.equals("ambdos"))
	{
	%>
		</optgroup>
		<optgroup label="<%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.fisic", sLang)%>">
	<%
	}
	
	ArrayList orderedLabelsF = new ArrayList(labelsF.values());
	ArrayList orderedKeysF = new ArrayList();
	java.util.Collections.sort(orderedLabelsF, new StringComparator());
	
	for(java.util.Iterator it = orderedLabelsF.iterator(); it.hasNext(); ) {
		String value = (String) it.next();
		// Look for the value's key
		 Enumeration allKeys = labelsF.keys();
		 while (allKeys.hasMoreElements()) {
			String id = (String)allKeys.nextElement();
			String name = (String)labelsF.get(id);
			if (name.equals(value)) {
				orderedKeysF.add(id);
				break;
			}
		 }
	}				
	
	for(int j = 0; j < orderedLabelsF.size(); j++) {
		String name = (String) orderedLabelsF.get(j);
		String id = (String)orderedKeysF.get(j);
	%>
		<option value="<%=id %>"><%=name %></option>
	<%
	}

	if(esfisic.equals("ambdos"))
	{
	%>
		</optgroup>
	<%}%>
	
	
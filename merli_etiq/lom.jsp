<?xml version="1.0" encoding="UTF-8"?>
<%@page session="false" contentType="text/xml; charset=UTF-8"%>
<jsp:useBean id="lb" class="edu.xtec.merli.lom.LOMBean" scope="request" />
<%if(!lb.init(request, response)){%><jsp:forward page="error.jsp"/><%}%>

<lom xmlns="http://celebrate.eun.org/xml/ns/celebrateLOM-0_2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rights="http://celebrate.eun.org/xml/ns/rights-0_1" xsi:schemaLocation="http://celebrate.eun.org/xml/ns/celebrateLOM-0_2 http://celebrate.eun.org/xml/schemas/celebrateLOM-0.2.xsd">
 <general>
   <identifier>
     <catalog>CELEBRATE</catalog>
     <entry>MERLI1305</entry>
   </identifier>
   <title>
     <string language="ca"><%=lb.getResource().getTitle()%></string>
   </title>
   <%
   	java.util.Iterator itLang = lb.getResource().getLanguage().iterator();
   	while (itLang.hasNext()){
   %>
   <language><%=itLang.next()%></language>
   <%} %>
   <description>
     <string language="ca"><%=lb.getResource().getDescription()%></string>
   </description>
 </general>
 <lifeCycle>
 <version>
       <string><%=lb.getResource().getVersion()!=null?lb.getResource().getVersion():""%></string>
  </version>         
 	<status>
       <source>LOMv1.0</source>
       <value><%=lb.getResource().getEstat()%> revised</value>
  </status>         
  <contribute>
    <role>
      <source>LOMv1.0</source>
      <value>author</value>
    </role>
    <entity>
	    BEGIN:VCARD
	    VERSION:3.0
	    FN:Jaume Bartrolí Brugués
	    ORG:Departament d'Educació i Universitats
	    END:VCARD
    </entity>
    <date>
      <dateTime>1999-10-05</dateTime>
    </date>
  </contribute>
  <contribute>
    <role>
      <source>LOMv1.0</source>
      <value>publisher</value>
    </role>
    <entity>
	    BEGIN:VCARD
	    VERSION:3.0
	    FN:JClic
	    ORG:Departament d'Educació i Universitats
	    END:VCARD
    </entity>
    <date>
      <dateTime>1999-10-05</dateTime>
    </date>
  </contribute>
 </lifeCycle>
 <metaMetadata>
  <identifier>
    <catalog>CELEBRATE</catalog>
    <entry>MERLI1305</entry>
  </identifier>
   <contribute>
     <role>
       <source>LOMv1.0</source>
       <value>creator</value>
     </role>
     <entity>
		BEGIN:VCARD
		VERSION:3.0
		FN:sarjona
		EMAIL;TYPE=INTERNET:sarjona@xtec.cat
		ORG:Departament d'Educació i Universitats
		END:VCARD
     </entity>
     <date>
       <dateTime>2006-10-10</dateTime>
     </date>
   </contribute>
   <language>ca</language>
 </metaMetadata>
 <technical>
  <format>text/html</format>
  <format>application/java</format>
  <location>http://clic.xtec.net/db/act_ca.jsp?id=1308</location>
  <duration>PT15M</duration>
 </technical>
 <educational>
   <learningResourceType>
     <source>ELNv1.1</source>
     <value>assessment</value>
   </learningResourceType>
   <learningResourceType>
     <source>ELNv1.1</source>
     <value>drill and practice</value>
   </learningResourceType>
   <intendedEndUserRole>
     <source>ELNv1.1</source>
     <value>learner</value>
   </intendedEndUserRole>
   <context>
     <source>ELNv1.1</source>
     <value>compulsory education</value>
   </context>
   <typicalAgeRange>6-16</typicalAgeRange>
 	<difficulty>
       <source>LOMv1.0</source>
       <value>medium</value>
  </difficulty>         
 	<typicalLearningTime>
       <duration>PT15M</duration>
 </typicalLearningTime>         
 </educational>
 <rights>
   <cost>no</cost>
   <copyrightAndOtherRestrictions>
     <source>CreativeCommons</source>
     <value>by-nc-sa</value>
   </copyrightAndOtherRestrictions>
   <description>
     <string language="ca">No es permet un ús comercial de l'obra original ni de les possibles obres derivades, la distribució de les quals s'ha de fer amb una llicència igual a la que regula l'obra original.</string>
   </description>
   <rights:cdr schemaVersion="0.1">
     <rights:permission>
       <rights:action>remoteplay</rights:action>
     </rights:permission>
   </rights:cdr>
 </rights>
 
 <classification>
		<purpose>
			<source>ETB</source>
			<value>discipline</value>
		</purpose>
		<taxonPath>
			<source>
				<string language="ca">Tesaurus de ELR</string>
			</source>
			<taxon>
				<id>161</id>
				<entry>
					<string language="ca">geometria</string>
				</entry>
			</taxon>
			<taxon>
				<id>1155</id>
				<entry>
					<string language="ca">trigonometria</string>
				</entry>
			</taxon>
		</taxonPath>
		<description>
			<string language="ca">Termes seleccionats del tesaurus de ETB</string>
		</description>
	</classification>
	
	<classification>
		<purpose>
			<source>DUC</source>
			<value>educational objective</value>
		</purpose>
		<taxonPath>
			<source>
				<string language="ca">DUC</string>
			</source>
			<taxon>
				<id>2541</id>
				<entry>
					<string language="ca">Moviments en el pla</string>
				</entry>
			</taxon>
			<taxon>
				<id>2156</id>
				<entry>
					<string language="ca">Volums i àrees</string>
				</entry>
			</taxon>
		</taxonPath>
		<description>
			<string language="ca">Continguts del curriculum relacionats</string>
		</description>
	</classification>
 
 </lom>

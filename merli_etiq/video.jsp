<%@page session="false" contentType="text/html; charset=UTF-8"%>
<jsp:useBean id="lb" class="edu.xtec.merli.lom.LOMBean" scope="request" />
<%if(!lb.init(request, response)){%><jsp:forward page="error.jsp"/><%}%>

<HTML>
<HEAD>
<TITLE>MeRLí. Vídeo</TITLE>

<LINK REL=StyleSheet HREF="http://www.xtec.cat/css/estil.css" TYPE="text/css">

</HEAD>

<BODY BACKGROUND="http://www.xtec.cat/videoteca/imatges/fons_int2.gif" BGCOLOR="#FFFFFF" ALINK="#FFD700" MARGINWIDTH="0" MARGINHEIGHT="0" TOPMARGIN="0" LEFTMARGIN="0">

<!-- --------- -->
<!-- capcalera -->
<!-- --------- -->

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" MARGINWIDTH="0" MARGINHEIGHT="0" TOPMARGIN="0" LEFTMARGIN="0" WIDTH=610>
<TR>
  <TD WIDTH=610 COLSPAN=2>
  <A HREF="http://www.xtec.cat"><IMG SRC="http://www.xtec.cat/videoteca/imatges/xtec_int.gif" WIDTH=610 HEIGHT=18 ALT="XTEC" BORDER=0 USEMAP="#tornar"></A><BR>
  <IMG SRC="http://www.xtec.cat/videoteca/imatges/quadres_int.gif" WIDTH=610 HEIGHT=15 ALT="" BORDER=0><BR>
  </TD>
</TR>
</TR>

</TABLE>

<!-- ---------- -->
<!-- /capcalera -->
<!-- ---------- -->

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" MARGINWIDTH="0" MARGINHEIGHT="0" TOPMARGIN="0" LEFTMARGIN="0" WIDTH=610>
<TR>
  <TD WIDTH=10>
    &#160;
  </TD>

  <TD VALIGN="TOP" WIDTH=>

  <!-- continguts columnes -->
  <BR><BR>

<%if (lb.getResource()!=null){ %>
  <TABLE width=355>
  <TR>
    <TD WIDTH=5></TD>
    <TD>

 
       <IMG SRC='http://www.xtec.cat/videoteca/imatges/e_punt.gif' WIDTH='9' HEIGHT='10' ALT='' BORDER='0'><B><%=lb.getResource().getTitle()%></B><BR><br>
 <%=lb.getResource().getDescription()%><BR><br>

  <!-- /continguts columnes -->
  </TD>
</TR>
</TABLE>
<%} %>

<center><object ID=video1 CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT=180 WIDTH=240><param NAME="controls" VALUE="ImageWindow"><param NAME="console" VALUE="Clip1"><param NAME="autostart" VALUE="true"><param NAME="src" VALUE="<%=request.getParameter("video")%>"><embed SRC="<%=request.getParameter("video")%>" type="audio/x-pn-realaudio-plugin" CONSOLE="Clip1" CONTROLS="ImageWindow" HEIGHT=180 WIDTH=240 AUTOSTART=true></object><br><object ID=video1 CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT=60 WIDTH=240><param NAME="controls" VALUE="ControlPanel,StatusBar"><param NAME="console" VALUE="Clip1"><embed type="audio/x-pn-realaudio-plugin" CONSOLE="Clip1" CONTROLS="ControlPanel,StatusBar" HEIGHT=60 WIDTH=240 AUTOSTART=true></object></center>

</BODY>
</HTML>
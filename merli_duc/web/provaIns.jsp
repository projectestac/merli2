<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ include file="/web/comu/taglibs.jsp"%>
<html:html locale="true">
    <meta http-equiv="content-type" content="text/html;charset=ISO-8859-1"/>
    <title><bean:message key="application.title"/></title>
    <head>
        <%@ include file="/web/comu/header.jsp"%>
    </head>
    <jsp:useBean id="semanticnet" scope="session" class="edu.xtec.merli.semanticnet.SemanticInterface"/>
    <jsp:useBean id="semanticnet" scope="session" class="edu.xtec.gescurriculum.gestorcurriculum.NodeForm"/>

    <body onload="init();">
        <html:errors property="nodeError"/>
        <html:errors property="addnode"/>
        <html:errors property="delnode"/>
        <html:errors property="setnode"/>
        <html:errors property="swapnode"/>
        <div class="title"><bean:message key="application.title"/><span onclick="modeC()">C</span> <span onclick="modeD()">D</span></div>
        <html:form action="gesNodes.do">
            <div class="curriculum" id="divlevel">
                <div class="title"><bean:message key="application.level"/></div>
                <div class="content"><html:errors property="idlevel"/>
                    <PHTM:ul id="level" name="NodeForm" property="idlevel"></PHTM:ul>
                    </div>
                    <div class="operacions">
                        <span onclick="amunt('level')"> T </span>
                        <span onclick="avall('level')">V</span>
                        <span onclick="insert('level');">+</span>
                        <span onclick="delNode('level');">-</span>
                        <span id="insfill" onclick="insertFill('level');">F</span>
                    </div>
                </div>
                <div class="curriculum" id="divarea">
                    <div class="title"><bean:message key="application.area"/></div>
                <div class="content">
                    <PHTM:ul id="area" name="NodeForm" property="idarea"  ></PHTM:ul>
                    </div>	
                    <div class="operacions">
                        <span onclick="amunt('area')"> T </span>
                        <span onclick="avall('area')">V</span>
                        <span onclick="insert('area');">+</span>
                        <span onclick="delNode('area');">-</span>
                    </div>
                </div>
                <div class="curriculum" id="divcontent">
                    <div class="title"><bean:message key="application.content"/></div>
                <div class="pestanya">
                    <table>
                        <tr>
                            <td id="pos1" onclick="pestanya('contCC', 'contCP', 'contCA')"><bean:message key="application.content.cc"/></td>
                            <td id="pos2" onclick="pestanya('contCP', 'contCC', 'contCA')"><bean:message key="application.content.cp"/></td>
                            <td id="pos3" onclick="pestanya('contCA', 'contCP', 'contCC')"><bean:message key="application.content.ca"/></td>
                        </tr>
                    </table>
                    <PHTM:ul id="content" name="NodeForm" property="idcontent"  ></PHTM:ul>
                    </div>
                    <div class="operacions">
                        <span onclick="amunt('content')"> T </span>
                        <span onclick="avall('content')">V</span>
                        <span onclick="insert('content')">+</span>
                        <span onclick="delNode('content');">-</span>
                    </div>
                </div>
                <div class="curriculum" id="divobjective">
                    <div class="title"><bean:message key="application.objective"/></div>
                <div class="pestanya">
                    <table>
                        <tr>
                            <td id="pos1" onclick="pestanya('objOP', 'objOT')"><bean:message key="application.objective.principal"/></td>
                            <td id="pos2" onclick="pestanya('objOT', 'objOP')"><bean:message key="application.objective.terminal"/></td>
                        </tr>
                    </table>
                    <PHTM:ul id="objective" name="NodeForm" property="idobjective"  ></PHTM:ul>			
                    </div>
                    <div class="operacions">
                        <span onclick="amunt('objective')"> T </span>
                        <span onclick="avall('objective')">V</span>
                        <span onclick="insert('objective');">+</span>
                        <span onclick="delNode('objective');">-</span>
                    </div>
                </div>

                <div class="formulari">
                    <div class="title"><bean:message key="application.nodeform.title"/></div>
                <div class="content">		
                    <html:hidden property="idLevel" ></html:hidden>
                    <html:hidden property="idArea" ></html:hidden>
                    <html:hidden property="idContent" ></html:hidden>
                    <html:hidden property="idObjective" ></html:hidden>
                    <html:hidden property="idNode" ></html:hidden>
                    <html:hidden property="operacio" value="setnode"></html:hidden>
                        <div>
                        <bean:message key="application.nodeform.term"/>:<html:text property="term" ></html:text>
                        </div>
                        <div id="formNodeType">
                        <bean:message key="application.nodeform.nodeType"/>: <html:select onchange="javascript:chtype(this);" property="nodeType" >
                            <html:option value="level" ><bean:message key="application.level"/></html:option>
                            <html:option value="area" ><bean:message key="application.area"/></html:option>
                            <html:option value="content" ><bean:message key="application.content"/></html:option>
                            <html:option value="objective" ><bean:message key="application.objective"/></html:option>
                        </html:select>
                    </div>
                    <div>
                        <bean:message key="application.nodeform.description"/>:<html:textarea property="description"  rows="8" cols="20" ></html:textarea>
                        </div>
                        <div id="formcontent">
                        <bean:message key="application.nodeform.category"/>:<br/>
                        <PHTM:Radio id="content" name="category"/>
                        <br/>
                    </div>
                    <div id="formobjective">
                        <bean:message key="application.nodeform.category"/>:<br/>
                        <PHTM:Radio id="objective" name="category"/>
                    </div>
                    <div id="formlevel">
                        <bean:message key="application.nodeform.category"/>: <br/>
                        <PHTM:Radio id="level" name="category"/>
                    </div>

                    <div><span onclick="swapDisplay('formObservacions')"><bean:message key="application.nodeform.observations"/></span>
                        <div id="formObservacions">
                            <html:textarea property="note"  rows="8" cols="20" ></html:textarea>
                            </div>
                        </div>
                        <div><span onclick="swapDisplay('formReferencia')"><bean:message key="application.nodeform.references"/></span>
                        <div id="formReferencia">
                            <html:textarea property="references"  rows="8" cols="20" ></html:textarea>
                            </div>
                        </div>
                        <script type="text/javascript">
                            setCapes('<jsp:getProperty name="NodeForm" property="contentcategory"/>', '<jsp:getProperty name="NodeForm" property="objectivecategory"/>');
                            setCategory('<jsp:getProperty name="NodeForm" property="category"/>');
                    </script>
                    <html:reset><bean:message key="application.reset"/></html:reset>
                    <html:submit><bean:message key="application.submit"/></html:submit>
                    </div>
                </div>
        </html:form>
    </body>
</html:html>
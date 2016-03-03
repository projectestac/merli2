package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class gesCur_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(6);
    _jspx_dependants.add("/web/comu/taglibs.jsp");
    _jspx_dependants.add("/web/comu/header.jsp");
    _jspx_dependants.add("/WEB-INF/PHTM.tld");
    _jspx_dependants.add("/WEB-INF/struts-html-el.tld");
    _jspx_dependants.add("/WEB-INF/struts-logic-el.tld");
    _jspx_dependants.add("/WEB-INF/struts-bean-el.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fhtml_0026_005flocale;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fform_0026_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fselect_0026_005fproperty_005fonchange;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005freset_0026_005fstyleClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fsubmit_0026_005fstyleClass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fhtml_005fhtml_0026_005flocale = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005fform_0026_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005fselect_0026_005fproperty_005fonchange = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005freset_0026_005fstyleClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005fsubmit_0026_005fstyleClass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fhtml_005fhtml_0026_005flocale.release();
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fhtml_005fform_0026_005faction.release();
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.release();
    _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.release();
    _005fjspx_005ftagPool_005fhtml_005fselect_0026_005fproperty_005fonchange.release();
    _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.release();
    _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.release();
    _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.release();
    _005fjspx_005ftagPool_005fhtml_005freset_0026_005fstyleClass.release();
    _005fjspx_005ftagPool_005fhtml_005fsubmit_0026_005fstyleClass.release();
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.release();
    _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">\r\n");
      out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
      //  html:html
      org.apache.strutsel.taglib.html.ELHtmlTag _jspx_th_html_005fhtml_005f0 = (org.apache.strutsel.taglib.html.ELHtmlTag) _005fjspx_005ftagPool_005fhtml_005fhtml_0026_005flocale.get(org.apache.strutsel.taglib.html.ELHtmlTag.class);
      _jspx_th_html_005fhtml_005f0.setPageContext(_jspx_page_context);
      _jspx_th_html_005fhtml_005f0.setParent(null);
      // /gesCur.jsp(4,0) name = locale type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_html_005fhtml_005f0.setLocaleExpr("true");
      int _jspx_eval_html_005fhtml_005f0 = _jspx_th_html_005fhtml_005f0.doStartTag();
      if (_jspx_eval_html_005fhtml_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<meta http-equiv=\"content-type\" content=\"text/html;charset=ISO-8859-15\"/>\r\n");
          out.write("<link rel=\"shortcut icon\" href=\"web/images/favicon_duc.ico\" type=\"image/x-icon\">\n");
          out.write("<link rel=\"icon\" href=\"web/images/favicon_duc.ico\" type=\"image/x-icon\">\n");
          out.write("\t<title>");
          if (_jspx_meth_bean_005fmessage_005f0(_jspx_th_html_005fhtml_005f0, _jspx_page_context))
            return;
          out.write("</title>\r\n");
          out.write("<head>\r\n");
          out.write("<link rel=\"shortcut icon\" href=\"web/images/favicon_duc.ico\" type=\"image/x-icon\">\n");
          out.write("<link rel=\"icon\" href=\"web/images/favicon_duc.ico\" type=\"image/x-icon\">\n");
          out.write("\n");
          out.write("<LINK REL=\"stylesheet\" TYPE=\"text/css\" HREF=\"web/css/duc2.css\" />\r\n");
          out.write("<LINK REL=\"stylesheet\" TYPE=\"text/css\" HREF=\"web/css/merli.css\" />\r\n");
          out.write("\n");
          out.write("<script type=\"text/javascript\" src=\"web/JS/duc.js\"></script>\r\n");
          out.write("<script type=\"text/javascript\" src=\"web/JS/XML_Script_1.js\"></script>");
          out.write("\r\n");
          out.write("</head>\r\n");
          edu.xtec.merli.semanticnet.SemanticInterface semanticnet = null;
          synchronized (session) {
            semanticnet = (edu.xtec.merli.semanticnet.SemanticInterface) _jspx_page_context.getAttribute("semanticnet", PageContext.SESSION_SCOPE);
            if (semanticnet == null){
              semanticnet = new edu.xtec.merli.semanticnet.SemanticInterface();
              _jspx_page_context.setAttribute("semanticnet", semanticnet, PageContext.SESSION_SCOPE);
            }
          }
          out.write('\r');
          out.write('\n');
          edu.xtec.gescurriculum.gestorcurriculum.NodeForm NodeForm = null;
          synchronized (request) {
            NodeForm = (edu.xtec.gescurriculum.gestorcurriculum.NodeForm) _jspx_page_context.getAttribute("NodeForm", PageContext.REQUEST_SCOPE);
            if (NodeForm == null){
              NodeForm = new edu.xtec.gescurriculum.gestorcurriculum.NodeForm();
              _jspx_page_context.setAttribute("NodeForm", NodeForm, PageContext.REQUEST_SCOPE);
            }
          }
          out.write("\r\n");
          out.write("\r\n");
          out.write("<body onload=\"init();\">\r\n");
          //  html:form
          org.apache.strutsel.taglib.html.ELFormTag _jspx_th_html_005fform_005f0 = (org.apache.strutsel.taglib.html.ELFormTag) _005fjspx_005ftagPool_005fhtml_005fform_0026_005faction.get(org.apache.strutsel.taglib.html.ELFormTag.class);
          _jspx_th_html_005fform_005f0.setPageContext(_jspx_page_context);
          _jspx_th_html_005fform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fhtml_005f0);
          // /gesCur.jsp(16,0) name = action type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_html_005fform_005f0.setActionExpr("gesNodes.do");
          int _jspx_eval_html_005fform_005f0 = _jspx_th_html_005fform_005f0.doStartTag();
          if (_jspx_eval_html_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<div class=\"esquerra\">\r\n");
              out.write("\t<div id=\"logo\">\r\n");
              out.write("\t\t<img id=\"img\" src=\"web/images/logo.png\" alt=\"DUC\" title=\"DUC\"/>\r\n");
              out.write("\t</div>\r\n");
              out.write("\t<div class=\"formulari\">\r\n");
              out.write("\t\t<div class=\"title\">");
              if (_jspx_meth_bean_005fmessage_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</div>\r\n");
              out.write("\t\t<div class=\"content\">\t\t\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f3(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f4(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f5(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f6(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f7(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f8(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fhidden_005f9(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t<div>\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(' ');
              if (_jspx_meth_bean_005fmessage_005f3(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(':');
              if (_jspx_meth_html_005ftext_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f4(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(' ');
              if (_jspx_meth_bean_005fmessage_005f5(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(':');
              if (_jspx_meth_html_005ftext_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f6(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(' ');
              if (_jspx_meth_bean_005fmessage_005f7(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(':');
              if (_jspx_meth_html_005ftext_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f8(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(' ');
              if (_jspx_meth_bean_005fmessage_005f9(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(':');
              if (_jspx_meth_html_005ftext_005f3(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t\t<br>\r\n");
              out.write("\t\t\t\t<div id=\"formNodeType\">\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f10(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(':');
              out.write(' ');
              if (_jspx_meth_html_005fselect_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t<div>\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f15(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(":<br>\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005ftextarea_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t<div id=\"formcontent\">\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f16(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(":<br/>\r\n");
              out.write("\t\t\t\t\t\t");
              if (_jspx_meth_PHTM_005fRadio_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t<br/>\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t<div id=\"formobjective\">\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f17(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(":<br/>\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_PHTM_005fRadio_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t<br/>\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t<div id=\"formlevel\">\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_bean_005fmessage_005f18(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write(": <br/>\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_PHTM_005fRadio_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t<br/>\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t<div class=\"opcional\"><div class=\"option\"><span class=\"left\">");
              if (_jspx_meth_bean_005fmessage_005f19(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</span><span class=\"right\"><a href=\"#\" onclick=\"navigateTo(0);swapDisplay('formThesaure')\">+</a></span></div></div>\t\t\t\r\n");
              out.write("\t\t\t\t<div class=\"opcional\"><div class=\"option\"><span class=\"left\">");
              if (_jspx_meth_bean_005fmessage_005f20(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</span><span class=\"right\"><a href=\"#\" onclick=\"swapDisplay('formObservacions')\">+</a></span></div>\r\n");
              out.write("\t\t\t\t\t<div id=\"formObservacions\">\r\n");
              out.write("\t\t\t\t\t");
              out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((edu.xtec.gescurriculum.gestorcurriculum.NodeForm)_jspx_page_context.findAttribute("NodeForm")).getLastNotes())));
              out.write("\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_html_005ftextarea_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t<div class=\"opcional\"><div class=\"option\"<span class=\"left\">");
              if (_jspx_meth_bean_005fmessage_005f21(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</span><span class=\"right\"><a href=\"#\" onclick=\"swapDisplay('formReferencia')\">+</a></span></div>\r\n");
              out.write("\t\t\t\t\t<div id=\"formReferencia\">\r\n");
              out.write("\t\t\t\t\t");
              if (_jspx_meth_html_005ftextarea_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005freset_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005fsubmit_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t\t");
              if (_jspx_meth_html_005ferrors_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t");
              if (_jspx_meth_html_005ferrors_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t</div>\r\n");
              out.write("</div>\r\n");
              out.write("<div id=\"dreta\">\r\n");
              out.write("\t<div id=\"header\">\r\n");
              out.write("\t\t<div id=\"titleHeader\">\r\n");
              out.write("\t\t\t<div id=\"entorns\">\r\n");
              out.write("\t\t\t</div>\r\n");
              out.write("\t\t\t<div class=\"inferior\">\r\n");
              out.write("\t\t\t\t<div class=\"esquerra2\" id=\"modes\">\r\n");
              out.write("\t\t\t\t\t<a href=\"login.jsp\" title=\"surt\">");
              if (_jspx_meth_bean_005fmessage_005f24(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</a>\t\t\t\t\t\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t\t<div class=\"dreta2\" id=\"username\">\n");
              out.write("\t\t\t\t<a href=\"#\">\r\n");
              out.write("\t\t\t\t\t<!-- a href=\"usuaris.do\" title=\"Editar usuari\"-->");

	if (request.getSession() != null && request.getSession().getAttribute("user") != null){
		out.println(((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser());	
	}else{
		
              out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((edu.xtec.gescurriculum.gestorcurriculum.NodeForm)_jspx_page_context.findAttribute("NodeForm")).getUser())));

	}

              out.write("</a>\r\n");
              out.write("\t\t\t\t</div>\r\n");
              out.write("\t\t\t</div>\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t</div>\r\n");
              out.write("\t<div class=\"curriculum\" id=\"divlevel\">\r\n");
              out.write("\t\t<div class=\"title\">");
              if (_jspx_meth_bean_005fmessage_005f25(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</div>\r\n");
              out.write("\t\t<div class=\"content\">\r\n");
              out.write("\t\t\t");
              if (_jspx_meth_PHTM_005ful_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t\t<div class=\"operacions\">\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"levelAmunt\" onclick=\"amunt(this,'level')\"><img class=\"operacio\" src=\"web/images/amunt.png\" alt=\"Amunt\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"levelAvall\" onclick=\"avall(this,'level')\"><img class=\"operacio\" src=\"web/images/avall.png\" alt=\"Avall\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"levelAdd\" onclick=\"insert(this,'level');\"><img class=\"operacio\" src=\"web/images/add.png\" alt=\"Add\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"levelDel\" onclick=\"delNode(this,'level');\"><img class=\"operacio\" src=\"web/images/del.png\" alt=\"Del\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"levelFill\" onclick=\"insertFill(this,'level');\"><img class=\"operacio\" src=\"web/images/fill.png\" alt=\"Fill\"/></a>\r\n");
              out.write("\t\t\t");

			if (request.getParameter("nodeType") != null && request.getParameter("nodeType").compareTo("level")==0){
				
              if (_jspx_meth_html_005ferrors_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005ferrors_005f3(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;

			}
			
              out.write("\t\t\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t</div>\r\n");
              out.write("\t<div class=\"curriculum\" id=\"divarea\">\r\n");
              out.write("\t\t<div class=\"title\">");
              if (_jspx_meth_bean_005fmessage_005f26(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</div>\r\n");
              out.write("\t\t<div class=\"content\">\r\n");
              out.write("\t\t\t");
              if (_jspx_meth_PHTM_005ful_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t</div>\t\r\n");
              out.write("\t\t<div class=\"operacions\">\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"areaAmunt\" onclick=\"amunt(this,'area')\"><img class=\"operacio\" src=\"web/images/amunt.png\" alt=\"Amunt\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"areaAvall\" onclick=\"avall(this,'area')\"><img class=\"operacio\" src=\"web/images/avall.png\" alt=\"Avall\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"areaAdd\" onclick=\"insert(this,'area');\"><img class=\"operacio\" src=\"web/images/add.png\" alt=\"Add\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"areaDel\" onclick=\"delNode(this,'area');\"><img class=\"operacio\" src=\"web/images/del.png\" alt=\"Del\"/></a>\r\n");
              out.write("\t\t\t");

			if (request.getParameter("nodeType") != null && request.getParameter("nodeType").compareTo("area")==0){
				
              if (_jspx_meth_html_005ferrors_005f4(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005ferrors_005f5(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;

			}
			
              out.write("\t\t\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t</div>\r\n");
              out.write("\t<div class=\"curriculum\" id=\"divcontent\">\r\n");
              out.write("\t\t<div class=\"title\">");
              if (_jspx_meth_bean_005fmessage_005f27(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</div>\r\n");
              out.write("\t\t<div class=\"pestanya\">\r\n");
              out.write("\t\t\t<table>\r\n");
              out.write("\t\t\t\t<tr> <!-- onclick=\"pestanya('contCC','contCP', 'contCA')\"onclick=\"pestanya('contCP','contCC', 'contCA')\"onclick=\"pestanya('contCA','contCP', 'contCC')\" -->\r\n");
              out.write("\t\t\t\t\t<td id=\"pos1\">");
              if (_jspx_meth_bean_005fmessage_005f28(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</td>\r\n");
              out.write("\t\t\t\t\t<td id=\"pos2\">");
              if (_jspx_meth_bean_005fmessage_005f29(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</td>\r\n");
              out.write("\t\t\t\t\t<td id=\"pos3\">");
              if (_jspx_meth_bean_005fmessage_005f30(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</td>\r\n");
              out.write("\t\t\t\t</tr>\r\n");
              out.write("\t\t\t</table>\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t\t<div class=\"content\">\r\n");
              out.write("\t\t\t");
              if (_jspx_meth_PHTM_005ful_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t\t<div class=\"operacions\">\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"contentAmunt\" onclick=\"amunt(this,'content')\"><img class=\"operacio\" src=\"web/images/amunt.png\" alt=\"Amunt\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"contentAvall\" onclick=\"avall(this,'content')\"><img class=\"operacio\" src=\"web/images/avall.png\" alt=\"Avall\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"contentAdd\" onclick=\"insert(this,'content')\"><img class=\"operacio\" src=\"web/images/add.png\" alt=\"Add\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"contentDel\" onclick=\"delNode(this,'content');\"><img class=\"operacio\" src=\"web/images/del.png\" alt=\"Del\"/></a>\r\n");
              out.write("\t\t\t<a href=\"#\" id=\"contentFill\" onclick=\"insertFill(this,'content');\"><img class=\"operacio\" src=\"web/images/fill.png\" alt=\"Fill\"/></a>\r\n");
              out.write("\t\t\t");

			if (request.getParameter("nodeType") != null && request.getParameter("nodeType").compareTo("content")==0){
				
              if (_jspx_meth_html_005ferrors_005f6(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t\t");
              if (_jspx_meth_html_005ferrors_005f7(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;

			}
			
              out.write("\t\t\r\n");
              out.write("\t\t</div>\r\n");
              out.write("\t</div>\r\n");
              out.write("</div>\r\n");
              out.write("\t<script type=\"text/javascript\">\r\n");
              out.write("\t setCapes('");
              out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((edu.xtec.gescurriculum.gestorcurriculum.NodeForm)_jspx_page_context.findAttribute("NodeForm")).getContentcategory())));
              out.write('\'');
              out.write(',');
              out.write('\'');
              out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((edu.xtec.gescurriculum.gestorcurriculum.NodeForm)_jspx_page_context.findAttribute("NodeForm")).getObjectivecategory())));
              out.write("');\r\n");
              out.write("\t setCategory('");
              out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((edu.xtec.gescurriculum.gestorcurriculum.NodeForm)_jspx_page_context.findAttribute("NodeForm")).getCategory())));
              out.write("');\r\n");
              out.write("\t var zone=\"");
              out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((edu.xtec.gescurriculum.gestorcurriculum.NodeForm)_jspx_page_context.findAttribute("NodeForm")).getNodeType())));
              out.write("\";\r\n");
              out.write("\t </script>\r\n");
              out.write("\t<div id=\"formThesaure\">\r\n");
              out.write("\t");
              if (_jspx_meth_PHTM_005ful_005f3(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t</div>\r\n");
              out.write("\t<div id=\"permisos\">");
              if (_jspx_meth_html_005ferrors_005f8(_jspx_th_html_005fform_005f0, _jspx_page_context))
                return;
              out.write("</div>\r\n");
              int evalDoAfterBody = _jspx_th_html_005fform_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_html_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005fform_0026_005faction.reuse(_jspx_th_html_005fform_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fhtml_005fform_0026_005faction.reuse(_jspx_th_html_005fform_005f0);
          out.write("\r\n");
          out.write("</body>\r\n");
          int evalDoAfterBody = _jspx_th_html_005fhtml_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_html_005fhtml_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fhtml_005fhtml_0026_005flocale.reuse(_jspx_th_html_005fhtml_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fhtml_005fhtml_0026_005flocale.reuse(_jspx_th_html_005fhtml_005f0);
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_bean_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fhtml_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f0 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fhtml_005f0);
    // /gesCur.jsp(8,8) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f0.setKeyExpr("application.title");
    int _jspx_eval_bean_005fmessage_005f0 = _jspx_th_bean_005fmessage_005f0.doStartTag();
    if (_jspx_th_bean_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f1 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(22,21) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f1.setKeyExpr("application.nodeform.title");
    int _jspx_eval_bean_005fmessage_005f1 = _jspx_th_bean_005fmessage_005f1.doStartTag();
    if (_jspx_th_bean_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f0 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(24,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f0.setPropertyExpr("idLevel");
    int _jspx_eval_html_005fhidden_005f0 = _jspx_th_html_005fhidden_005f0.doStartTag();
    if (_jspx_th_html_005fhidden_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f0);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f1 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f1.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(25,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f1.setPropertyExpr("idArea");
    int _jspx_eval_html_005fhidden_005f1 = _jspx_th_html_005fhidden_005f1.doStartTag();
    if (_jspx_th_html_005fhidden_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f1);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f2 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f2.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(26,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f2.setPropertyExpr("idContent");
    int _jspx_eval_html_005fhidden_005f2 = _jspx_th_html_005fhidden_005f2.doStartTag();
    if (_jspx_th_html_005fhidden_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f2);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f3 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f3.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(27,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f3.setPropertyExpr("idObjective");
    int _jspx_eval_html_005fhidden_005f3 = _jspx_th_html_005fhidden_005f3.doStartTag();
    if (_jspx_th_html_005fhidden_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f3);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f4 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f4.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(28,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f4.setPropertyExpr("idNode");
    int _jspx_eval_html_005fhidden_005f4 = _jspx_th_html_005fhidden_005f4.doStartTag();
    if (_jspx_th_html_005fhidden_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f4);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f5 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f5.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(29,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f5.setPropertyExpr("idKey");
    int _jspx_eval_html_005fhidden_005f5 = _jspx_th_html_005fhidden_005f5.doStartTag();
    if (_jspx_th_html_005fhidden_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f5);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f6 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f6.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(30,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f6.setPropertyExpr("selecPath");
    int _jspx_eval_html_005fhidden_005f6 = _jspx_th_html_005fhidden_005f6.doStartTag();
    if (_jspx_th_html_005fhidden_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f6);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f7 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f7.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(31,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f7.setPropertyExpr("navPath");
    int _jspx_eval_html_005fhidden_005f7 = _jspx_th_html_005fhidden_005f7.doStartTag();
    if (_jspx_th_html_005fhidden_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f7);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f8 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f8.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(32,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f8.setPropertyExpr("operacio");
    int _jspx_eval_html_005fhidden_005f8 = _jspx_th_html_005fhidden_005f8.doStartTag();
    if (_jspx_th_html_005fhidden_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f8);
    return false;
  }

  private boolean _jspx_meth_html_005fhidden_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:hidden
    org.apache.strutsel.taglib.html.ELHiddenTag _jspx_th_html_005fhidden_005f9 = (org.apache.strutsel.taglib.html.ELHiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELHiddenTag.class);
    _jspx_th_html_005fhidden_005f9.setPageContext(_jspx_page_context);
    _jspx_th_html_005fhidden_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(33,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fhidden_005f9.setPropertyExpr("entornOperacio");
    int _jspx_eval_html_005fhidden_005f9 = _jspx_th_html_005fhidden_005f9.doStartTag();
    if (_jspx_th_html_005fhidden_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fhidden_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f9);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f2 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(35,4) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f2.setKeyExpr("application.nodeform.term");
    int _jspx_eval_bean_005fmessage_005f2 = _jspx_th_bean_005fmessage_005f2.doStartTag();
    if (_jspx_th_bean_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f3 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(35,52) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f3.setKeyExpr("application.nodeform.term.ca");
    int _jspx_eval_bean_005fmessage_005f3 = _jspx_th_bean_005fmessage_005f3.doStartTag();
    if (_jspx_th_bean_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_html_005ftext_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.strutsel.taglib.html.ELTextTag _jspx_th_html_005ftext_005f0 = (org.apache.strutsel.taglib.html.ELTextTag) _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELTextTag.class);
    _jspx_th_html_005ftext_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005ftext_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(35,103) name = size type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f0.setSizeExpr("55");
    // /gesCur.jsp(35,103) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f0.setPropertyExpr("term");
    int _jspx_eval_html_005ftext_005f0 = _jspx_th_html_005ftext_005f0.doStartTag();
    if (_jspx_th_html_005ftext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f0);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f4 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(36,4) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f4.setKeyExpr("application.nodeform.term");
    int _jspx_eval_bean_005fmessage_005f4 = _jspx_th_bean_005fmessage_005f4.doStartTag();
    if (_jspx_th_bean_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f5 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(36,52) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f5.setKeyExpr("application.nodeform.term.oc");
    int _jspx_eval_bean_005fmessage_005f5 = _jspx_th_bean_005fmessage_005f5.doStartTag();
    if (_jspx_th_bean_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_html_005ftext_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.strutsel.taglib.html.ELTextTag _jspx_th_html_005ftext_005f1 = (org.apache.strutsel.taglib.html.ELTextTag) _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELTextTag.class);
    _jspx_th_html_005ftext_005f1.setPageContext(_jspx_page_context);
    _jspx_th_html_005ftext_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(36,103) name = size type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f1.setSizeExpr("55");
    // /gesCur.jsp(36,103) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f1.setPropertyExpr("termOc");
    int _jspx_eval_html_005ftext_005f1 = _jspx_th_html_005ftext_005f1.doStartTag();
    if (_jspx_th_html_005ftext_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f1);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f6 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(37,4) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f6.setKeyExpr("application.nodeform.term");
    int _jspx_eval_bean_005fmessage_005f6 = _jspx_th_bean_005fmessage_005f6.doStartTag();
    if (_jspx_th_bean_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f7 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(37,52) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f7.setKeyExpr("application.nodeform.term.es");
    int _jspx_eval_bean_005fmessage_005f7 = _jspx_th_bean_005fmessage_005f7.doStartTag();
    if (_jspx_th_bean_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_html_005ftext_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.strutsel.taglib.html.ELTextTag _jspx_th_html_005ftext_005f2 = (org.apache.strutsel.taglib.html.ELTextTag) _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELTextTag.class);
    _jspx_th_html_005ftext_005f2.setPageContext(_jspx_page_context);
    _jspx_th_html_005ftext_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(37,103) name = size type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f2.setSizeExpr("55");
    // /gesCur.jsp(37,103) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f2.setPropertyExpr("termEs");
    int _jspx_eval_html_005ftext_005f2 = _jspx_th_html_005ftext_005f2.doStartTag();
    if (_jspx_th_html_005ftext_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f2);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f8 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(38,4) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f8.setKeyExpr("application.nodeform.term");
    int _jspx_eval_bean_005fmessage_005f8 = _jspx_th_bean_005fmessage_005f8.doStartTag();
    if (_jspx_th_bean_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f9 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(38,52) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f9.setKeyExpr("application.nodeform.term.en");
    int _jspx_eval_bean_005fmessage_005f9 = _jspx_th_bean_005fmessage_005f9.doStartTag();
    if (_jspx_th_bean_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_html_005ftext_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:text
    org.apache.strutsel.taglib.html.ELTextTag _jspx_th_html_005ftext_005f3 = (org.apache.strutsel.taglib.html.ELTextTag) _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELTextTag.class);
    _jspx_th_html_005ftext_005f3.setPageContext(_jspx_page_context);
    _jspx_th_html_005ftext_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(38,103) name = size type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f3.setSizeExpr("55");
    // /gesCur.jsp(38,103) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftext_005f3.setPropertyExpr("termEn");
    int _jspx_eval_html_005ftext_005f3 = _jspx_th_html_005ftext_005f3.doStartTag();
    if (_jspx_th_html_005ftext_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f3);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f10 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(42,5) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f10.setKeyExpr("application.nodeform.nodeType");
    int _jspx_eval_bean_005fmessage_005f10 = _jspx_th_bean_005fmessage_005f10.doStartTag();
    if (_jspx_th_bean_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_html_005fselect_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:select
    org.apache.strutsel.taglib.html.ELSelectTag _jspx_th_html_005fselect_005f0 = (org.apache.strutsel.taglib.html.ELSelectTag) _005fjspx_005ftagPool_005fhtml_005fselect_0026_005fproperty_005fonchange.get(org.apache.strutsel.taglib.html.ELSelectTag.class);
    _jspx_th_html_005fselect_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005fselect_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(42,58) name = onchange type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fselect_005f0.setOnchangeExpr("javascript:chtype(this);");
    // /gesCur.jsp(42,58) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fselect_005f0.setPropertyExpr("nodeType");
    int _jspx_eval_html_005fselect_005f0 = _jspx_th_html_005fselect_005f0.doStartTag();
    if (_jspx_eval_html_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_005fselect_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_005fselect_005f0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t\t\t");
        if (_jspx_meth_html_005foption_005f0(_jspx_th_html_005fselect_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t\t\t");
        if (_jspx_meth_html_005foption_005f1(_jspx_th_html_005fselect_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t\t\t");
        if (_jspx_meth_html_005foption_005f2(_jspx_th_html_005fselect_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t<!-- \t\t");
        if (_jspx_meth_html_005foption_005f3(_jspx_th_html_005fselect_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t -->\t");
        int evalDoAfterBody = _jspx_th_html_005fselect_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_005fselect_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_html_005fselect_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fselect_0026_005fproperty_005fonchange.reuse(_jspx_th_html_005fselect_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fselect_0026_005fproperty_005fonchange.reuse(_jspx_th_html_005fselect_005f0);
    return false;
  }

  private boolean _jspx_meth_html_005foption_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fselect_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:option
    org.apache.strutsel.taglib.html.ELOptionTag _jspx_th_html_005foption_005f0 = (org.apache.strutsel.taglib.html.ELOptionTag) _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.get(org.apache.strutsel.taglib.html.ELOptionTag.class);
    _jspx_th_html_005foption_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005foption_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fselect_005f0);
    // /gesCur.jsp(43,9) name = value type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005foption_005f0.setValueExpr("level");
    int _jspx_eval_html_005foption_005f0 = _jspx_th_html_005foption_005f0.doStartTag();
    if (_jspx_eval_html_005foption_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_005foption_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_005foption_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_005foption_005f0.doInitBody();
      }
      do {
        if (_jspx_meth_bean_005fmessage_005f11(_jspx_th_html_005foption_005f0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_html_005foption_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_005foption_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_html_005foption_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f0);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005foption_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f11 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f11.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005foption_005f0);
    // /gesCur.jsp(43,37) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f11.setKeyExpr("application.level");
    int _jspx_eval_bean_005fmessage_005f11 = _jspx_th_bean_005fmessage_005f11.doStartTag();
    if (_jspx_th_bean_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f11);
    return false;
  }

  private boolean _jspx_meth_html_005foption_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fselect_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:option
    org.apache.strutsel.taglib.html.ELOptionTag _jspx_th_html_005foption_005f1 = (org.apache.strutsel.taglib.html.ELOptionTag) _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.get(org.apache.strutsel.taglib.html.ELOptionTag.class);
    _jspx_th_html_005foption_005f1.setPageContext(_jspx_page_context);
    _jspx_th_html_005foption_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fselect_005f0);
    // /gesCur.jsp(44,9) name = value type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005foption_005f1.setValueExpr("area");
    int _jspx_eval_html_005foption_005f1 = _jspx_th_html_005foption_005f1.doStartTag();
    if (_jspx_eval_html_005foption_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_005foption_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_005foption_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_005foption_005f1.doInitBody();
      }
      do {
        if (_jspx_meth_bean_005fmessage_005f12(_jspx_th_html_005foption_005f1, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_html_005foption_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_005foption_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_html_005foption_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f1);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005foption_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f12 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f12.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005foption_005f1);
    // /gesCur.jsp(44,36) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f12.setKeyExpr("application.area");
    int _jspx_eval_bean_005fmessage_005f12 = _jspx_th_bean_005fmessage_005f12.doStartTag();
    if (_jspx_th_bean_005fmessage_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f12);
    return false;
  }

  private boolean _jspx_meth_html_005foption_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fselect_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:option
    org.apache.strutsel.taglib.html.ELOptionTag _jspx_th_html_005foption_005f2 = (org.apache.strutsel.taglib.html.ELOptionTag) _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.get(org.apache.strutsel.taglib.html.ELOptionTag.class);
    _jspx_th_html_005foption_005f2.setPageContext(_jspx_page_context);
    _jspx_th_html_005foption_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fselect_005f0);
    // /gesCur.jsp(45,9) name = value type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005foption_005f2.setValueExpr("content");
    int _jspx_eval_html_005foption_005f2 = _jspx_th_html_005foption_005f2.doStartTag();
    if (_jspx_eval_html_005foption_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_005foption_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_005foption_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_005foption_005f2.doInitBody();
      }
      do {
        if (_jspx_meth_bean_005fmessage_005f13(_jspx_th_html_005foption_005f2, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_html_005foption_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_005foption_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_html_005foption_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f2);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005foption_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f13 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f13.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005foption_005f2);
    // /gesCur.jsp(45,39) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f13.setKeyExpr("application.content");
    int _jspx_eval_bean_005fmessage_005f13 = _jspx_th_bean_005fmessage_005f13.doStartTag();
    if (_jspx_th_bean_005fmessage_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f13);
    return false;
  }

  private boolean _jspx_meth_html_005foption_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fselect_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:option
    org.apache.strutsel.taglib.html.ELOptionTag _jspx_th_html_005foption_005f3 = (org.apache.strutsel.taglib.html.ELOptionTag) _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.get(org.apache.strutsel.taglib.html.ELOptionTag.class);
    _jspx_th_html_005foption_005f3.setPageContext(_jspx_page_context);
    _jspx_th_html_005foption_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fselect_005f0);
    // /gesCur.jsp(46,14) name = value type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005foption_005f3.setValueExpr("objective");
    int _jspx_eval_html_005foption_005f3 = _jspx_th_html_005foption_005f3.doStartTag();
    if (_jspx_eval_html_005foption_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_005foption_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_005foption_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_005foption_005f3.doInitBody();
      }
      do {
        if (_jspx_meth_bean_005fmessage_005f14(_jspx_th_html_005foption_005f3, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_html_005foption_005f3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_005foption_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_html_005foption_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005foption_0026_005fvalue.reuse(_jspx_th_html_005foption_005f3);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005foption_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f14 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f14.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005foption_005f3);
    // /gesCur.jsp(46,46) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f14.setKeyExpr("application.objective");
    int _jspx_eval_bean_005fmessage_005f14 = _jspx_th_bean_005fmessage_005f14.doStartTag();
    if (_jspx_th_bean_005fmessage_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f14);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f15 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f15.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(50,4) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f15.setKeyExpr("application.nodeform.description");
    int _jspx_eval_bean_005fmessage_005f15 = _jspx_th_bean_005fmessage_005f15.doStartTag();
    if (_jspx_th_bean_005fmessage_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f15);
    return false;
  }

  private boolean _jspx_meth_html_005ftextarea_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:textarea
    org.apache.strutsel.taglib.html.ELTextareaTag _jspx_th_html_005ftextarea_005f0 = (org.apache.strutsel.taglib.html.ELTextareaTag) _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.get(org.apache.strutsel.taglib.html.ELTextareaTag.class);
    _jspx_th_html_005ftextarea_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005ftextarea_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(51,4) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f0.setPropertyExpr("description");
    // /gesCur.jsp(51,4) name = rows type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f0.setRowsExpr("8");
    // /gesCur.jsp(51,4) name = cols type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f0.setColsExpr("41");
    int _jspx_eval_html_005ftextarea_005f0 = _jspx_th_html_005ftextarea_005f0.doStartTag();
    if (_jspx_th_html_005ftextarea_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.reuse(_jspx_th_html_005ftextarea_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.reuse(_jspx_th_html_005ftextarea_005f0);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f16 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f16.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(54,5) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f16.setKeyExpr("application.nodeform.category");
    int _jspx_eval_bean_005fmessage_005f16 = _jspx_th_bean_005fmessage_005f16.doStartTag();
    if (_jspx_th_bean_005fmessage_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f16);
    return false;
  }

  private boolean _jspx_meth_PHTM_005fRadio_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  PHTM:Radio
    edu.xtec.gescurriculum.htmltag.HTMLRadioTag _jspx_th_PHTM_005fRadio_005f0 = (edu.xtec.gescurriculum.htmltag.HTMLRadioTag) _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.get(edu.xtec.gescurriculum.htmltag.HTMLRadioTag.class);
    _jspx_th_PHTM_005fRadio_005f0.setPageContext(_jspx_page_context);
    _jspx_th_PHTM_005fRadio_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(55,6) name = id type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005fRadio_005f0.setId("content");
    // /gesCur.jsp(55,6) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005fRadio_005f0.setName("category");
    int _jspx_eval_PHTM_005fRadio_005f0 = _jspx_th_PHTM_005fRadio_005f0.doStartTag();
    if (_jspx_th_PHTM_005fRadio_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005fRadio_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005fRadio_005f0);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f17 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f17.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(59,5) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f17.setKeyExpr("application.nodeform.category");
    int _jspx_eval_bean_005fmessage_005f17 = _jspx_th_bean_005fmessage_005f17.doStartTag();
    if (_jspx_th_bean_005fmessage_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f17);
    return false;
  }

  private boolean _jspx_meth_PHTM_005fRadio_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  PHTM:Radio
    edu.xtec.gescurriculum.htmltag.HTMLRadioTag _jspx_th_PHTM_005fRadio_005f1 = (edu.xtec.gescurriculum.htmltag.HTMLRadioTag) _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.get(edu.xtec.gescurriculum.htmltag.HTMLRadioTag.class);
    _jspx_th_PHTM_005fRadio_005f1.setPageContext(_jspx_page_context);
    _jspx_th_PHTM_005fRadio_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(60,5) name = id type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005fRadio_005f1.setId("objective");
    // /gesCur.jsp(60,5) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005fRadio_005f1.setName("category");
    int _jspx_eval_PHTM_005fRadio_005f1 = _jspx_th_PHTM_005fRadio_005f1.doStartTag();
    if (_jspx_th_PHTM_005fRadio_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005fRadio_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005fRadio_005f1);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f18 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f18.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(64,5) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f18.setKeyExpr("application.nodeform.category");
    int _jspx_eval_bean_005fmessage_005f18 = _jspx_th_bean_005fmessage_005f18.doStartTag();
    if (_jspx_th_bean_005fmessage_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f18);
    return false;
  }

  private boolean _jspx_meth_PHTM_005fRadio_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  PHTM:Radio
    edu.xtec.gescurriculum.htmltag.HTMLRadioTag _jspx_th_PHTM_005fRadio_005f2 = (edu.xtec.gescurriculum.htmltag.HTMLRadioTag) _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.get(edu.xtec.gescurriculum.htmltag.HTMLRadioTag.class);
    _jspx_th_PHTM_005fRadio_005f2.setPageContext(_jspx_page_context);
    _jspx_th_PHTM_005fRadio_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(65,5) name = id type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005fRadio_005f2.setId("level");
    // /gesCur.jsp(65,5) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005fRadio_005f2.setName("category");
    int _jspx_eval_PHTM_005fRadio_005f2 = _jspx_th_PHTM_005fRadio_005f2.doStartTag();
    if (_jspx_th_PHTM_005fRadio_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005fRadio_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fPHTM_005fRadio_0026_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005fRadio_005f2);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f19 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f19.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(68,65) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f19.setKeyExpr("application.nodeform.thesaurus");
    int _jspx_eval_bean_005fmessage_005f19 = _jspx_th_bean_005fmessage_005f19.doStartTag();
    if (_jspx_th_bean_005fmessage_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f19);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f20 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f20.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(69,65) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f20.setKeyExpr("application.nodeform.observations");
    int _jspx_eval_bean_005fmessage_005f20 = _jspx_th_bean_005fmessage_005f20.doStartTag();
    if (_jspx_th_bean_005fmessage_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f20);
    return false;
  }

  private boolean _jspx_meth_html_005ftextarea_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:textarea
    org.apache.strutsel.taglib.html.ELTextareaTag _jspx_th_html_005ftextarea_005f1 = (org.apache.strutsel.taglib.html.ELTextareaTag) _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.get(org.apache.strutsel.taglib.html.ELTextareaTag.class);
    _jspx_th_html_005ftextarea_005f1.setPageContext(_jspx_page_context);
    _jspx_th_html_005ftextarea_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(72,5) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f1.setPropertyExpr("note");
    // /gesCur.jsp(72,5) name = rows type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f1.setRowsExpr("8");
    // /gesCur.jsp(72,5) name = cols type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f1.setColsExpr("20");
    int _jspx_eval_html_005ftextarea_005f1 = _jspx_th_html_005ftextarea_005f1.doStartTag();
    if (_jspx_th_html_005ftextarea_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.reuse(_jspx_th_html_005ftextarea_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.reuse(_jspx_th_html_005ftextarea_005f1);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f21 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f21.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(75,64) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f21.setKeyExpr("application.nodeform.references");
    int _jspx_eval_bean_005fmessage_005f21 = _jspx_th_bean_005fmessage_005f21.doStartTag();
    if (_jspx_th_bean_005fmessage_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f21);
    return false;
  }

  private boolean _jspx_meth_html_005ftextarea_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:textarea
    org.apache.strutsel.taglib.html.ELTextareaTag _jspx_th_html_005ftextarea_005f2 = (org.apache.strutsel.taglib.html.ELTextareaTag) _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.get(org.apache.strutsel.taglib.html.ELTextareaTag.class);
    _jspx_th_html_005ftextarea_005f2.setPageContext(_jspx_page_context);
    _jspx_th_html_005ftextarea_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(77,5) name = property type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f2.setPropertyExpr("references");
    // /gesCur.jsp(77,5) name = rows type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f2.setRowsExpr("8");
    // /gesCur.jsp(77,5) name = cols type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ftextarea_005f2.setColsExpr("20");
    int _jspx_eval_html_005ftextarea_005f2 = _jspx_th_html_005ftextarea_005f2.doStartTag();
    if (_jspx_th_html_005ftextarea_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.reuse(_jspx_th_html_005ftextarea_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ftextarea_0026_005frows_005fproperty_005fcols_005fnobody.reuse(_jspx_th_html_005ftextarea_005f2);
    return false;
  }

  private boolean _jspx_meth_html_005freset_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:reset
    org.apache.strutsel.taglib.html.ELResetTag _jspx_th_html_005freset_005f0 = (org.apache.strutsel.taglib.html.ELResetTag) _005fjspx_005ftagPool_005fhtml_005freset_0026_005fstyleClass.get(org.apache.strutsel.taglib.html.ELResetTag.class);
    _jspx_th_html_005freset_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005freset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(80,4) name = styleClass type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005freset_005f0.setStyleClassExpr("buto");
    int _jspx_eval_html_005freset_005f0 = _jspx_th_html_005freset_005f0.doStartTag();
    if (_jspx_eval_html_005freset_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_005freset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_005freset_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_005freset_005f0.doInitBody();
      }
      do {
        if (_jspx_meth_bean_005fmessage_005f22(_jspx_th_html_005freset_005f0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_html_005freset_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_005freset_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_html_005freset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005freset_0026_005fstyleClass.reuse(_jspx_th_html_005freset_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005freset_0026_005fstyleClass.reuse(_jspx_th_html_005freset_005f0);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f22(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005freset_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f22 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f22.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005freset_005f0);
    // /gesCur.jsp(80,34) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f22.setKeyExpr("application.reset");
    int _jspx_eval_bean_005fmessage_005f22 = _jspx_th_bean_005fmessage_005f22.doStartTag();
    if (_jspx_th_bean_005fmessage_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f22);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f22);
    return false;
  }

  private boolean _jspx_meth_html_005fsubmit_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:submit
    org.apache.strutsel.taglib.html.ELSubmitTag _jspx_th_html_005fsubmit_005f0 = (org.apache.strutsel.taglib.html.ELSubmitTag) _005fjspx_005ftagPool_005fhtml_005fsubmit_0026_005fstyleClass.get(org.apache.strutsel.taglib.html.ELSubmitTag.class);
    _jspx_th_html_005fsubmit_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005fsubmit_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(81,4) name = styleClass type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005fsubmit_005f0.setStyleClassExpr("buto");
    int _jspx_eval_html_005fsubmit_005f0 = _jspx_th_html_005fsubmit_005f0.doStartTag();
    if (_jspx_eval_html_005fsubmit_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_html_005fsubmit_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_html_005fsubmit_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_html_005fsubmit_005f0.doInitBody();
      }
      do {
        if (_jspx_meth_bean_005fmessage_005f23(_jspx_th_html_005fsubmit_005f0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_html_005fsubmit_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_html_005fsubmit_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_html_005fsubmit_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005fsubmit_0026_005fstyleClass.reuse(_jspx_th_html_005fsubmit_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005fsubmit_0026_005fstyleClass.reuse(_jspx_th_html_005fsubmit_005f0);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f23(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fsubmit_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f23 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f23.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fsubmit_005f0);
    // /gesCur.jsp(81,35) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f23.setKeyExpr("application.submit");
    int _jspx_eval_bean_005fmessage_005f23 = _jspx_th_bean_005fmessage_005f23.doStartTag();
    if (_jspx_th_bean_005fmessage_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f23);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f23);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f0 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f0.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(83,2) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f0.setPropertyExpr("addnode");
    int _jspx_eval_html_005ferrors_005f0 = _jspx_th_html_005ferrors_005f0.doStartTag();
    if (_jspx_th_html_005ferrors_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f0);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f1 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f1.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(84,2) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f1.setPropertyExpr("setnode");
    int _jspx_eval_html_005ferrors_005f1 = _jspx_th_html_005ferrors_005f1.doStartTag();
    if (_jspx_th_html_005ferrors_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f1);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f24(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f24 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f24.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(94,38) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f24.setKeyExpr("application.close");
    int _jspx_eval_bean_005fmessage_005f24 = _jspx_th_bean_005fmessage_005f24.doStartTag();
    if (_jspx_th_bean_005fmessage_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f24);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f24);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f25 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f25.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(110,21) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f25.setKeyExpr("application.level");
    int _jspx_eval_bean_005fmessage_005f25 = _jspx_th_bean_005fmessage_005f25.doStartTag();
    if (_jspx_th_bean_005fmessage_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f25);
    return false;
  }

  private boolean _jspx_meth_PHTM_005ful_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  PHTM:ul
    edu.xtec.gescurriculum.htmltag.HTMLulTag _jspx_th_PHTM_005ful_005f0 = (edu.xtec.gescurriculum.htmltag.HTMLulTag) _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.get(edu.xtec.gescurriculum.htmltag.HTMLulTag.class);
    _jspx_th_PHTM_005ful_005f0.setPageContext(_jspx_page_context);
    _jspx_th_PHTM_005ful_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(112,3) name = id type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f0.setId("level");
    // /gesCur.jsp(112,3) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f0.setName("NodeForm");
    // /gesCur.jsp(112,3) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f0.setProperty("idlevel");
    int _jspx_eval_PHTM_005ful_005f0 = _jspx_th_PHTM_005ful_005f0.doStartTag();
    if (_jspx_th_PHTM_005ful_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f0);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f2 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f2.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(122,6) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f2.setPropertyExpr("operation");
    int _jspx_eval_html_005ferrors_005f2 = _jspx_th_html_005ferrors_005f2.doStartTag();
    if (_jspx_th_html_005ferrors_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f2);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f3 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f3.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(123,4) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f3.setPropertyExpr("delnode");
    int _jspx_eval_html_005ferrors_005f3 = _jspx_th_html_005ferrors_005f3.doStartTag();
    if (_jspx_th_html_005ferrors_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f3);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f26(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f26 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f26.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(129,21) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f26.setKeyExpr("application.area");
    int _jspx_eval_bean_005fmessage_005f26 = _jspx_th_bean_005fmessage_005f26.doStartTag();
    if (_jspx_th_bean_005fmessage_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f26);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f26);
    return false;
  }

  private boolean _jspx_meth_PHTM_005ful_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  PHTM:ul
    edu.xtec.gescurriculum.htmltag.HTMLulTag _jspx_th_PHTM_005ful_005f1 = (edu.xtec.gescurriculum.htmltag.HTMLulTag) _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.get(edu.xtec.gescurriculum.htmltag.HTMLulTag.class);
    _jspx_th_PHTM_005ful_005f1.setPageContext(_jspx_page_context);
    _jspx_th_PHTM_005ful_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(131,3) name = id type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f1.setId("area");
    // /gesCur.jsp(131,3) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f1.setName("NodeForm");
    // /gesCur.jsp(131,3) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f1.setProperty("idarea");
    int _jspx_eval_PHTM_005ful_005f1 = _jspx_th_PHTM_005ful_005f1.doStartTag();
    if (_jspx_th_PHTM_005ful_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f1);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f4 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f4.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(140,6) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f4.setPropertyExpr("operation");
    int _jspx_eval_html_005ferrors_005f4 = _jspx_th_html_005ferrors_005f4.doStartTag();
    if (_jspx_th_html_005ferrors_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f4);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f5 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f5.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(141,4) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f5.setPropertyExpr("delnode");
    int _jspx_eval_html_005ferrors_005f5 = _jspx_th_html_005ferrors_005f5.doStartTag();
    if (_jspx_th_html_005ferrors_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f5);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f27(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f27 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f27.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(147,21) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f27.setKeyExpr("application.content");
    int _jspx_eval_bean_005fmessage_005f27 = _jspx_th_bean_005fmessage_005f27.doStartTag();
    if (_jspx_th_bean_005fmessage_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f27);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f27);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f28 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f28.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(151,19) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f28.setKeyExpr("application.content.cc");
    int _jspx_eval_bean_005fmessage_005f28 = _jspx_th_bean_005fmessage_005f28.doStartTag();
    if (_jspx_th_bean_005fmessage_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f28);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f29(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f29 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f29.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(152,19) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f29.setKeyExpr("application.content.cp");
    int _jspx_eval_bean_005fmessage_005f29 = _jspx_th_bean_005fmessage_005f29.doStartTag();
    if (_jspx_th_bean_005fmessage_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f29);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f29);
    return false;
  }

  private boolean _jspx_meth_bean_005fmessage_005f30(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:message
    org.apache.strutsel.taglib.bean.ELMessageTag _jspx_th_bean_005fmessage_005f30 = (org.apache.strutsel.taglib.bean.ELMessageTag) _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.get(org.apache.strutsel.taglib.bean.ELMessageTag.class);
    _jspx_th_bean_005fmessage_005f30.setPageContext(_jspx_page_context);
    _jspx_th_bean_005fmessage_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(153,19) name = key type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_bean_005fmessage_005f30.setKeyExpr("application.content.ca");
    int _jspx_eval_bean_005fmessage_005f30 = _jspx_th_bean_005fmessage_005f30.doStartTag();
    if (_jspx_th_bean_005fmessage_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f30);
      return true;
    }
    _005fjspx_005ftagPool_005fbean_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_bean_005fmessage_005f30);
    return false;
  }

  private boolean _jspx_meth_PHTM_005ful_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  PHTM:ul
    edu.xtec.gescurriculum.htmltag.HTMLulTag _jspx_th_PHTM_005ful_005f2 = (edu.xtec.gescurriculum.htmltag.HTMLulTag) _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.get(edu.xtec.gescurriculum.htmltag.HTMLulTag.class);
    _jspx_th_PHTM_005ful_005f2.setPageContext(_jspx_page_context);
    _jspx_th_PHTM_005ful_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(158,3) name = id type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f2.setId("content");
    // /gesCur.jsp(158,3) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f2.setName("NodeForm");
    // /gesCur.jsp(158,3) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f2.setProperty("idcontent");
    int _jspx_eval_PHTM_005ful_005f2 = _jspx_th_PHTM_005ful_005f2.doStartTag();
    if (_jspx_th_PHTM_005ful_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f2);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f6 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f6.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(168,6) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f6.setPropertyExpr("operation");
    int _jspx_eval_html_005ferrors_005f6 = _jspx_th_html_005ferrors_005f6.doStartTag();
    if (_jspx_th_html_005ferrors_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f6);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f7 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f7.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(169,4) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f7.setPropertyExpr("delnode");
    int _jspx_eval_html_005ferrors_005f7 = _jspx_th_html_005ferrors_005f7.doStartTag();
    if (_jspx_th_html_005ferrors_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f7);
    return false;
  }

  private boolean _jspx_meth_PHTM_005ful_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  PHTM:ul
    edu.xtec.gescurriculum.htmltag.HTMLulTag _jspx_th_PHTM_005ful_005f3 = (edu.xtec.gescurriculum.htmltag.HTMLulTag) _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.get(edu.xtec.gescurriculum.htmltag.HTMLulTag.class);
    _jspx_th_PHTM_005ful_005f3.setPageContext(_jspx_page_context);
    _jspx_th_PHTM_005ful_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(181,1) name = id type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f3.setId("thesaurus");
    // /gesCur.jsp(181,1) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f3.setName("NodeForm");
    // /gesCur.jsp(181,1) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_PHTM_005ful_005f3.setProperty("idlevel");
    int _jspx_eval_PHTM_005ful_005f3 = _jspx_th_PHTM_005ful_005f3.doStartTag();
    if (_jspx_th_PHTM_005ful_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fPHTM_005ful_0026_005fproperty_005fname_005fid_005fnobody.reuse(_jspx_th_PHTM_005ful_005f3);
    return false;
  }

  private boolean _jspx_meth_html_005ferrors_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:errors
    org.apache.strutsel.taglib.html.ELErrorsTag _jspx_th_html_005ferrors_005f8 = (org.apache.strutsel.taglib.html.ELErrorsTag) _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.get(org.apache.strutsel.taglib.html.ELErrorsTag.class);
    _jspx_th_html_005ferrors_005f8.setPageContext(_jspx_page_context);
    _jspx_th_html_005ferrors_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
    // /gesCur.jsp(183,20) name = property type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_html_005ferrors_005f8.setPropertyExpr("user");
    int _jspx_eval_html_005ferrors_005f8 = _jspx_th_html_005ferrors_005f8.doStartTag();
    if (_jspx_th_html_005ferrors_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fhtml_005ferrors_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ferrors_005f8);
    return false;
  }
}

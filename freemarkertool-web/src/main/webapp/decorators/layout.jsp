<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="housepad" uri="/housepad-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
General Sitemsh page decorator

Attributes:
   content: mandatory   body html
   sidebar: optional    extended sidebar content
--%>
<html>
  <head>
    <title><decorator:title default="Freemarker Tool" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <meta name="Keywords" content="freemarker template, freemarker, template, java, mvc, struts2, struts, tag, taglib">
    <meta name="Description" content="A tool to edit and review live FreeMarker templates online">
    <meta name="Robots" content="Index,Follow">
    <meta name="Author" content="Blue Sky Minds Pty Ltd" />
    <meta name="Copyright" content="Blue Sky Minds Pty Ltd" />
    <script type="text/javascript"></script>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/reset-fonts-grids/reset-fonts-grids.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/base/base-min.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/assets/skins/sam/layout.css">
  	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/assets/skins/sam/resize.css"> 
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/button/assets/skins/sam/button.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/skin.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/theme.css">
    <decorator:head/>
  </head>

  <body class="yui-skin-sam">
    <div id="hd">
        <div id="adContainer"></div>
        <div id="title"><img src="/static/images/freemarkertool.jpg" alt="FreeMarker Tool" width="175" height="35"></div>
        <div id="exampleContainer">
          <s:form id="exampleForm" action="example.json">
          <h2><span><a href="${pageContext.request.contextPath}/examples/">Examples:</a></span></h2>
          <select id="examples" name="example">
            <option value="">select...</option>
            <option value="1">hello world</option>
            <option value="2">sequence</option>
            <option value="3">sequence-context</option>
            <option value="4">hash-context</option>
            <option value="5">struts2-div</option>
            <option value="6">struts2-checkbox</option>
          </select>&nbsp;<br/>
          <div class="indicatorContainer"><img id="exampleIndicator" src="/static/images/indicator.gif" style="display:none" alt="Loading..."/></div>
          </s:form>
    </div>
    <decorator:body/>
    <div id="ft"> 
      <a href="http://www.blueskyminds.com.au/contact.jsp" title="Blue Sky Minds"><img src="/static/images/blueskyminds_sml.gif" alt="Blue Sky Minds Logo" width="120" height="15"/><span>Blue Sky Minds</span></a>
    </div>
    </div>
  </body>
</html>
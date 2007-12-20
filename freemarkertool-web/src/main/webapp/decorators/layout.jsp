<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="housepad" uri="/housepad-tags" %>

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
    <decorator:head/>
  </head>

  <body>
    <div id="header">
      <div id="adContainer"></div>            
      <div id="title"><img src="/static/images/freemarkertool.jpg" alt="FreeMarker Tool" width="175" height="35"></div>
      <div id="exampleContainer">
        <h2><span>Examples:</span></h2>
        <select id="examples" name="example">
          <option value=""></option>
          <option value="1">hello world</option>
          <option value="2">struts2-div</option>
          <option value="3">struts2-checkbox</option>
        </select>&nbsp;<br/>
        <div class="indicatorContainer"><img id="exampleIndicator" src="/static/images/indicator.gif" style="display:none" alt="Loading..."/></div>
      </div>
    </div>
    <div id="content">
      <decorator:body/>
    </div>
    <div id="footer"><a href="http://www.blueskyminds.com.au/" title="Blue Sky Minds"><img src="/static/images/blueskyminds_sml.gif" alt="Blue Sky Minds Logo" width="110" height="15"/><span>Blue Sky Minds</span></a></div>
  </body>
</html>
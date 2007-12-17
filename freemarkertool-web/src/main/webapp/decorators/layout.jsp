<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

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
    <meta name="Keywords" content="freemarker java">
    <meta name="Description" content="Renders freemarker templates">
    <meta name="Robots" content="Index,Follow">
    <meta name="MS.Locale" content="EN-AU" />
    <meta name="Author" content="Blue Sky Minds Pty Ltd" />
    <meta name="Copyright" content="Blue Sky Minds Pty Ltd" />
    <script type="text/javascript"></script>
    <decorator:head/>
  </head>

  <body>
    <div id="header">
      <div id="title">Freemarker Tool</div>
      <div id="adContainer"></div>            
    </div>
    <div id="content">
      <decorator:body/>
    </div>
    <div id="footer">Copyright (c) Blue Sky Minds Pty Ltd 2007</div>
  </body>
</html>
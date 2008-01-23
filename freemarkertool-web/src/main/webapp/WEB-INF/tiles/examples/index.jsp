<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="yui" uri="/struts-yui-tags" %>
<%@ taglib prefix="housepad" uri="/housepad-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<html>
<head>
  <title>FreeMarker Template Examples</title>
</head>
<body id="body">
  <div id="yui-main"> <%-- YUI Grids: main body--%>
    <div class="yui-b"> <%-- YUI Grids: block --%>
      <h2>Examples</h2>
    </div>
  </div>  

  <tiles:insertDefinition name="scripts"/>
</body>
</html>
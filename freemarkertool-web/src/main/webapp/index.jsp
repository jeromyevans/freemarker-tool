<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="yui" uri="/struts-yui-tags" %>
<%@ taglib prefix="housepad" uri="/housepad-tags" %>
<html>
<head>
  <title>Freemarker Tool</title>
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/logger/assets/skins/sam/logger.css">
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/datatable/assets/skins/sam/datatable.css">
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/tabview/assets/skins/sam/tabview.css">
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/button/assets/skins/sam/button.css">
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/theme.css">
</head>
<body>
<housepad:div theme="box">
  <s:form id="templateForm" action="parse.json">
    <div id="left">
      <div class="yui-skin-sam">
        <%--<yui:pushbutton id="toggleViewBtn" value="Toggle" noscript="true"/>--%>
        <input type="checkbox" id="toggleViewBtn" checked="false" value="Template"/>
      </div>
    </div>
    <div id="center">
      <housepad:div id="openContainer" theme="box" cssClass="editor">
        <h2>Template</h2>
        <s:textarea id="open" name="openTemplate" value="1"/>
      </housepad:div>
      <housepad:div id="bodyTextContainer" theme="box">
        <h2>Body Text</h2>
        <s:textfield id="bodyText" name="bodyText" value="2"/>
      </housepad:div>
      <housepad:div id="closeContainer" theme="box" cssClass="editor">
        <h2>Close Template</h2>
        <s:textarea id="close" name="closeTemplate" value="3"/>
      </housepad:div>
      <s:submit name="Submit"/>     
      <div id="errorContainer"></div>
      <housepad:div theme="box" cssClass="editor readonly">
        <div id="indicatorContainer"><img id="indicator" src="/static/images/indicator.gif" style="display:none" alt="Loading..."/></div>
        <s:textarea id="outputText" name="outputText" value="" disabled="true"/>
      </housepad:div>      
    </div>
    <div id="right">
      <housepad:div theme="box">
        <h2>Context</h2>
        <s:textfield name="context['parameters.id']" value="a"/>
        <s:textfield name="context['title']" value="b"/>
        <s:textfield name="context['parameters.test']" value="c"/>
      </housepad:div>
    </div>        
  </s:form>
</housepad:div>

<div class="yui-skin-sam">
  <div id="log">Log:</div>
  <div id="myLogger"/>
</div>

<script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/utilities/utilities.js"></script>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/yahoo/yahoo-debug.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/event/event-debug.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/connection/connection-debug.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/dom/dom-debug.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/logger/logger-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/element/element-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/button/button-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/datasource/datasource-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/datatable/datatable-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/tabview/tabview-debug.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/tools-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-dom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-events.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-net.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/freemarkertool.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/trimpath-template-1.0.38.js"></script>

<%--<script type="text/javascript">--%>
  <%--var myLogReader = new YAHOO.widget.LogReader("myLogger");--%>
<%--</script>--%>

</body>
</html>
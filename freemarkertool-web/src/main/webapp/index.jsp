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
</head>
<body>
<housepad:div theme="box">
  <s:form action="parse.json">
    <s:textarea name="openTemplate" value="1"/>
    <s:textfield name="bodyText" value="2"/>
    <s:textarea name="closeTemplate" value="3"/>
    <s:textfield name="context['parameters.id']" value="a"/>
    <s:textfield name="context['title']" value="b"/>
    <s:textfield name="context['parameters.test']" value="c"/>
    <s:submit name="Submit"/>
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
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-core.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-dom.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-events.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-net.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-ui.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/housepad-core.js"></script>--%>

<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/search.js"></script>--%>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/trimpath-template-1.0.38.js"></script>

<script type="text/javascript">
  var myLogReader = new YAHOO.widget.LogReader("myLogger");
</script>

</body>
</html>
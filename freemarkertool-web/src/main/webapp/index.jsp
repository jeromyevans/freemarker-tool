<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="yui" uri="/struts-yui-tags" %>
<%@ taglib prefix="housepad" uri="/housepad-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<html>
<head>
  <title>FreeMarker Template Tools</title>
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/yui/build/button/assets/skins/sam/button.css">
  <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/theme.css">
</head>
<body id="body">
<housepad:div id="mainPanel" theme="box">
  <s:form id="templateForm" action="parse.json">
    <div id="left">
      <div class="yui-skin-sam">
        <div id="toggleViewBtnGroup" class="yui-buttongroup">           
          <input type="radio" id="toggleViewPageBtn" value="Page"/><br/>
          <input type="radio" id="toggleViewTagBtn" value="Tag"/>
        </div>
      </div>
    </div>
    <div id="center">
      <div id="openContainer" class="block">
        <h2 id="openTitleTemplate"><span>Page Template:</span></h2><h2 id="openTitleTag" style="display:none"><span>Open Template:</span></h2>
        <housepad:div id="openContainerBox" theme="box" cssClass="editor">
          <s:textarea id="open" name="openTemplate" value="Type or paste your FreeMarker template here" wrap="off"/>
        </housepad:div>
      </div>
      <div id="bodyTextContainer" class="block">
        <h2><span>Body Text:</span></h2>
        <housepad:div id="bodyTextContainerBox" theme="box" cssClass="editor small">
          <s:textarea id="bodyText" name="bodyText" value="" rows="1" wrap="off" onclick="this.focus();"/>
        </housepad:div>
      </div>
      <div id="closeContainer" class="block">
        <h2><span>Close Template:</span></h2>
        <housepad:div id="closeContainerBox" theme="box" cssClass="editor">
          <s:textarea id="close" name="closeTemplate" value="" wrap="off"/>
        </housepad:div>
      </div>
      <%--<s:submit name="Submit"/>     --%>
      <div id="errorContainer"></div>
      <div id="outputTextContainer" class="block">
        <h2><span>FreeMarker Result:</span></h2>
        <housepad:div theme="box" cssClass="editor readonly">
          <div id="indicatorContainer"><img id="indicator" src="/static/images/indicator.gif" style="display:none" alt="Loading..."/></div>
          <s:textarea id="outputText" name="outputText" value="" disabled="true" cssStyle="color:#000000"/>
        </housepad:div>
      </div>
    </div>
    <div id="right" class="block">
      <housepad:div id="settings" theme="box">
        <div class="drop">
        <label for="version">Version:</label>
          <select id="version" name="version">
            <option value="freemarker-2.3.8" selected="true">freemarker-2.3.8</option>
          </select>&nbsp;<br/>
        </div>
        <div class="drop">
        <label for="version">Context Root:</label>
          <select id="root" name="root">
            <option value="map" selected="true">Map</option>
          </select>&nbsp;<br/>
        </div>
      </housepad:div>
      <h2><span>Context:</span></h2>
      <housepad:div theme="box">
        <div id="contextPanel">
          <table width="100%" summary="Table of values to include in the Template Context">
            <thead><tr><th></th><th>Name</th><th>Value</th><th>Null</th></tr></thead>
            <tbody id="contextContainer"></tbody>
          </table>
        </div>
      </housepad:div>
    </div>        
  </s:form>
</housepad:div>

<!--<div class="yui-skin-sam">-->
  <!--<div id="log">Log:</div>-->
  <!--<div id="myLogger"/>-->
<!--</div>-->

<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/utilities/utilities.js"></script>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/yahoo/yahoo-debug.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/event/event-debug.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/connection/connection-debug.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/dom/dom-debug.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/logger/logger-min.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/element/element-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/button/button-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/datasource/datasource-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/datatable/datatable-beta-min.js"></script>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/tabview/tabview-min.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/yui/build/dragdrop/dragdrop-min.js"></script>--%>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/tools-min.js"></script>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/trimpath-template-1.0.38.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-core.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-dom.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-events.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-net.js"></script>--%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/blueskyminds-ui.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/freemarkertool-min.js"></script>

<%--<script type="text/javascript">--%>
  <%--var myLogReader = new YAHOO.widget.LogReader("myLogger");--%>
<%--</script>--%>
<%--<textarea id="contextFieldTemplate" style="display:none">--%>
    <%--<tiles:insertDefinition name="contextField.jst"/>--%>
<%--</textarea>--%>

<!--<textarea id="errorTemplate" style="display:none">-->
    <%--<tiles:insertDefinition name="errorTemplate.jst"/>--%>
<!--</textarea>-->

</body>
</html>
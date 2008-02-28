<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="yui" uri="/struts-yui-tags" %>
<%@ taglib prefix="housepad" uri="/housepad-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<html>
<head>
  <title>FreeMarker Template Tools</title>
</head>
<body id="body">

  <div id="lt">
    <div class="yui-buttongroup">
      <input type="button" id="newBtn" value="New"/>
    </div>
    <div class="yui-buttongroup">
      <h2>Layout:</h2>
      <div id="toggleViewBtnGroup">
        <input type="radio" id="toggleViewPageBtn" value="Page"/><br/>
        <input type="radio" id="toggleViewTagBtn" value="Tag"/>
      </div>
    </div>
  </div>

  <s:form id="templateForm" action="parse.json">
    <s:hidden name="openTemplate"/>
    <s:hidden name="bodyText"/>
    <s:hidden name="closeTemplate"/>
  </s:form>

  <div id="bd">
    <div id="bdinput">
      <div id="openContainer" class="block">
        <h2 id="openTitleTemplate"><span>Page Template:</span></h2><h2 id="openTitleTag" style="display:none"><span>Open Template:</span></h2>
        <housepad:div id="openContainerBox" theme="box" cssClass="editor">
          <s:textarea id="open" name="openTemplateInput" value="Type or paste your FreeMarker template here" wrap="off"/>
        </housepad:div>
      </div>
      <div id="bodyTextContainer" class="block">
        <h2><span>Body Text:</span></h2>
        <housepad:div id="bodyTextContainerBox" theme="box" cssClass="editor small">
          <s:textarea id="bodyText" name="bodyTextInput" value="" rows="1" wrap="off" onclick="this.focus();"/>
        </housepad:div>
      </div>
      <div id="closeContainer" class="block">
        <h2><span>Close Template:</span></h2>
        <housepad:div id="closeContainerBox" theme="box" cssClass="editor">
          <s:textarea id="close" name="closeTemplateInput" value="" wrap="off"/>
        </housepad:div>
      </div>
    </div>
          <%--<s:submit name="Submit"/>     --%>
    <div id="bdoutput">
      <div id="errorContainer"></div>
      <div id="outputTextContainer" class="block">
            <h2><span>FreeMarker Result:</span></h2>
            <housepad:div theme="box" cssClass="editor readonly">
              <div id="indicatorContainer"><img id="indicator" src="/static/images/indicator.gif" style="display:none" alt="Loading..."/></div>
              <%--<s:textarea id="outputText" name="outputText" value="" onfocus="this.blur();"/>--%>
              <div id="outputText"></div>
            </housepad:div>
      </div>
    </div>
  </div>

  <div id="rt">
    <housepad:div id="settings" theme="box">
      <div class="drop">
        <label for="version">Version:</label>
        <select id="version" name="version">
          <option value="freemarker-2.3.11" selected="true">freemarker-2.3.11</option>
        </select>
      </div>
      <div class="drop">
        <label for="root">Context Root:</label>
        <select id="root" name="root">
          <option value="map" selected="true">Map</option>
        </select>
      </div>
    </housepad:div>
    <div class="block">
      <h2><span>Context:</span></h2>
      <%--<housepad:div theme="box">--%>
        <div id="contextPanel">
          <table width="100%" summary="Table of values to include in the Template Context">
            <thead><tr><th class="check"></th><th>Name</th><th>Value</th><th class="check">Null</th></tr></thead>
            <tbody id="contextContainer"></tbody>
          </table>
        </div>
      <%--</housepad:div>--%>
    </div>
  </div>

  <tiles:insertDefinition name="scripts"/>

</body>
</html>
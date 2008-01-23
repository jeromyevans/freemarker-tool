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
  <s:form id="templateForm" action="parse.json">
  <div id="yui-main"> <%-- YUI Grids: main body--%>
    <div class="yui-b"> <%-- YUI Grids: block --%>
      <div class="yui-gf"> <%-- YUI Grids: grid  1/4 - 3/4 --%>
        <div id="left" class="yui-u first yui-skin-sam"> <%-- YUI Grids: first unit (1/4) (override to 10%) --%>
          <div id="toggleViewBtnGroup" class="yui-buttongroup">
            <input type="radio" id="toggleViewPageBtn" value="Page"/><br/>
            <input type="radio" id="toggleViewTagBtn" value="Tag"/>
          </div>
        </div>
        <div id="center" class="yui-u"> <%-- YUI Grids: second unit (3/4) (override to 90%) --%>
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
              <s:textarea id="outputText" name="outputText" value="" onfocus="this.blur();"/>
            </housepad:div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div id="right" class="yui-b"> <%-- YUI Grids: block (not main): 300px via doc3 page--%>
    <housepad:div id="settings" theme="box">
      <div class="drop">
        <label for="version">Version:</label>
        <select id="version" name="version">
          <option value="freemarker-2.3.8" selected="true">freemarker-2.3.8</option>
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
  </s:form>

  <tiles:insertDefinition name="scripts"/>

</body>
</html>
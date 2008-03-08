<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="yui" uri="/struts-yui-tags" %>
<%@ taglib prefix="housepad" uri="/housepad-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<html>
<head>
  <title>FreeMarker Template Preview for Java Developers</title>
</head>
<body id="body">
  <div id="help" style="display: none">
    <h1>FreeMarker Tool Help</h1>
    <p>FreeMarker is a Java Template Engine Library. Use the FreeMarker Tool to quickly view the result of processing a <a href="http://www.freemarker.org">FreeMarker</a> Template.</p>
    <h2>Usage</h2>
    <p>Type some text into the Page Template and view the response from the template engine a moment later.</p>
    <h2>Context</h2>
    <p>The Context is a nested Map of strings, hashes and sequences.  Use dot notation in the name to
      define an entry in a hash and square-bracket notation to define an
      entry in a sequence.</p>
<div class="codeBody">
   Create an entry named id in the hash named parameters:<br/>
   <div class="code">1. parameters.id</div>
   Create an entry named suburb in a hash named address in a hash named person:<br/>
   <div class="code">2. person.address.suburb</div>
    Create the third entry in a sequence named parameters<br/>
   <div class="code">3. parameters[2]</div>
    Create the second entry in a sequence named lines in the address entry of person<br/>
   <div class="code">4. person.address.lines[1]</div>
</div>
      <p>Hashes can also use the square-bracket notation and the notation can be mixed.  The tool will infer whether an entry
      needs to be a string, sequence or map.</p>
<div class="codeBody">
 Create an entry called id in the hash named parameters<br/>
   <div class="code">1. parameters[id]</div>
 Create an entry called id in the hash named parameters<br/>
   <div class="code">2. parameters['id']</div>
 And so on:<br/>
   <div class="code">3. people[0].address['personal'].lines[1]</div>
</div>
    <h2>Interactive Examples</h2>
    <p>Real examples are provided for editing.  Use them to explore the features of this tool.</p>
  </div>

  <div id="lt" style="visibility:hidden">
    <div class="yui-buttongroup">
      <input type="button" id="helpBtn" value="Help"/>
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
  <div id="bd" style="visibility:hidden">
    <div id="bdinput">
      <div id="openContainer" class="block">
        <h2 id="openTitleTemplate"><span>Page Template:</span></h2><h2 id="openTitleTag" style="display:none"><span>Open Template:</span></h2>
        <housepad:div id="openContainerBox" theme="box" cssClass="editor">
          <s:textarea id="open" name="openTemplate" value="Type or paste your FreeMarker template here..." wrap="off"/>
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
    </div>
          <%--<s:submit name="Submit"/>     --%>
    <div id="bdoutput">
      <div id="errorContainer"></div>
      <div id="outputTextContainer" class="block">
            <h2><span>FreeMarker Result:</span></h2>
            <housepad:div theme="box" cssClass="editor readonly">
              <div id="indicatorContainer"><img id="indicator" src="/static/images/indicator.gif" style="display:none" alt="Loading..."/></div>
              <div id="outputText"><pre style="height:100px; font-size: 16px">...and view the result here</pre></div>
            </housepad:div>
      </div>
    </div>
  </div>

  <div id="rt" style="visibility:hidden">
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
  </s:form>

  <div style="display:none">
  <div id="contactFormContainer">
    <s:form id="contactForm" action="/contact.json" theme="dialog">
      <s:textfield label="Your Name" name="fullName"/>
      <s:textfield label="Your E-mail Address" name="emailAddress"/>
      <s:textarea label="Feature Request/Problem Report" name="message" rows="10"/>
    </s:form>
  </div>
  </div>
  <tiles:insertDefinition name="scripts"/>

</body>
</html>
<?php
header('Content-Type: text/html; charset=ISO-8859-1');
header('Content-Language: en');?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title>FreeMarker Tool<?php if (isset($pagetitle)) {echo " - ".$pagetitle;}?></title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <meta name="Keywords" content="freemarker template, freemarker, template, java, mvc, struts2, struts, tag, taglib">
    <meta name="Description" content="A tool to edit and review live FreeMarker templates online">
    <meta name="Robots" content="Index,Follow">
    <meta name="Author" content="Blue Sky Minds Pty Ltd" />
    <meta name="Copyright" content="Blue Sky Minds Pty Ltd" />
    <script type="text/javascript"></script>
  </head>

  <body>
    <div id="header">
      <div id="title"><img src="/static/images/freemarkertool.jpg" alt="FreeMarker Tool" width="175" height="35"></div>
      <div id="adContainer"></div>
    </div>
    <div id="content">

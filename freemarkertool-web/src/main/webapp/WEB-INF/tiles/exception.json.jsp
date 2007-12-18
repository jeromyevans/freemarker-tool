<%@ page contentType="application/json;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
{ "exception" : {
    "message": "The server reported an exception: <s:property value="%{exception.message}"/>"
}}
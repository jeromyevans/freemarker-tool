<%@ taglib prefix="s" uri="/struts-tags" %>
{ "exception" : {
    "message": "<s:property value="%{exception.message}"/>",
    "stacktrace": "<s:property value="%{exceptionStack}"/>"
}}
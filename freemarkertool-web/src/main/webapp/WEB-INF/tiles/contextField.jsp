<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:useAttribute name="index" scope="page"/>
<tr id="context[${index}].container">
  <td><input id="context[${index}].enabled" type="checkbox" name="context[${index}].enabled"></td>
  <td><input id="context[${index}].name" class="name" type="text" name="context[${index}].name"></td>
  <td><input id="context[${index}].value" class="value" type="text" name="context[${index}].value"></td>
  <td><input id="context[${index}].nullValue" type="checkbox" name="context[${index}].nullValue"></td>
</tr>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr id="tr${param.id}">
	<td class="a-center">${param.id}</td>
    <td class="a-center name${param.id}">${param.name}</td>
    <td class="a-center">
        <a class="moduleDetails" href="#"><img src="resources/images/icons/information.png" title="View Details" onClick="displayModuleDetailsDialog(${param.id})" /></a>
		<a class="editModule" href="#"><img src="resources/images/icons/pencil.png" title="Edit Module" onClick="displayEditModuleDialog(${param.id})" /></a>
		<a class="deleteModule" href="#"><img src="resources/images/icons/delete.png" title="Delete Module" onClick="displayDeleteModuleDialog(${param.id})" /></a>
	</td>
</tr>
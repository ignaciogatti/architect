<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr id="trArchitecture${param.id}">
	<td class="a-center">${param.id}</td>
	<td class="a-center name${param.id}"><a href="analysisPanel?architecture=${param.id}">${param.name}</a></td>
	<td class="a-center">
		<a href="analysisPanel?architecture=${param.id}"><img src="resources/images/icons/folder_page_white.png" title="Open Architecture" /></a> 
		<a class="editArchitecture" href="#"><img src="resources/images/icons/pencil.png" title="Edit Architecture" onClick="displayEditDialog(${param.id})" /></a>
		<a class="deleteArchitecture" href="#"><img src="resources/images/icons/delete.png" title="Delete Architecture" onClick="displayDeleteDialog(${param.id})" /></a>
	</td>
</tr>

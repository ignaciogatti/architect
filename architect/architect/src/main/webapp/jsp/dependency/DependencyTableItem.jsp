<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr id="tr${param.idChild}-${param.idParent}">
	<td class="a-center parent">${param.parentName}</td>
	<td class="a-center child">${param.childName}</td>
	<td class="a-center couplingcost-${param.idChild}-${param.idParent}">${param.couplingcost}</td>
	<td class="a-center">
		<a id="editDependency"><img src="resources/images/icons/pencil.png" title="Edit Dependency" onClick="displayEditDialog(${param.idChild},${param.idParent})"/></a>
		<a id="deleteDependency"><img src="resources/images/icons/delete.png" title="Delete Dependency" onClick="displayDeleteDialog(${param.idChild},${param.idParent})"/></a>
	</td>
</tr>

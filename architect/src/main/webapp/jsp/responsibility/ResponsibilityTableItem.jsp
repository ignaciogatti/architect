<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr id="tr${param.id}">
	<td class="a-center">${param.id}</td>
    <td class="a-center name${param.id}">${param.name}</td>
    <td class="a-center module${param.id}">${param.module}</td>
    <td class="a-center complexity${param.id}">${param.complexity}</td>
    <td class="a-center executionTime${param.id}">${param.executionTime}</td>
    <td class="a-center">
    	<a class="scenarios" href="#"> <img src="resources/images/icons/text_list_numbers.png" title="Scenarios" onClick="displayScenariosDialog(${param.id})" /></a>
        <a class="dependencies" href="#"> <img src="resources/images/icons/arrow_join.png" title="Dependencies" onClick="displayDependenciesDialog(${param.id})" /></a>
        <a class="responsibilityDetails" href="#"><img src="resources/images/icons/information.png" title="View Details" onClick="displayResponsibilityDetailsDialog(${param.id})" /></a>
		<a class="editResponsibility" href="#"><img src="resources/images/icons/pencil.png" title="Edit Responsibility" onClick="displayEditResponsibilityDialog(${param.id})" /></a>
		<a class="deleteResponsibility" href="#"><img src="resources/images/icons/delete.png" title="Delete Responsibility" onClick="displayDeleteResponsibilityDialog(${param.id})" /></a>
	</td>
</tr>
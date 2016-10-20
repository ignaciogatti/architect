<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr id="tr${param.id}">
	<td class="a-center">${param.id}</td>
	<td class="a-center name${param.id}">${param.name}</td>
	<td class="a-center type${param.id}">${param.type}</td>
	<td class="a-center priority${param.id}">${param.priority}</td>
	<td class="a-center response${param.id}">${param.measure} ${param.response}</td>
	<td class="a-center">
		<a id="scenarioResponsibilities"><img src="resources/images/icons/text_list_numbers.png" title="Scenario Responsibilities" onClick="displayRespDialog(${param.id})"/></a>
		<a id="scenarioDetail"><img src="resources/images/icons/information.png" title="Scenario Details"  onClick="displayDetailsDialog(${param.id})"/></a>
		<a id="editScenario"><img src="resources/images/icons/pencil.png" title="Edit Scenario" onClick="displayEditDialog(${param.id})"/></a>
		<a id="deleteScenario"><img src="resources/images/icons/delete.png" title="Delete Scenario"  onClick="displayDeleteDialog(${param.id})"/></a>
	</td>
</tr>

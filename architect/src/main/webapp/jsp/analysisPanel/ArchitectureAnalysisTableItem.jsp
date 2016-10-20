<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr id="trArchitectureAnalysis${param.id}">
	<td class="a-center">${param.id}</td>
	<td class="a-center scenario${param.id}">${param.scenario}</td>
	<td class="a-center type${param.id}">${param.type}</td>
	<td class="a-center dbot${param.id} }">${param.dbot}</td>
	<td class="a-center enable${param.id}">${param.enable}</td>
	<td class="a-center">
		<a class="editAnalysis" href="#"><img src="resources/images/icons/pencil.png" title="Edit Analysis Configuration" onClick="displayEditArchitectureAnalysisDialog(${param.id})" /></a>
		<a class="deleteAnalysis" href="#"><img src="resources/images/icons/delete.png" title="Delete Analysis Configuration" onClick="displayDeleteArchitectureAnalysisDialog(${param.id})" /></a>
        <a class="startIndividualAnalysis" href="#"><img src="resources/images/icons/Running.png" title="Start Individual Analysis" onClick="startIndividualAnalysis(${param.id})"></a>
		<a class="stopIndividualAnalysis" href="#"><img src="resources/images/icons/Stopped.png" title="Stop Individual Analysis" onClick="stopIndividualAnalysis(${param.id})"></a>
	</td>
</tr>
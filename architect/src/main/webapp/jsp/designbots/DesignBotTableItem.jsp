<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr id="tr${param.id}">
	<td class="a-center">${param.id}</td>
    <td class="a-center name${param.id}">${param.name}</td>
    <td class="a-center qualityAttribute${param.id}">${param.qualityAttribute}</td>
    <td class="a-center">
<%--     	<a class="tacticsDb" rel="<s:property value="#d.id"/>" href="#"><img src="resources/images/icons/text_list_numbers.png" title="Asignar tacticas"  /></a> --%>
        <a class="designBotDetails" href="#"><img src="resources/images/icons/information.png" title="Design Bot Details" onClick="displayDesignBotDetailsDialog(${param.id})" /></a>
		<a class="editDesignBot" href="#"><img src="resources/images/icons/pencil.png" title="Edit Design Bot" onClick="displayEditDesignBotDialog(${param.id})" /></a>
		<a class="deleteDesignBot" href="#"><img src="resources/images/icons/delete.png" title="Delete Design Bot" onClick="displayDeleteDesignBotDialog(${param.id})" /></a>
	</td>
</tr>

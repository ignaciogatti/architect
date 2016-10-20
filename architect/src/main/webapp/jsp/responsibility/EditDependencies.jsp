<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h3>${responsibility.name} (${responsibility.description}) depends on :</h3>
<input type="hidden" name="idResp" id="idResp" value="${responsibility.id}" />
<div class="itemDialog">
	<div style="margin-left: 200px; margin-top: 10px;">
		<table class="tableResponsibilities">
			<thead>
				<tr>
					<th>Name</th>
					<th>Coupling Cost</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${dependencies}" var="d">
					<tr>
						<td style="text-align: center"><span><img src="resources/images/icons/arrow_right.gif" style="vertical-align: middle;">${d.parentResponsibility.name}</span></td>
						<td style="text-align: center"><span>${d.couplingcost}</span></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!-- <button style="margin-top: 10px;" class="sendDep">Accept</button> -->
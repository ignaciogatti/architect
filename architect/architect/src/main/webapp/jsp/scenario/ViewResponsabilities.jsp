<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	$(document).ready(function() {
		$(".tableResponsibilities tbody tr:odd").css("background", " #F6F6F6");
		$(".tableResponsibilities tbody tr").mouseover(function() {
			$(this).css("background", "#e5f5ff");
		});
		$(".tableResponsibilities tbody tr").mouseout(function() {
			$(".tableResponsibilities tbody tr:odd").css("background", "#F6F6F6");
			$(".tableResponsibilities tbody tr:even").css("background", "#FFFFFF");
		});
	});
</script>
<div class="dialogContainer">
	<table class="tableResponsibilities">
		<thead>
			<tr>
				<th width="40px"><a href="#">ID
					<img src="resources/images/icons/arrow_down_mini.gif" width="16" height="16" /></a>
				</th>
				<th>Name</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${responsibilities}" var="r">
				<tr>
					<td style="text-align: center"><span>${r.id}</span></td>
					<td style="text-align: center"><span>${r.name}</span></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="dialogContainer">
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label for="name">Name : </label>
		</div>
		<div id="name" class="itemDialogRight">
			<span>${module.name}</span>
		</div>
	</div>
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label for="description">Description : </label>
		</div>
		<div id="description" class="itemDialogRight">
			<span>${module.description}</span>
		</div>
	</div>
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label>Responsibilities : </label>
		</div>
		<div class="itemDialogRight">
			<ul style="margin-left: 15px; float: left; list-style: none;">
				<c:forEach items="${responsibilities}" var="r">
					<li> 
						<img src="resources/images/icons/arrow_right.gif" style="vertical-align: middle;">${r.name}
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>
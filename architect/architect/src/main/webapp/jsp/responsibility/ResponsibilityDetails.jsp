<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="dialogContainer">
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label for="name">Name : </label>
		</div>
		<div class="itemDialogRight">
			<span>${responsibility.name}</span>
		</div>
	</div>
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label for="description">Description : </label>
		</div>
		<div class="itemDialogRight">
			<span>${responsibility.description}</span>
		</div>
	</div>
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label for="complexity">Complexity : </label>
		</div>
		<div class="itemDialogRight">
			<span>${responsibility.complexity}</span>
		</div>
	</div>
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label for="executionTime">Execution Time : </label>
		</div>
		<div class="itemDialogRight">
			<span>${responsibility.executionTime}</span>
		</div>
	</div>
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label for="module">Module : </label>
		</div>
		<div class="itemDialogRight">
			<span>${responsibility.module.name}</span>
		</div>
	</div>
	<div class="itemDialog">
		<div class="itemDialogLeft">
			<label>Depends On : </label>
		</div>
		<div class="itemDialogRight">
			<ul style="margin-left: 15px; float: left; list-style: none;">
				<c:forEach items="${dependencies}" var="d">
					<li> 
						<img src="resources/images/icons/arrow_right.gif" style="vertical-align: middle;">${d.parentResponsibility.name}
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>
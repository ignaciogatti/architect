<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
	<input type="hidden" name="id" id="id"  value="${responsibility.id}" />
	<div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="name">Name : </label>
        </div>
        <div class="itemDialogRight">
            <textarea name="name" id="name">${responsibility.name}</textarea>
            <p class="errorName" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Name is obligatory.</p>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="description">Description : </label>
        </div>
        <div class="itemDialogRight">
            <textarea name="description" id="description">${responsibility.description}</textarea>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="complexity">Complexity : </label>
        </div>
        <div class="itemDialogRight">
        	<input name="complexity" id="complexity" type="text" value="${responsibility.complexity}"/>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="executionTime">Execution Time : </label>
        </div>
        <div class="itemDialogRight">
        	<input name="executionTime" id="executionTime" type="text" value="${responsibility.executionTime}"/>
        </div>
    </div>
    <div class="itemDialog">
    	<div class="itemDialogLeft">
            <label for="module">Module : </label>
        </div>
        <div style="margin-left: 200px; margin-top: 10px;" >
            <select name="module" id="module" >
<!--             	<option value="-1">NONE</option> -->
            	<c:forEach items="${modules}" var="m">
                    <option
                    	<c:choose>
    						<c:when test="${responsibility.module.id.equals(m.id) == true}">
        						selected="selected"
    						</c:when>
						</c:choose>
                        value="${m.id}">${m.name}</option>
                </c:forEach>
            </select>
         </div>
    </div>
</div>
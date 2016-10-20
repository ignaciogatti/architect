<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="nombre">Name : </label>
        </div>
        <div class="itemDialogRight">
            <input name="nombre" id="nombre" type="text" />
            <p id="errorArchitecture" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Nombre es obligatorio</p>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="tipo">Group : </label>
        </div>
        <div class="itemDialogRight">
            <select name="group" id="group">
            	<c:forEach items="${groups}" var="g">
                     <option value="${g.id}">${g.groupname}</option>
                </c:forEach>
            </select>
        </div>
    </div>
</div>
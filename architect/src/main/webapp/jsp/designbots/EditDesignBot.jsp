<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><div class="dialogContainer">
	<input type="hidden" name="id" id="id"  value="${designBot.id}" />
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="name">Name : </label>
        </div>
        <div class="itemDialogRight">
            <input name="name" id="name" value="${designBot.name}" />
            <p class="errorName" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Name is obligatory</p>
        </div>
    </div>
    <div class="itemDialog">
    	<div class="itemDialogLeft">
            <label for="tactics">Tactics : </label>
        </div>
        <div class="itemDialogRight" >
            <select name="tactics[]" id="tactics" id="multiselectorTactics" multiple="multiple" >
            	<c:forEach items="${tactics}" var="t">
                    <option
                    	<c:choose>
    						<c:when test="${designBot.containsTactic(t) == true}">
        						selected="selected"
    						</c:when>
						</c:choose>
                        value="${t.id}">${t.name}</option>
                </c:forEach>
            </select>
         </div>
    </div>
</div>
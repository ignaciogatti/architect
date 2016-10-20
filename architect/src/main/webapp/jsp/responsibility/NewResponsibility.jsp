<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
	<div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="name">Name : </label>
        </div>
        <div class="itemDialogRight">
            <textarea name="name" id="name"></textarea>
            <p class="errorName" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Name is obligatory.</p>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="description">Description : </label>
        </div>
        <div class="itemDialogRight">
            <textarea name="description" id="description"></textarea>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="complexityForNewResp">Complexity : </label>
        </div>
        <div class="itemDialogRight">
        	<input name="complexityForNewResp" id="complexityForNewResp" type="text"/>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="executionTimeForNewResp">Execution Time : </label>
        </div>
        <div class="itemDialogRight">
        	<input name="executionTimeForNewResp" id="executionTimeForNewResp" type="text"/>
        </div>
    </div>
    <div class="itemDialog">
    	<div class="itemDialogLeft">
            <label for="moduleForNewResp">Module : </label>
        </div>
        <div style="margin-left: 200px; margin-top: 10px;" >
            <select name="moduleForNewResp" id="moduleForNewResp" >
            	<c:forEach items="${modules}" var="m">
                    <option value="${m.id}">${m.name}</option>
                </c:forEach>
            </select>
            <p class="errorModuleSelector" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Module is obligatory.</p>
         </div>
    </div>
</div>
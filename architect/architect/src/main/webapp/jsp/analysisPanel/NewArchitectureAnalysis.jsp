<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="scenario">Scenario : </label>
        </div>
        <div class="itemDialogRight">
            <select name="scenario" id="scenario" >
            	<c:forEach items="${scenarios}" var="s">
                    <option value="${s.id}">${s.name} - ${s.qualityAttribute.name}</option>
                </c:forEach>
            </select>
         </div>
         <div class="itemDialogLeft">
            <label for="designBot">Design Bot : </label>
        </div>
        <div class="itemDialogRight">
            <select name="designBot" id="designBot" >
            	<c:forEach items="${designBots}" var="d">
                    <option value="${d.id}">${d.name} - ${d.qualityAttribute.name}</option>
                </c:forEach>
            </select>
         </div>
         <div class="itemDialogLeft">
            <label for="enable">Enable : </label>
        </div>
        <div class="itemDialogRight">
            <select name="enable" id="enable" >
            	<option value="true" selected="selected">True</option>
            	<option value="false">False</option>
            </select>
         </div>
    </div>  
</div>
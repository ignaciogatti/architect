<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<input type="hidden" name="idAnalysis" id="idAnalysis" value="${architectureAnalysis.id}" />
<div class="dialogContainer">
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="scenario">Scenario : </label>
        </div>
        <div class="itemDialogRight">
            <select name="scenario" id="scenario" >
            	<c:forEach items="${scenarios}" var="s">
                    <option 
                    	<c:choose>
    						<c:when test="${architectureAnalysis.scenario.id.equals(s.id) == true}">
        						selected="selected"
    						</c:when>
						</c:choose>
						value="${s.id}">${s.name} - ${s.qualityAttribute.name}</option>
                </c:forEach>
            </select>
         </div>
         <div class="itemDialogLeft">
            <label for="designBot">Design Bot : </label>
        </div>
        <div class="itemDialogRight">
            <select name="designBot" id="designBot" >
            	<c:forEach items="${designBots}" var="d">
                    <option 
                    	<c:choose>
    						<c:when test="${architectureAnalysis.designBot.id.equals(d.id) == true}">
        						selected="selected"
    						</c:when>
						</c:choose>
						value="${d.id}">${d.name} - ${d.qualityAttribute.name}</option>
                </c:forEach>
            </select>
         </div>
         <div class="itemDialogLeft">
            <label for="enable">Enable : </label>
        </div>
        <div class="itemDialogRight">
            <select name="enable" id="enable" >
            	<option 
            		<c:choose>
    					<c:when test="${architectureAnalysis.enable == true}">
        					selected="selected"
    					</c:when>
					</c:choose>
				value="true">True</option>
            	<option 
            		<c:choose>
    					<c:when test="${architectureAnalysis.enable == false}">
        					selected="selected"
    					</c:when>
					</c:choose>
				value="false">False</option>
            </select>
         </div>
    </div>  
</div>
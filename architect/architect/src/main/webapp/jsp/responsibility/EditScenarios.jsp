<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
<h3>Assign scenarios to : ${responsibility.description}</h3>
    <div class="itemDialog">
        <div style="margin-left: 200px; margin-top: 10px;" >
            <select name="scenarios[]" id="scenarios" id="multiselectorScenarios" multiple="multiple" >
            	<c:forEach items="${scenarios}" var="s">
                    <option
                    	<c:choose>
    						<c:when test="${responsibility.containsScenario(s) == true}">
        						selected="selected"
    						</c:when>
						</c:choose>
                        value="${s.id}">${s.name}</option>
                </c:forEach>
            </select>
            <input type="hidden" name="responsibilityId" id="responsibilityId" value="${responsibility.id}" />
         </div>
    </div>
</div>
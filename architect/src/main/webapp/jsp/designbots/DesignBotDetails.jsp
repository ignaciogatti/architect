<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="name">Name : </label>
        </div>
        <div class="itemDialogRight">
            <span>${designBot.name}</span>
        </div>
    </div>
     <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="qualityAttribute">Quality Attribute : </label>
        </div>
        <div class="itemDialogRight">
            <span>${designBot.qualityAttribute.name}</span>
        </div>
    </div>
    <div class="itemDialog">
      <div class="itemDialogLeft">
        <label>Tactics : </label>
      </div>
      <div class="itemDialogRight">
        <ul style="margin-left : 15px; float: left; list-style: none;">
        	<c:forEach items="${designBot.tactics}" var="t">
                <li>
                	<img src="resources/images/icons/arrow_right.gif" style="vertical-align : middle;">${t.name}
                </li>
          	</c:forEach>
        </ul>
      </div>
    </div>
</div>
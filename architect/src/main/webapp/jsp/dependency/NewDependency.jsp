<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="parent">Parent : </label>
        </div>
        <div class="itemDialogRight">
            <select name="parent" id="parent">
            	<c:forEach items="${responsibilities}" var="r">
                     <option value="${r.id}">${r.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="type">Child : </label>
        </div>
        <div class="itemDialogRight">
            <select name="child" id="child">
            	<c:forEach items="${responsibilities}" var="r">
                     <option value="${r.id}">${r.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="couplingcost">Coupling Cost : </label>
        </div>
        <div class="itemDialogRight">
        	<input name="couplingcost" id="couplingcost" type="text" />
        </div>
    </div>
</div>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><div class="dialogContainer">
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="name">Name : </label>
        </div>
        <div class="itemDialogRight">
            <input name="name" id="name" />
            <p class="errorName" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Name is obligatory</p>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="qualityAttribute">Quality Attribute : </label>
        </div>
        <div class="itemDialogRight">
            <select name="qualityAttribute" id="qualityAttribute">
                 <c:forEach items="${qualityAttributes}" var="q">
                    <option value="${q.id}">${q.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
</div>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<input type="hidden" name="idChild" id="idChild"  value="${dependency.childResponsibility.id}" />
<input type="hidden" name="idParent" id="idParent"  value="${dependency.parentResponsibility.id}" />
<div class="dialogContainer">
   	<div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="child">Child : </label>
        </div>
        <div class="itemDialogRight">
       		<ul style="margin-left: 15px; float: left; list-style: none;">
				<li> 
					<img src="resources/images/icons/arrow_right.gif" style="vertical-align: middle;">${dependency.childResponsibility.name}
				</li>
			</ul>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="parent">Parent : </label>
        </div>
        <div class="itemDialogRight">
        	<ul style="margin-left: 15px; float: left; list-style: none;">
				<li> 
					<img src="resources/images/icons/arrow_right.gif" style="vertical-align: middle;">${dependency.parentResponsibility.name}
				</li>
			</ul>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="couplingcost">Coupling Cost : </label>
        </div>
        <div class="itemDialogRight">
        	<input name="couplingcost" id="couplingcost" type="text" value="${dependency.couplingcost}"/>
        	<p class="errorCouplingcost" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Coupling Cost is obligatory.</p>
        </div>
    </div>
</div>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
	<div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="name">Name : </label>
        </div>
        <div class="itemDialogRight">
            <span>${currentScenario.name}</span>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="description">Description : </label>
        </div>
        <div class="itemDialogRight">
            <span>${currentScenario.description}</span>
        </div>
    </div>

     <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="type">Type : </label>
        </div>
        <div class="itemDialogRight">
            <span>${currentScenario.qualityAttribute.name}</span>
        </div>
    </div>

   <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="priority">Priority : </label>
        </div>
        <div class="itemDialogRight">
            <span>${currentScenario.priority}</span>
        </div>
    </div>

    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="estimulus">Stimulus : </label>
        </div>
        <div class="itemDialogRight">
            <span>${currentScenario.stimulus}</span>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="source">Stimulus Source : </label>
        </div>
        <div class="itemDialogRight">
            <span>${currentScenario.source}</span>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="enviroment">Environment : </label>
        </div>
        <div class="itemDialogRight">
            <span>${currentScenario.enviroment}</span>
        </div>
     </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="artifact">Artifact : </label>
         </div>
        <div class="itemDialogRight">
            <span>${currentScenario.artifact}</span>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="response">Response : </label>
         </div>
        <div class="itemDialogRight">
            <span>${currentScenario.getResponse().getLabel()}</span>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="measure">Response Measure : </label>
         </div>
        <div class="itemDialogRight">
            <span>${currentScenario.measure}</span>
        </div>
    </div>
    <div class="itemDialog">
      <div class="itemDialogLeft">
        <label>Responsibilities : </label>
      </div>
      <div class="itemDialogRight">
        <ul style="margin-left : 15px; float: left; list-style: none;">
        	<c:forEach items="${responsibilities}" var="r">
                <li>
                	<img src="resources/images/icons/arrow_right.gif" style="vertical-align : middle;">${r.getName()}
                </li>
          	</c:forEach>
        </ul>
      </div>
    </div>
</div>

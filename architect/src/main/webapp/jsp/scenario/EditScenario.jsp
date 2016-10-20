<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dialogContainer">
	<div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="name">name : </label>
        </div>
        <div class="itemDialogRight">
            <textarea name="name" id="name">${currentScenario.name}</textarea>
            <p class="errorName" style="color : red; font-size : 11px; display : none; float : left; width : 100%; padding-left:10px; padding-top:10px;" >Nombre es obligatorio</p>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="description">Description : </label>
        </div>
        <div class="itemDialogRight">
            <textarea name="description" id="description">${currentScenario.description}</textarea>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="tipo">Type : </label>
        </div>
        <div class="itemDialogRight">
            <select name="type" id="type">
            	<c:forEach items="${types}" var="t">
                     <option value="${t.id}">${t.name}</option>
                </c:forEach>
            </select>
          	<script type="text/javascript">
            	$(document).ready(function(){
              		$("#type").val('${currentScenario.qualityAttribute.id}');
            	});
          	</script>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="tipo">Priority : </label>
        </div>
        <div class="itemDialogRight">
            <select name="priority" id="priority">
                 <option value="1">1</option>
                 <option value="2">2</option>
                 <option value="3">3</option>
                 <option value="4">4</option>
                 <option value="5">5</option>
                 <option value="6">6</option>
                 <option value="7">7</option>
                 <option value="8">8</option>
                 <option value="9">9</option>
                 <option value="10">10</option>
            </select>
          	<script type="text/javascript">
            	$(document).ready(function(){
              		$("#priority").val('${currentScenario.priority}');
            	});
          	</script>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="estimulo">Stimulus : </label>
        </div>
        <div class="itemDialogRight">
            <input name="stimulus" id="stimulus" type="text" value="${currentScenario.stimulus}" />
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="fuente">Stimulus Source : </label>
        </div>
        <div class="itemDialogRight">
            <input name="source" id="source" type="text" value="${currentScenario.source}" />
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="ambiente">Environment : </label>
        </div>
        <div class="itemDialogRight">
            <input name="enviroment" id="enviroment" type="text" value="${currentScenario.enviroment}" />
        </div>
     </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="artefecto">Artifact : </label>
         </div>
        <div class="itemDialogRight">
            <input name="artifact" id="artifact" type="text" value="${currentScenario.artifact}" />
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="response">Response : </label>
         </div>
        <div class="itemDialogRight">
        	<select name="response" id="response">
        		<c:set var="types" value="<%=architect.model.Scenario.ResponseType.values()%>" />
        		<c:forEach items="${types}" var="t">
        			<c:choose>
         				<c:when test="${currentScenario.getResponse().equals(t)}">
            				<option value="${t.ordinal()}" selected="selected">${t.getLabel()}</option>
        			</c:when>
         			<c:otherwise>
            			<option value="${t.ordinal()}">${t.getLabel()}</option>
         			</c:otherwise>
      				</c:choose>
                </c:forEach>
        	</select>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="respuesta_m">Response Measure : </label>
         </div>
        <div class="itemDialogRight">
            <input name="measure" id="measure" type="text" value="${currentScenario.measure}"/>
        </div>
    </div>
    <input type="hidden" name="id" id="id"  value="${currentScenario.id}" />
</div>
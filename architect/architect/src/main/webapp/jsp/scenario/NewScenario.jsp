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
            <label for="type">Type : </label>
        </div>
        <div class="itemDialogRight">
            <select name="type" id="type">
            	<c:forEach items="${types}" var="t">
                     <option value="${t.id}">${t.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="priority">Priority : </label>
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
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="stimulus">Stimulus : </label>
        </div>
        <div class="itemDialogRight">
            <input name="stimulus" id="stimulus" type="text" />
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="source">Stimulus Source : </label>
        </div>
        <div class="itemDialogRight">
            <input name="source" id="source" type="text" />
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="enviroment">Environment : </label>
        </div>
        <div class="itemDialogRight">
            <input name="enviroment" id="enviroment" type="text" />
        </div>
     </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="artifact">Artifact : </label>
         </div>
        <div class="itemDialogRight">
            <input name="artifact" id="artifact" type="text" />
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
            		<option value="${t.ordinal()}">${t.getLabel()}</option>
         		</c:forEach>
        	</select>
        </div>
    </div>
    <div class="itemDialog">
        <div class="itemDialogLeft">
            <label for="measure">Response Measure : </label>
         </div>
        <div class="itemDialogRight">
            <input name="measure" id="measure" type="text" />
        </div>
    </div>
</div>
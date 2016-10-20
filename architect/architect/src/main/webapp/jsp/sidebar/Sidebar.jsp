<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<script type="text/javascript" src="resources/js/sidebar/sidebar.js"></script>
<div id="dialogDetailResp"></div>
<div id="dialogDetailScen"></div>
<div id="dialogDetailTactics"></div>
<div id="dialogDetailDesign"></div>
<div class="sidebarContainerSuper">
<div id="optionsContainer" class="sidebarContainer">
	<ul>
		<li>
			<h3>
				<a class="undo" id="statesTitle" href="#">Back to State</a>
			</h3>
			<div id="statesBody" style="display: none;">
			</div>
		</li>
	</ul>
</div>
<div id="sidebar" class="sidebarContainer">
	<ul>
		<li>
			<h3>
				<a class="house" href="#">Architectures</a>
			</h3>
			<ul class="architectureOptions" style="display: none;">
			</ul>
		</li>
	</ul>
	<ul>
		<li><h3>
				<a class="scen" href="#">Scenarios</a>
			</h3>
			<ul class="scenOptions" style="display: none;">
			</ul>
		</li>
	</ul>
	<ul>
		<li><h3>
				<a class="resp" href="#">Responsibilities</a>
			</h3>
			<ul class="respOptions" style="display: none;">
			</ul>
		</li>
	</ul>
	<ul>
		<li><h3>
				<a class="design" href="#">Design Bots</a>
			</h3>
			<ul class="designOptions" style="display: none;">
			</ul>
		</li>
	</ul>
</div>
</div>
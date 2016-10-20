<script type="text/javascript">
	mxBasePath = 'resources/mxgraph';
</script>
<script type="text/javascript" src="resources/js/mxgraph/mxClient.js"></script>
<script type="text/javascript" src="resources/js/mxgraph/mxApplication.js"></script>
	<div id="page">
		<div id="mainActions" style="margin: 4px;">
		</div>
		<div id="selectActions" style="margin-left: 35px;">
		</div>
		<table border="0" width="700px">
			<tr>
<!-- 				<td id="toolbar" style="width:16px;" valign="top"> -->
<!-- 					Toolbar Here				 -->
<!-- 				</td> -->
				<td valign="top" style="border-width:1px;border-style:solid;border-color:black;">
					<div id="graph" style="position:relative;height:480px;width:684px;overflow:hidden;cursor:default;">
						<!-- Graph Here -->
						<center id="splash" style="padding-top:230px;">
							<img src="resources/mxgraph/images/loading.gif">
						</center>
					</div>
					<textarea id="xml" style="height:480px;width:684px;display:none;border-style:none;"></textarea>
				</td>
			</tr>
		</table>
		<span style="float:right;padding-right:36px;">
			<input id="source" type="checkbox"/>Source
		</span>
		<div id="zoomActions" style="width:100%;margin-left: 35px;">
		</div>
		<div id="footer">
			<p id="status">
				<!-- Status Here -->Loading...
			</p>
			<br/>
		</div>
	</div>
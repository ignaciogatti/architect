<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
$(document).ready( function() {
  $(".respItem").click(function()
  {
    $("#dialogDetailResp").dialog('open');
    $("#dialogDetailResp").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
        var id = $(this).attr("rel");
        $.get("responsibilityDetails?id="+id, function (data)
        {
          $("#dialogDetailResp").html(data);
        });
  });
});
</script>
<c:forEach items="${responsibilities}" var="r">
	<li><a class="respItem" rel="${r.id}" href="#">${r.name}</a></li>
</c:forEach>
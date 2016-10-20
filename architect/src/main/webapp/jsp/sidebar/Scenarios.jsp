<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
$(document).ready( function() {
  $(".scenItem").click(function()
  {
    $("#dialogDetailScen").dialog('open');
    $("#dialogDetailScen").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
        var id = $(this).attr("rel");
        $.get("viewScenario?id="+id, function (data)
        {
          $("#dialogDetailScen").html(data);
        });
  });
});
</script>
<c:forEach items="${scenarios}" var="s">
    <li><a class="scenItem" rel="${s.id}" href="#">${s.name}</a></li>
</c:forEach>
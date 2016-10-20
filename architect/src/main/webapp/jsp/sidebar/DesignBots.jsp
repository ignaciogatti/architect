<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
$(document).ready( function() {
  $(".dbItem").click(function()
  {
    $("#dialogDetailDesign").dialog('open');
    $("#dialogDetailDesign").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
        var id = $(this).attr("rel");
        $.get("designBotDetails?id="+id, function (data)
        {
          $("#dialogDetailDesign").html(data);
        });
  });
});
</script>
<c:forEach items="${designbots}" var="d">
    <li><a class="dbItem" rel="${d.id}" href="#">${d.name}</a></li>
</c:forEach>
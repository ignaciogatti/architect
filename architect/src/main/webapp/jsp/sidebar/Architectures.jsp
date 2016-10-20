<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach items="${architectures}" var="a">
    <li><a class="close" href="analysisPanel?architecture=${a.id}">${a.name}</a></li>
</c:forEach>
<%@ include file="/web/taglibs.jsp"%>
<script type="text/javascript" src="web/JS/calendari.js">
</script>
<script type="text/javascript">
<!--


var cal = new CalendarPopup("calendari");
cal.setCssPrefix("TEST");
cal.showYearNavigation();
cal.setMonthNames(<bean:message key='etiq.month'/>);
cal.setDayHeaders(<bean:message key='etiq.week'/>);
cal.setWeekStartDay(1);
cal.setTodayText(<bean:message key='etiq.today'/>);
cal.setReturnFunction("<%=request.getParameter("function")%>");
//-->
</script>
<link type="text/css" href="web/css/calendari.css" rel="stylesheet">

<div id="calendari"></div>
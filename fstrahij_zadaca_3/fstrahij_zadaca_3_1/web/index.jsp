<%-- 
    Document   : index
    Created on : Apr 25, 2017, 7:13:42 PM
    Author     : fstrahij
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Unos IoT uređaja</title>
    </head>
    <body>
        <h1>Unos IoT uređaja</h1>
        <form action="${pageContext.servletContext.contextPath}/DodajUredjaj" method="POST">
            <label for="naziv">Naziv i adresa:</label>
            <input id="naziv" name="naziv" <% if (request.getSession().getAttribute("naziv") != null) {%> value="<%= request.getSession().getAttribute("naziv")%><%}%> "/>
            <input id="adresa" name="adresa" <% if (request.getSession().getAttribute("adresa") != null) {%> value="<%= request.getSession().getAttribute("adresa")%><%}%> "/>
            <input type="submit" name="gumb" value="geoLokacija"/><br/>
            <label for="geoLokacija">Geo lokacija:</label>
            <input id="geoLokacija" name="geoLokacija" <% if (request.getSession().getAttribute("latitude") != null) {%>
                   value="<%= request.getSession().getAttribute("latitude") + " " + request.getSession().getAttribute("longitude")%><%}%>"/>
            <input type="submit" name="gumb" value="spremi"/><br/>
            <input type="submit" name="gumb" value="meteoPodaci"/><br/>           
        </form>
        <%

            if (request.getSession().getAttribute("temp") != null) {
                String temp = request.getSession().getAttribute("temp").toString();
                String vlaga = request.getSession().getAttribute("vlaga").toString();
                String tlak = request.getSession().getAttribute("tlak").toString();
                String sunset = request.getSession().getAttribute("sunset").toString();%>
        <label >Temp:<%=temp%> C</label><br/>
        <label >Vlaga:<%=vlaga%> %</label><br/>
        <label >Tlak:<%=tlak%> hPa</label><br/>
        <label >Sunset:<%=sunset%></label>
        <%}%>                           

    </body>
</html>

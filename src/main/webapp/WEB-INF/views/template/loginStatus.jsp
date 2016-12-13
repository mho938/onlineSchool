<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<s:url value="/" var="rootUrl" />
<li style="font-size: 16px;" class="text-center">

       <% boolean status = (boolean) request.getAttribute("status");
           System.out.println(status);
           if (!status){%>
    <a href="http://localhost:8080/online-school/" style="color: #eecd8d; font-size: 17px;">
        <i class="fa fa-sign-in"></i>
    </a>
    <% }else { %>
       <a onclick="signout()" style="color: red; font-size: 17px; cursor: auto" >
           <script type="text/javascript">
               function signout(){
                   <% String str=(String) request.getAttribute("sessionId"); %>
                   var s="<%=str%>";
                   $.ajax({
                       type: "POST",
                       dataType: 'json',
                       data:JSON.stringify(
                               {
                                   "sessionId" :s
                               })
                       ,
                       contentType: "application/json; charset=utf-8",
                       url: "http://localhost:8080/online-school/adminsignout",
                       error: function (xhr, ajaxOptions,
                                        thrownError) {
                       },
                       success: function (data) {
                            if(data.response=="ok")
                                window.location.replace("/online-school/");
                       }
                   });
               }
           </script>

           <i class="fa fa-power-off"></i>
       </a>
    <%}%>
</li>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="false"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <%
    HttpSession session=request.getSession(false);
		if(session!=null){
		    request.getRequestDispatcher("/DashboardController").forward(request, response);
		}
		
		String error=request.getParameter("error");
		if(error!=null){
			pageContext.setAttribute("error", "User name /password is not valid.");
		}
		
    %>

    <title>PUP Utility</title>

    <!-- Bootstrap Core CSS -->
     <!-- link href="bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    MetisMenu CSS
    <link href="bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    Custom CSS
    <link href="dist/css/sb-admin-2.css" rel="stylesheet">

    Custom Fonts
    <link href="bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"--> 
    
        <link href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap.min.css" />" rel="stylesheet" media="screen">
         <link href="<c:url value="/resources/bower_components/metisMenu/dist/metisMenu.min.css" />" rel="stylesheet" media="screen">
         <link href="<c:url value="/resources/dist/css/sb-admin-2.css" />" rel="stylesheet" media="screen">
         <link href="<c:url value="/resources/bower_components/font-awesome/css/font-awesome.min.css" />" rel="stylesheet" media="screen">
   
    
    
    
    
    

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <style>
.error {
	padding: 2px;
	margin-bottom: 2px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 2px;
	margin-bottom: 2px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

/* #login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
} */
</style>

</head>

<body>

    <div class="container">
     <div class="row">
        
        
            <div class="col-md-4 col-md-offset-4">
            <!--img src="images/logo.jpg"/-->
     		</div>
    </div>
        <div class="row">
        
        
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">PUP Utility Login</h3>
                        <c:if test="${not empty error}">
							<div class="error">${error}</div>
						</c:if>
						
						<c:if test="${not empty msg}">
							<div class="msg">${msg}</div>
						</c:if>
                    </div>
                    <div class="panel-body">
                        <form role="form" name='loginForm' action="<c:url value='/LoginController' />" method='POST'>
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="User Name" name="username" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Password" name="password" type="password" value="">
                                </div>
                               
									
                              <!--   <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="Remember Me">Remember Me
                                    </label>
                                </div> -->
                                <!-- Change this to a button or input when using this as a form -->
                                <!-- <a href="#" class="btn btn-lg btn-success btn-block">Login</a> -->
                        <!--         <button type="submit" class="btn btn-primary btn-lg">Login</button> -->
                        <button type="submit" class="btn btn-primary btn-lg btn-block">Login</button>
                            </fieldset>
                             <%-- <input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" /> --%>
                        </form>
                        
                        
                      <%--   <form name='loginForm' action="<c:url value='/j_spring_security_check' />" method='POST'>
					
								<table>
									<tr>
										<td>User:</td>
										<td><input type='text' name='username'></td>
									</tr>
									<tr>
										<td>Password:</td>
										<td><input type='password' name='password' /></td>
									</tr>
									<tr>
										<td colspan='2'><input name="submit" type="submit"
											value="submit" /></td>
									</tr>
								</table>
					
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
					
							</form> --%>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
   <script src="<c:url value="/resources/bower_components/jquery/dist/jquery.min.js" />"></script>
    <script src="<c:url value="/resources/bower_components/bootstrap/dist/js/bootstrap.min.js" />"></script>
     <script src="<c:url value="/resources/bower_components/metisMenu/dist/metisMenu.min.js" />"></script>
      <script src="<c:url value="/resources/dist/js/sb-admin-2.js" />"></script>
      
    <!-- jQuery -->
    <!-- <script src="bower_components/jquery/dist/jquery.min.js"></script>

    Bootstrap Core JavaScript
    <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    Metis Menu Plugin JavaScript
    <script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>

    Custom Theme JavaScript
    <script src="dist/js/sb-admin-2.js"></script> -->

</body>

</html>

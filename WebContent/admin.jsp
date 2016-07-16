<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<!DOCTYPE html>
<html lang="en">

<head>
<%
if(session==null) {
	response.sendRedirect("login.jsp");
}
%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>PUP Utility</title>
<link
	href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap.min.css" />"
	rel="stylesheet" media="screen">
<link
	href="<c:url value="/resources/bower_components/metisMenu/dist/metisMenu.min.css" />"
	rel="stylesheet" media="screen">
<link href="<c:url value="/resources/dist/css/sb-admin-2.css" />"
	rel="stylesheet" media="screen">
<link
	href="<c:url value="/resources/bower_components/font-awesome/css/font-awesome.min.css" />"
	rel="stylesheet" media="screen">
	
	<%-- <link
	href="<c:url value="/resources/bower_components/jquery/jquery.min.css" />"
	rel="stylesheet" media="screen"> --%>
	
	<link
	href="<c:url value="/resources/bower_components/jquery/jquery-ui.theme.min.css" />"
	rel="stylesheet" media="screen">
	<link
	href="<c:url value="/resources/bower_components/jquery/jquery-ui.structure.min.css" />"
	rel="stylesheet" media="screen">
	
	
	
	
<%-- 	<link href="<c:url value="/resources/bower_components/bootstrap/dist/css/bootstrap-datetimepicker.css" />" rel="stylesheet">
		<script src="/resources/bower_components/bootstrap/dist/js/bootstrap-datetimepicker.js"></script> --%>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div id="wrapper">

		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="<c:url value="/DashboardController" />">PUP
					Utility</a>
			</div>
			<!-- /.navbar-header -->

			<ul class="nav navbar-top-links navbar-right">

				<!-- /.dropdown -->

				<!-- /.dropdown -->

				<!-- /.dropdown -->
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
						<i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-user">

						<!-- <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
						</li> -->
						<li class="divider"></li>
						<li><a href="<c:url value='/LogoutController' />"><i class="fa fa-sign-out fa-fw"></i>
								Logout</a></li>
					</ul> <!-- /.dropdown-user --></li>
				<!-- /.dropdown -->
			</ul>
			<!-- /.navbar-top-links -->

			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">

						<%-- <li><a href="<c:url value="/admin" />"><i
								class="fa fa-dashboard fa-fw"></i> Dashboard</a></li> --%>

						<li><a href="<c:url value="/DashboardController" />"><i
								class="fa fa-table fa-fw"></i> Excel Import</a></li>

					</ul>
				</div>
				<!-- /.sidebar-collapse -->
			</div>
			<!-- /.navbar-static-side -->
		</nav>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">PUP Excel Import Form</h1>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
				
				<div class="alert alert-success" id="successMsg">
                                
                </div>
                
                <div class="alert alert-danger" id="errorMsg">
                                
               </div>
                            
					<div class="panel panel-default">
						<div class="panel-heading"></div>
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-6">
									<form role="form" name="xlsImportForm" id="xlsImportForm" method="post" enctype="multipart/form-data">
										<div class="form-group">
											<label>File input</label> <input type="file" name="xlsFile" id="xlsFile">
										</div>
											
										<!-- <div class="form-group">
											<label>Date Date</label><input type="date" name="dataDate" id="dataDate">
										</div> -->
										
										<p>Date Date: <input type="text" id="datepicker" name="dataDate"></p>
										


										<button type="button" class="btn btn-primary btn-lg" id="xlsImportBtn">Start
											Import</button>
											<br>
											<div class="alert alert-warning">
                               <B>*Please make copy of projects before import.</B>
                            </div>

									</form>
								
								
									<!-- progress bar -->
								
										<!-- div class="progress-bar progress-bar-success"
											role="progressbar" aria-valuenow="40" aria-valuemin="0"
											aria-valuemax="100" style="width: 40%">40% Complete</div-->
											<div id="loader"><img src="<c:url value="/resources/images/loader.gif" />"/></div>
									

									<!-- end progress bar -->

								</div>
							</div>
							<!-- /.col-lg-6 (nested) -->
						</div>
						<!-- /.row (nested) -->
					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			
			<!-- eeeeee -->
			<B><U><a href="<c:url value='/DownloadFileServlet' />" id="errorLogDownloadLink">Download Error Logs</a></U></B>
			<table class="table" id="errorLogsTbl">
                                   
                                </table>
			<!-- /.col-lg-12 -->
		</div>
		<!-- /.row -->
	</div>
	<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	<script
		src="<c:url value="/resources/bower_components/jquery/dist/jquery.min.js" />"></script>  
		<script
		src="<c:url value="/resources/bower_components/jquery/dist/jquery-ui.min.js" />"></script>
	<script
		src="<c:url value="/resources/bower_components/bootstrap/dist/js/bootstrap.min.js" />"></script>
	<script
		src="<c:url value="/resources/bower_components/metisMenu/dist/metisMenu.min.js" />"></script>
	<script src="<c:url value="/resources/dist/js/sb-admin-2.js" />"></script>

	
	<script type="text/javascript">
	
	$( document ).ready(function() {
	    console.log( "ready!" );
	    $( "#loader" ).hide();
	    $( "#successMsg" ).hide();
	    $( "#errorMsg" ).hide();
	    $( "#errorLogsTbl" ).hide();
	    $("#errorLogDownloadLink").hide();
	    

        $(function () {
        	 $( "#datepicker" ).datepicker({ dateFormat: 'yy-mm-dd' });
        });
    
      //  $("#your_elements_id").datepicker({ dateFormat: 'yyyy-mm-dd' }); 
	    
	});
	
	
	/*$.ajax({
	    url: webUrl+methodName,
        data: formData,
        processData: false,
        type: 'POST',
		cache:false,
        dataType: "json",  
        contentType: false, 
	    success: function(responseData) {
			console.log('responseData: '+responseData);
   	
		}	
		,
	    error: function (responseData) {
	        console.log('POST failed.');
	    }
	  });*/
	
	
	$( "#xlsImportBtn" ).click(function() {
		
		 $( "#errorMsg" ).text('');
		 $( "#successMsg" ).text('');
		 $( "#errorLogsTbl" ).html('');
		 $( "#successMsg" ).hide();
		 $( "#errorMsg" ).hide();
		 
		 
			  //alert( "Handler for .click() called." );
			var formData = new FormData($( "#xlsImportForm" )[0]);
			//formData.append("dataDate",$("#datepicker").val());
			  //var data=$( "#xlsImportForm" ).serialize();
			  $.ajax({
				    
				    method:'POST',
				    url: '<c:url value="/ImportXlsToP6Controller" />',
				    crossDomain: true,
				    headers: { 'Access-Control-Allow-Origin': '*' },
				    data: formData,
				    dataType: 'json',
				    contentType: false,
				    processData: false,
				    cache: false,
				    beforeSend: function(){
				        // Handle the beforeSend event
				        
				        $( "#xlsImportBtn" ).hide();
				    	$( "#loader" ).show();
				      },
				    success: function(responseData) {
				    	//reset form fields
				    	$('#xlsImportForm')[0].reset();
				    	//alert(responseData);
				    	 $( "#xlsImportBtn" ).show();
				  //  alert(responseData.errorCode);
				    $( "#loader" ).hide();
				    if(responseData.errorCode==100) {
				    	 $( "#successMsg" ).show();
				    	 $( "#successMsg" ).text(responseData.erroMsg);
				    	 $("#errorLogDownloadLink").hide();
				    }
				    
				    if(responseData.errorCode==107) {
				    	 $( "#errorMsg" ).show();
				    	 $( "#errorMsg" ).text(responseData.erroMsg);
				    	
				    	 //In this case show all the error logs in data grid
				    	 var tblBodyPart1="<thead><tr> <th>Project ID</th><th>Activity ID</th><th>Resource ID</th><th>Actual Hrs</th><th>Rem Hrs</th> <th>Rem Units/Time</th><th>Remark</th></tr></thead><tbody>";
				    	 var tblRow="";
				    	 /*<tbody>
                         <tr class="danger">
                             <td>4</td>
                             <td>John</td>
                             <td>Smith</td>
                             <td>@jsmith</td>
                               <td>John</td>
                             <td>Smith</td>
                             <td>@jsmith</td>
                         </tr>
                     </tbody>*/
				    	  for(var i=0;i<responseData.xlsDataBeans.length;i++){
				    		  var obj = responseData.xlsDataBeans[i];
				    		   tblRow=tblRow+"<tr class='danger'>"+" <td>"+obj['projectId']+"</td>"+" <td>"+obj['activityId']+"</td>"+" <td>"+obj['resourceId']+"</td>"+" <td>"+obj['actualUnits']+"</td>"+" <td>"+obj['remainingUnits']+"</td>"+" <td>"+obj['remainingUnitsPerTime']+"</td>"+" <td>"+obj['remarks']+"</td> </tr>";
				    	  }
				    	 var tblData=tblBodyPart1+tblRow+" </tbody>";
				    	   $( "#errorLogsTbl" ).show();
				    	   $( "#errorLogsTbl" ).html(tblData);
				    	   
				    	   $("#errorLogDownloadLink").show();
				    	 
				    }
				    
				    if(responseData.errorCode==101) {
				    	 $( "#errorMsg" ).show();
				    	 $( "#errorMsg" ).text(responseData.erroMsg);
				    }
			        if(responseData.errorCode==103) {
				    	 $( "#errorMsg" ).show();
				    	 $( "#errorMsg" ).text(responseData.erroMsg);
				    }
			        if(responseData.errorCode==105) {
				    	 $( "#errorMsg" ).show();
				    	 $( "#errorMsg" ).text(responseData.erroMsg);
				    }
				     
				    },
				    error: function (responseData) {
				    	//alert("error = "+responseData);
				    	 $( "#xlsImportBtn" ).show();
				    //	alert(responseData.errorCode);
				        $( "#loader" ).hide();
				       
				        
				        return false;
				    }
				  });
			  
			  
			  
			  
			});
	
	
	</script>
</body>

</html>

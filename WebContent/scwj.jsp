<%@ page language="java" pageEncoding="UTF-8"%>


<!doctype html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>文件上传服务器</title>
	
		<script type="text/javascript">
			function checkForm(form){
			var flag=validateFile();
			
			
		
			if(!flag){
				alert("您还没有选择上传文件");
				return false;
			}else{
				return true;
			}
		
		
	}
	function validateFile(){
		var tmp=document.getElementById("file1").value;
	//	var tmpStr=tmp.IndexOf("//");
		if(tmp==null||tmp==""){
			return false;
		}else{
			return true;
		}
		
	}
	
	
		</script>
		
	</head>
	
	<body>
	<form name="demo" action="doUpload.do" enctype="multipart/form-data" method="POST" >
<div id="J_TabCase">
	<input type="file" name="file1" id="file1" >
	
	<input type=submit  name="submit1" id="jia" value="上传" onClick="return checkForm(this.form);"/>
		
</div>
<div>
	
</div>
		</form>

</html>
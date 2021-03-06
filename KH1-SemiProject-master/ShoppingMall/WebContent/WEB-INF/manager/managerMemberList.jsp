<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= ctxPath %>/css/style.css" />
<title>managerMember.jsp</title>
<!-- 차트 링크 --> 
<script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script> 
<style type="text/css">
	.sideMenu{
		margin-top:10px;
		display: inline-block;
		width: 150px;
		float:left;
	}
	.memberList{
		display:inline-block;
		width:800px;
		margin-top: 10px;
	}
	
	.member-table{
		width: 100%;
		text-align: center;
	}
	
	.member-search{
		width:100%;
		margin-bottom:5px;
	}
	
	.member-count{
		float: right;
	}
	
	.board-title{
		width: 400px;
	}
	
	.type{
		border:solid 1px purple;
		margin-left:10px;
		padding:10px 20px;
		color:purple;
	}
	
	.type:hover{
		cursor: pointer;
		background-color: purple;
		color:white;
	}
	
	
</style>
<!-- 부트스트랩 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/ShoppingMall/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/ShoppingMall/util/myutil.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		
	});
	
	function func_pop(){
		window.name="parentFrm";
		sessionStorage.setItem("recieve","이주명");
		console.log(sessionStorage.getItem("recieve"));
		var win = window.open("include/popup.jsp","childFrm","left=100px, top=100px, width=400px, height=350px");
	}
	
</script>
</head>
<body>
	<div class="Mycontainer">
		<jsp:include page="../include/header.jsp"></jsp:include>
		<div class="section" align="center">
			<div class="contents">
				<div class="sideMenu">
					<jsp:include page="../include/managerSide.jsp"></jsp:include>
				</div>
				<div class="memberList" align="left">
					<div class="member-search">
						<h4>회원관리</h4>
						검색 : <input type="text" />
						<select>
							<option>유저명</option>
							<option>id</option>
							<option>주소</option>
						</select>
						<span class="member-count">전체 회원 수 : </span>
					</div>
					<table class="table member-table" style="border-top:solid 2px purple;">
						<tr>
							<th>선택</th>
							<th>No.</th>
							<th>유저명</th>
							<th>id</th>
							<th class="board-title">주소</th>
							<th>모바일</th>
						</tr>
						<tr>
							<td><input type="checkbox" /></td>
							<td>No.</td>
							<td>유저명</td>
							<td>id</td>
							<td class="board-title">주소</td>
							<td>모바일</td>
						</tr>
					</table>
				</div>
				<div style="clear:both;"></div>
				<div class="managerBtn" align="right">
					<span class="type" onclick="func_pop()">선택 탈퇴</span> <span class="type" onclick="func_pop()">선택 경고</span>
				</div>
				<div class="paging">
					
				</div>
			</div>
		</div>
		<jsp:include page="../include/footer.jsp"></jsp:include>
	</div>
</body>
</html>
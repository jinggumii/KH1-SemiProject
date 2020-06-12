<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>header.jsp</title>
<style type="text/css">
	/*로고와 링크가 있는 영역*/
	.logo_login{
		width:1080px;
		margin: 10px auto;
		border:solid 0px black;
		background-color: white;
	}
	
	/*회원가입, 로그인 링크 영역*/
	.loginLink{
		float:right;
	}
	
	
	.loginLink a{
		text-decoration: none;
		color: gray;
	}
	
	
	.loginLink a:hover{
		color: purple;
	}
	
	/*고객센터 영역*/
	.serviceCenter{
		position: relative;
	}
	
	.underIcon{
		font-size: 8pt;
		padding: 0px;
	}
	
	/*고객센터 하위의 메뉴영역*/
	.serviceCenter-dropdown-content{
		display:none;
		position: absolute;
		z-index: 5;
		background-color: white;
		border:solid 1px black;
	}
	
	/*고객센터 하위의 메뉴가 되는 ul*/
	.serviceCenter-categori{
		display:inline-block;
		list-style: none;
		padding: 0px;
		text-align: left;
		margin: 0px;
		float:left;
	}
	
	.serviceCenter-categori .listType{
		display: block;
		margin: 0px 10px;
		color:black;
		font-size: 8pt;
	}
	
	/*로고 이미지가 있는 영역*/
	.logo{
		width: 300px;
		border:solid 1px gray;
		clear:both;
	}
	
	/*상단에 고정해야 하는 navigation이 있을 영역*/
	.header-navi{
		max-width:1700px;
		min-width:1080px;
		border:solid 0px black;
		margin: 0 auto;
		z-index: 4;
		background-color: white;
	}
	
	/*navigation이 되는 ul태그*/
	.header-naviList{
		list-style: none;
		border:solid 0px blue;
		margin-top: 10px;
		margin-bottom: 0;
	}
	
	.header-naviList > li{
		text-align: center;
		display: inline-block;
		border: solid 0px gray;
		
	}
	
	/*각각의 navigation을 구분하는 I(span태그)*/
	.bar{
		font-size: 8pt;
		color:gray;
		
	}
	
	/*navi에 들어가는 li태그 안의 영역(span)*/
	.navi-categori .listType, .header-naviList .listType {
		display: inline-block;
		margin: 0px 10px;
		font-weight: bold;
		color:black;
		font-size: 12pt;
		width: 150px;
		height: 30px;
	}
	
	.listType:hover{
		color:purple;
		text-decoration:underline;
		cursor: pointer;
	}
	
	/*navi에 들어가는 li태그에서 input태그가 있는 영역(span)*/
	.search{
		width: 200px;
	}
	
	/*navi에 들어가는 li태그에서 아이콘(장바구니)이 들어가 있을 영역(span)*/
	.icon{
		width: 120px;
	}
	
	
	/*전체 카테고리 li*/
	.navi-dropdown{
		position: relative;
	}
	
	/*전체 카테고리 하위의 navi영역*/
	.navi-dropdown-content{
		display:none;
		position: absolute;
		z-index: 5;
		min-width:150px;
		background-color: white;
		border:solid 1px black;
	}
	
	/*전체 카테고리 하위의 navi가 되는 ul*/
	.navi-categori{
		width:149px;
		display:inline-block;
		list-style: none;
		padding:0px;
		text-align: left;
		margin: 0px;
		float:left;
	}
	
	/*하위 navi가 되는 ul 두번째*/
	.navi-categori2{
		width:149px;
		display:none;
		list-style: none;
		padding:0px;
		text-align: left;
		background-color: #f1f1f1;
	}
	
	/*하위 navi에 들어가는 li*/
	.navi-categori > li, .navi-categori2 > li{
		line-height: 30px;
	}
	
	/*상단 navi가 고정되도록 하는 css*/
	.scroll_fixed{
    	position: fixed;
    	top:0px;
    	
	}
	
	/*첫번째 하위 navi 안에 있는 li태그에 hover 했을 때 그 li태그의 배경색 변경*/
	.navi-categori .list:hover{background-color: #f1f1f1;}
	
</style>

<script type="text/javascript">
	$(document).ready(function(){
		var $list = $(".list"); //하위 navi에 존재하는 li태그들 (배열)
		
		// 하위 navi에 존재하는 li태그에 hover했을 때 function
		$(".list").hover(function(){ //마우스를 올렸을 때
			$(".navi-dropdown-content").css("min-width","300px"); //하위 navi가 존재하는 영역의 넓이 300px로 조정
			$(".navi-categori2").css("display","inline-block"); //navi-categori 옆에 붙을 navi-categori2(ul)태그의 display변경
			
		},function(){ //마우스를 내렸을 때
			$(".navi-dropdown-content").css("min-width","150px"); //하위 navi가 존재하는 영역의 넓이 150px로 조정
			$(".navi-categori2").css("display","none"); //navi-categori2(ul)태그의 display none(안보이도록)
		});
		
		
		// 전체카테고리 li태그에 hover했을 때 function
		$(".navi-dropdown").hover(function(){
			$(".navi-dropdown-content").css("display","block"); //하위 navi가 존재하는 영역 display 변경
		},function(){
			$(".navi-dropdown-content").css({"display":"none","min-width":"150px"}); //원래 있던대로 display와 width 수정
		});
		
		// 고객센터 span태그에 hover했을 때 function
		$(".serviceCenter-dropdown").hover(function(){
			$(".serviceCenter-dropdown-content").css("display","block"); //하위 navi가 존재하는 영역 display 변경
		},function(){
			$(".serviceCenter-dropdown-content").css({"display":"none"}); //원래 있던대로 display와 width 수정
		});
		
		
		// === 상단 navi 스크롤 사용 시와 브라우저 가로 길이 변경했을 때 고정하도록 하는 내용 === //
		
		var naviPosition = $(".header-navi").offset(); //문서가 로딩 되었을 때 상단 navi가 위치하는 위치값을 변수에 대입
		
		//스크롤 이동할 때 실행되는 function
		$(window).scroll(function(){ 
			var width = $(".header-navi").css("width"); //스크롤을 움직였을 때 상단 navi의 width값 변수에 대입
			
			if($(document).scrollTop()>naviPosition.top){ //스크롤을 움직여 나온 top의 값이 실제 상단 navi의 top값 보다 클 경우(상단 navi가 화면에서 안보여질 때)
				$(".header-navi").addClass("scroll_fixed"); //상단 navi에 fix기능을 하는 css를 갖는 class(scroll_fixed) 를 추가
				$(".scroll_fixed").css("width",width); //기존에 있던 width그대로 고정
				
			}
			else{ //반대인 경우(상단 navi가 화면에 보여질 때)
				$(".header-navi").removeClass("scroll_fixed"); //fix기능을 하는 css를 갖는 class(scroll_fixed)제거
				$(".header-navi").removeAttr('style'); //고정으로 주고 있었던 css를 리셋
				
			}
		});
		
		// 브라우저의 가로길이에 변화가 있을 때 실행되는 function
		$(window).resize(function(){
			var width = $(".header").css("width"); //header(로고+링크)영역의 width값 변수에 대입
			$(".scroll_fixed").css("width",width); //해당 width값을 scroll_fixed(상단navi)클래스에 적용하는 css에 추가
		});
	});
</script>
</head>
<body>
	
	<div class="logo_login" align="center">
		<div class="loginLink"> 
			<a href="#">회원가입</a> | <a href="#">로그인</a> | 
			<div class="serviceCenter-dropdown" style="display:inline-block;">
				<a href="#">고객센터</a> <span class="underIcon">▼</span>
				<div class="serviceCenter-dropdown-content" align="left">
					<ul class="serviceCenter-categori">
						<li class="list"><span class="listType">공지사항</span></li>
						<li class="list"><span class="listType">자주하는 질문</span></li>
						<li class="list"><span class="listType">1:1문의</span></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="logo">
			<a href="index.jsp"><img src = "/ShoppingMall/include/images/logo.png" /></a>
		</div>	
	</div>
	<div class="header-navi" align="center" >
		<ul class="header-naviList" style="border-bottom:solid 1px purple;">
			<li class="navi-dropdown">
				<span class="listType dropbtn">전체 카테고리</span>
				<span class="bar">I</span><br/>
				<div class="navi-dropdown-content" align="left">
					<ul class="navi-categori">
						<li class="list"><span class="listType">채소1</span></li>
						<li class="list"><span class="listType">채소2</span></li>
						<li class="list"><span class="listType">채소3</span></li>
						<li class="list"><span class="listType">채소4</span></li>
						<li class="list"><span class="listType">채소5</span></li>
					</ul>
					<ul class="navi-categori2">
						<li class="list"><span class="listType">채소6</span></li>
						<li class="list"><span class="listType">채소7</span></li>
						<li class="list"><span class="listType">채소8</span></li>
						<li class="list"><span class="listType">채소9</span></li>
						<li class="list"><span class="listType">채소10</span></li>
					</ul>
				</div>
			</li>
			<li><a><span class="listType">알뜰쇼핑</span></a><span class="bar">I</span></li>
			<li><a><span class="listType">신상품</span></a><span class="bar">I</span></li>
			<li><a><span class="listType">추천쇼핑</span></a></li>
			<li><span class="search"><input type="text" placeholder="test"/></span></li>
			<li><span class="">장바구니 아이콘</span></li>
		</ul>		
	</div>
</body>
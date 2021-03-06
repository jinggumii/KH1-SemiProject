<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= ctxPath %>/css/style.css" />
<title>index.jsp</title>
<style type="text/css">
	table, td {
	border: solid 2px gray;
	border-collapse:collapse; 
	
	}
	
	#shoppingBasket {
		border-top: solid 3px purple;
	}
	
	.goodsImg{
		width: 60px;
		height: 70px;
	}
	
</style>
<!-- 부트스트랩 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/ShoppingMall/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/ShoppingMall/util/myutil.js"></script>
<script type="text/javascript">
	var arrFood = [{name:"루비 싱글 바",filename:"iscream.png",price:"3600"}
	,{name:"병 샐러드",filename:"salad.png",price:"6200"}
	,{name:"동물복지 우유",filename:"milk.png",price:"2650"}];
	
	$(document).ready(function(){
	// 선택한 목록 테이블에 넣어주기
	
	var html = "";
	for(var i=0; i<arrFood.length; i++) {
	html += "<tr> <td>"+
			"<input type='checkbox' class='singleCheck' id='food"+[i]+"' onClick='choiceFood()' ></input>"+
				"<img src='images/"+arrFood[i].filename+"' class='goodsImg'/>"+
			"</td> <td>"+
				"<label>"+arrFood[i].name+"</label>"+
			"</td> <td>"+
				"<input type='number' class='foodOrdercnt' name='foodOrdercnt' min='1' max='99' step='1' value='1' required onchange='choiceCnt()' />"+
			"</td> <td>"+
				"<label class='cost'>"+arrFood[i].price+"</label>"+
			"</td> </tr> ";
	}
	$("#basket").html(html);
	
	// 전체 다 체크된 상태로 시작
	$("input:checkbox[class=singleCheck]").prop("checked", true);
	$("input:checkbox[class=allCheck]").prop("checked", true);
	choiceFood();
	
	
	
	// == 체크박스 전체선택/ 전체해제 == //
	$(document).on("click",".allCheck",function(){
	var bool = $(this).prop("checked");
	
	$("input:checkbox[class=singleCheck]").prop("checked", bool);
	$("input:checkbox[class=allCheck]").prop("checked", bool);
	
	choiceFood();
	}); 
	
	
	// == 체크박스 전체선택 / 전체해제 에서
	//    하위 체크박스에 체크가 1개라도 체크가 해제되면 체크박스 전체선택/전체해제 체크박스도 체크가 해제되고
	//    하위 체크박스에 체크가 모두 체크가 되어지지면  체크박스 전체선택/전체해제 체크박스도 체크가 되어지도록 하는 것 == //
	$(document).on("click",".singleCheck",function(){  
	var bFlag = false;
	
	$("input:checkbox[class=singleCheck]").each(function(){
	var bool = $(this).prop("checked");
	if(!bool) {
		$("input:checkbox[class=allCheck]").prop("checked", false);
		bFlag = true;
		return false; //continue;
	}
	});
	if(!bFlag)
	$("input:checkbox[class=allCheck]").prop("checked", true); 
	}); 
	
	
	}); // end of $(document).ready(function(){})---------------------------------
	
	
	// 가격 표시하기
	var arrCost = document.getElementsByClassName("cost");
	var arrFoodOrdercnt = document.getElementsByClassName("foodOrdercnt");
	
	function choiceCnt() {
	
	var sumCost = 0;
	var cnt = 0;
	var price = 0;
	
	for(var i=0; i<arrCost.length; i++){
	var bChecked = document.getElementById('food'+[i]+'').checked;
	
	if(arrFoodOrdercnt[i].value != "1") {
		cnt = Number( arrFoodOrdercnt[i].value );
		price = Number ( arrFood[i].price );
		
		sumCost = cnt*price;
		
		arrCost[i].innerText = sumCost;
	}
	else {
		arrCost[i].innerText = arrFood[i].price;
	}
	
	}
	}
	
	
	function choiceFood() {
		// 상품금액에 합산금액 나타내기
		var arrCost = document.getElementsByClassName("cost");
		var totalSum = 0;
		
		for(var i=0; i<arrFood.length; i++) {
		
			var bChecked = document.getElementById('food'+[i]+'').checked;
			
			if(bChecked) {
			totalSum += Number( arrCost[i].innerText );
			}
		
		}
		
		var to = "<label>"+totalSum+"원</label>";
			
		$("#price").html(to);
			
		// 합산금액과 배송비합친 결제예정금액 나타내기
		var arrCost = document.getElementsByClassName("cost");
		var totalSum = 0;
		
		for(var i=0; i<arrFood.length; i++) {
			var bChecked = document.getElementById('food'+[i]+'').checked;
		
			if(bChecked) {
				totalSum += Number( arrCost[i].innerText );
			}
		
		}
		
		if(totalSum != 0) {
			totalSum = totalSum + 3000;
			
			var del = "<label>3000원</label>";
			
			$("#delivery").html(del);
		} else {
			var del = "<label>0원</label>";
			
			$("#delivery").html(del);	
		}
		
		var total = "<label>"+totalSum+"원</label>";
		$("#totalPrice").html(total);
	
	}
	
	// 상품 삭제하기
	function cancelProduct() {
		for(var i=0; i<arrFood.length; i++) {
			var bChecked = document.getElementById('food'+[i]+'').checked;
			if(bChecked) {
				alert("["+arrFood[i].name+"] 상품을 삭제하기를 누르셨습니다")
			}
		}
	}
</script>
</head>
<body>
	
	<div class="Mycontainer">
		<jsp:include page="../include/header.jsp"></jsp:include>
		<div class="section" align="center">
			<div class="contents">
				<h1 id="title">장바구니</h1>
				<h5>주문하실 상품명 및 수량을 정확하게 확인해 주세요.</h5>
				<form name="frmData">
					<table id="shoppingBasket" class="table">
					<tr>
						<td class="td1">
							<input type="checkbox" class="allCheck"/><label>전체 선택</label>
						</td>
						<td>
							상품정보
						</td>
						<td>
							수량
						</td>
						<td>
							상품금액
						</td>
					</tr>
					
					<tbody id="basket">
						
					</tbody>
					
					<tr>
						<td>
							<input type="checkbox" class="allCheck"/><label>전체 선택</label>
							<input type="button" onClick="cancelProduct();" value="선택 삭제"/>
						</td> 
					</tr>
					</table>
					<table id="total" class="table">
						<tr>
							<td>
								상품금액
							</td>
							<td rowspan="2">+</td>
							<td>
								배송비
							</td>
							<td rowspan="2">=</td>
							<td>
								결제예정금액
							</td>
						</tr>
							<td id="price">
								<label>0원</label>
							</td>
							<td id="delivery">
								<label>0원</label>
							</td>
							<td id="totalPrice">
								<label>0원</label>
							</td>
						<tr align="center">
							<td colspan="5">
								<input type="button" value="주문하기" onClick="order();"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		
		<jsp:include page="../include/footer.jsp"></jsp:include>
		
	</div>
</body>
</html>
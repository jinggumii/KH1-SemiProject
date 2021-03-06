package main.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import common.controller.AbstractController;
import main.model.IndexDAO;
import main.model.InterIndexDAO;
import member.model.MemberVO;

public class InBasketAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		boolean check = true;
		String message="";
		int n = 0;
		
		// 로그인 유무 확인
		boolean loginFlag = super.checkLogin(request);
		if(!loginFlag) {
			String goBackURL = request.getContextPath()+"/detail.do?product_num="+request.getParameter("product_num");
			session.setAttribute("goBackURL", goBackURL);
			message = "로그인이 필요한 기능입니다.";
			n=-1;
		}
		else {
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

			Map<String, String> orderMap = new HashMap<String, String>();
			orderMap.put("product_num", request.getParameter("product_num"));
			orderMap.put("price", request.getParameter("price"));
			orderMap.put("count", request.getParameter("count"));
			orderMap.put("member_num",String.valueOf(loginuser.getMember_num()));
			
			// 유저가 이미 해당 상품을 장바구니에 담았는지 확인하고 없으면 장바구니에 담고 있으면 담지 않는다.
			InterIndexDAO dao = new IndexDAO();
			check = dao.basketSelect(orderMap);
			
			if(check) {
				 n = dao.basketInsert(orderMap);
				if(n==1) message="상품을 장바구니에 담았습니다.";
				else message="상품을 장바구니에 담는 도중 오류가 발생했습니다.";
			}else {
				message="이미 동일한 상품이 장바구니에 담아져 있습니다.";
			}
		}
		
		
		JSONObject jobj = new JSONObject();
		jobj.put("message", message);
		jobj.put("flag", n);
		String json = jobj.toString();
		
		request.setAttribute("json", json);
		super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}

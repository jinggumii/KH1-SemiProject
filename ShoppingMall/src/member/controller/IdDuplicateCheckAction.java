package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.model.*;

public class IdDuplicateCheckAction extends AbstractController {
   
   @Override
   public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      String userid =  request.getParameter("userid");  
      InterMemberDAO memberdao = new MemberDAO();
      
      boolean isUse = memberdao.idDuplicateCheck(userid);
      
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("isUse", isUse);			// isUse: true/false 
      
      String json = jsonObj.toString();		// -> toString() => {"isUse":true}
      request.setAttribute("json", json);	
      
      super.setViewPage("/WEB-INF/jsonview.jsp");
   }

}
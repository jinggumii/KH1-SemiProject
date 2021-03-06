package service.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import main.model.EncryptMyKey;
import main.model.FAQtableVO;
import main.model.NoticeVO;
import main.model.OneInquiryVO;
import member.model.MemberVO;
import my.util.MyUtil;
import util.security.AES256;

public class ServiceDAO implements InterServiceDAO {

	private DataSource ds; 
	// DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다. 
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes = null;
	
	// 생성자 
	public ServiceDAO() {
		// 암호화/복호화 키 (양방향암호화) ==> 이메일,휴대폰의 암호화/복호화
		String key = EncryptMyKey.KEY;
		
		try {
		    Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/semiProject");
			aes = new AES256(key);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	
	
	// 사용한 자원을 반납하는 close() 메소드 생성하기
	public void close() {
		try {
			if(rs != null) { rs.close(); rs=null; }
			if(pstmt != null) { pstmt.close(); pstmt=null; } 
			if(conn != null) { conn.close(); conn=null; }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 공지사항 리스트 조회
	@Override
	public List<NoticeVO> selectPagingMember(HashMap<String, String> paraMap) throws SQLException {
		List<NoticeVO> noticeList = new ArrayList<NoticeVO>();
		try {		
				conn = ds.getConnection();
				String[] searchTypeArr = null;
				String sql = " select RNO, notice_num, subject, content, write_date, hits "
							+" from "
							+" ( select rownum AS RNO, notice_num, subject, content,  write_date, hits "
							+"   from "
							+"     ( select notice_num, subject, content, to_char(write_date,'yyyy-mm-dd')as write_date, hits "
							+"       from notice_table ";
							
				String searchWord = paraMap.get("searchWord");
				String searchType = paraMap.get("searchType");
					
				if(searchWord != null && !searchWord.trim().isEmpty()) {      
			            if(searchType.indexOf(",")==-1) {
			            	sql += " where "+searchType+" like '%'||?||'%' "; 
			            }
			            else {
			            	searchTypeArr = searchType.split(",");
			            	sql += " where "+searchTypeArr[0]+" like '%'||?||'%' "; 
			            	if(!searchTypeArr[1].trim().isEmpty()) sql += " or "+searchTypeArr[1]+" like '%'||?||'%' ";
			            }
			                             
			    }
			         
			    sql +=    "       order by notice_num desc "
			           +"     )V "
			           +" )T "
			           +" where T.RNO between ? and ? ";     				
				
				pstmt = conn.prepareStatement(sql);	
				
				System.out.println("sql:"+sql);
				
				int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
				int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
				
				if(searchWord != null && !searchWord.trim().isEmpty()) {	
					if(searchTypeArr == null) {
						pstmt.setString(1, searchWord);
						pstmt.setInt(2, (currentShowPageNo*sizePerPage)-sizePerPage+1);
						pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
					}
					else if(searchTypeArr[1].trim().isEmpty()) {
						pstmt.setString(1, searchWord);
						pstmt.setInt(2, (currentShowPageNo*sizePerPage)-sizePerPage+1);
						pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
					}
					else {
						pstmt.setString(1, searchWord);
						pstmt.setString(2, searchWord);
						pstmt.setInt(3, (currentShowPageNo*sizePerPage)-sizePerPage+1);
						pstmt.setInt(4, (currentShowPageNo*sizePerPage) );
					}
					
					
				}else {
					
					pstmt.setInt(1, (currentShowPageNo*sizePerPage)-sizePerPage+1);
					pstmt.setInt(2, (currentShowPageNo*sizePerPage) );
				}
			
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					NoticeVO nvo = new NoticeVO();
					nvo.setNotice_num(rs.getInt("notice_num"));
					nvo.setSubject(rs.getString("subject"));
					nvo.setContent(rs.getString("content"));
					nvo.setWrite_date(rs.getString("write_date"));
					nvo.setHit(rs.getInt("hits"));
					noticeList.add(nvo);
				}
				
		}
		finally {
			close();
		}
		return noticeList;
	}


	// 공지사항 게시글 수 조회
	@Override
	public int getTotalPage(HashMap<String, String> paraMap) throws SQLException {
		int totalPage = 0;
		
		try {
			conn=ds.getConnection();
			
			String sql = " select ceil(count(*)/?) as totalPage "
						+" from notice_table ";
			
			String searchWord = paraMap.get("searchWord");
			String searchType = paraMap.get("searchType");
			String[] searchTypeArr = null;
			
			
			if(searchWord !=null && !searchWord.trim().isEmpty()) {		
				if(searchType.indexOf(",")!=-1) {
					searchTypeArr = searchType.split(",");
					sql += " where "+searchTypeArr[0]+" like '%'||?||'%'";
					if(!searchTypeArr[1].trim().isEmpty()) sql += " or "+searchTypeArr[1]+" like '%'||?||'%' ";
				}
				
				else {
						sql += " where "+searchType+" like '%'||?||'%'";
				}	
										
			}
			
			
			pstmt = conn.prepareStatement(sql);
			
			
			if(searchWord !=null && !searchWord.trim().isEmpty()) {		
				
				if(searchTypeArr == null) {
					pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
					pstmt.setString(2, searchWord);
				}
				else if(searchTypeArr[1].trim().isEmpty()) {
					pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
					pstmt.setString(2, searchWord);
				}
				else {
					pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
					pstmt.setString(2, searchWord);
					pstmt.setString(3, searchWord);
				}
								
			}else {
				pstmt.setInt(1, Integer.parseInt(paraMap.get("sizePerPage")));
			}
			
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalPage = rs.getInt(1);
			
			System.out.println("totalPage : "+totalPage);
			
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			close();
		}
		
		return totalPage;
	}



	// 자주하는 질문 테이블 조회
	@Override
	public List<FAQtableVO> selectFAQ(HashMap<String, String> paraMap) throws SQLException {
		List<FAQtableVO> faqList = new ArrayList<FAQtableVO>();
		try {		
				conn = ds.getConnection();
				String sql = " select RNO, FAQ_num, subject, content, write_date, hits, fk_category_num, category_content "
							+" from "
							+" ( select rownum AS RNO, FAQ_num, subject, content,  write_date, hits, fk_category_num, category_content "
							+"   from "
							+"     ( select F.FAQ_num, F.subject, F.content, to_char(F.write_date,'yyyy-mm-dd')as write_date, F.hits, F.fk_category_num, IF.category_content "
							+"       from FAQ_table F join inquiry_category_table IF on F.fk_category_num = IF.category_num ";
							
				String searchWord = paraMap.get("searchWord");
				String category = paraMap.get("category");	
				if((searchWord != null && !searchWord.trim().isEmpty()) && (category != null && !category.trim().isEmpty())) {      
						sql += " where subject like '%'||?||'%' or content like '%'||?||'%' and fk_category_num = ? ";                          
			    }
			    
				else if(searchWord != null && !searchWord.trim().isEmpty()) {
					sql += " where subject like '%'||?||'%' or content like '%'||?||'%' ";
				}
				
				else if(category != null && !category.trim().isEmpty()){
					sql += " where fk_category_num = ? ";
				}
				
			    sql +=    "       order by FAQ_num desc "
			           +"     )V "
			           +" )T "
			           +" where T.RNO between ? and ? ";     				
				
				pstmt = conn.prepareStatement(sql);	
				
				System.out.println("sql:"+sql);
				
				int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
				int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
				
				if((searchWord != null && !searchWord.trim().isEmpty()) && (category != null && !category.trim().isEmpty())) {      
					pstmt.setString(1,searchWord);
					pstmt.setString(2, searchWord);
					pstmt.setString(3, category);
					pstmt.setInt(4, (currentShowPageNo*sizePerPage)-(sizePerPage-1));
					pstmt.setInt(5, (currentShowPageNo*sizePerPage) );
			    }
			    
				else if(searchWord != null && !searchWord.trim().isEmpty()) {
					pstmt.setString(1,searchWord);
					pstmt.setString(2, searchWord);
					pstmt.setInt(3, (currentShowPageNo*sizePerPage)-sizePerPage+1);
					pstmt.setInt(4, (currentShowPageNo*sizePerPage) );
				}
				
				else if(category != null && !category.trim().isEmpty()){
					pstmt.setString(1, category);
					pstmt.setInt(2, (currentShowPageNo*sizePerPage)-sizePerPage+1);
					pstmt.setInt(3, (currentShowPageNo*sizePerPage) );
				}
				
				else {
					pstmt.setInt(1, (currentShowPageNo*sizePerPage)-sizePerPage+1);
					pstmt.setInt(2, (currentShowPageNo*sizePerPage) );
				}
				
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					FAQtableVO fvo = new FAQtableVO();
					fvo.setCount(rs.getInt(1));
					fvo.setFaq_num(rs.getInt(2));
					fvo.setSubject(rs.getString(3));
					fvo.setContent(rs.getString(4));
					fvo.setWrite_date(rs.getString(5));
					fvo.setHits(rs.getInt(6));
					fvo.setCategory_content(rs.getString(8));
					faqList.add(fvo);
				}
				
		}
		finally {
			close();
		}
		return faqList;
	}


	// 자주하는 질문 테이블 총 게시글 수 조회
	@Override
	public int getTotalPageFAQ(HashMap<String, String> paraMap) throws SQLException {
		int totalPage = 0;
		int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));
		try {
			conn=ds.getConnection();
			
			String sql = " select ceil(count(*)/?) as totalPage "
						+" from FAQ_table ";
			
			String searchWord = paraMap.get("searchWord");
			String category = paraMap.get("category");
			
			
			if((searchWord != null && !searchWord.trim().isEmpty()) && (category != null && !category.trim().isEmpty())) {      
				sql += " where subject like '%'||?||'%' or content like '%'||?||'%' and fk_category_num = ? ";                          
			}
	    
			else if(searchWord != null && !searchWord.trim().isEmpty()) {
				sql += " where subject like '%'||?||'%' or content like '%'||?||'%' ";
			}
			
			else if(category != null && !category.trim().isEmpty()){
				sql += " where fk_category_num = ? ";
			}
			
			
			
			pstmt = conn.prepareStatement(sql);
			
			

			if((searchWord != null && !searchWord.trim().isEmpty()) && (category != null && !category.trim().isEmpty())) {      
				pstmt.setInt(1,sizePerPage);
				pstmt.setString(2, searchWord);
				pstmt.setString(3, searchWord);
				pstmt.setString(4, category);
			}
	    
			else if(searchWord != null && !searchWord.trim().isEmpty()) {
				pstmt.setInt(1,sizePerPage);
				pstmt.setString(2, searchWord);
				pstmt.setString(3, searchWord);
			}
			
			else if(category != null && !category.trim().isEmpty()){
				pstmt.setInt(1,sizePerPage);
				pstmt.setString(2, category);
			}
			else {
				pstmt.setInt(1,sizePerPage);
			}
			
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalPage = rs.getInt(1);
			
			System.out.println("totalPage : "+totalPage);
			
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			close();
		}
		
		return totalPage;
	}


	// 자주하는 질문 카테고리 조회
	@Override
	public List<Map<String, String>> getFAQcategory() throws SQLException {
		List<Map<String, String>>categoryList = new ArrayList<Map<String,String>>();
		try {
			conn = ds.getConnection();
			String sql = " select category_content, category_num  from inquiry_category_table ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Map<String,String> categoryMap = new HashMap<String, String>();
				categoryMap.put("category", rs.getString(1));
				categoryMap.put("num", rs.getString(2));
				categoryList.add(categoryMap);
			}
		}
		finally {
			close();
		}
		return categoryList;
	}


	// 1:1문의 테이블 조회
	@Override
	public List<OneInquiryVO> selectOneInquiry(Map<String, Integer> paraMap) throws SQLException {
		List<OneInquiryVO> oneInquiryList = new ArrayList<OneInquiryVO>();
		try {
			conn = ds.getConnection();
			String sql = " select RON, one_inquiry_num, subject, content, write_date, "
					   + " answer, emailFlag, smsFlag, fk_member_num, fk_order_num, category_content,"
					   + " name, mobile, email"
					   + " from"
					   + " (select rownum as RON, one_inquiry_num, subject, content, "
					   + " write_date, answer, emailFlag, smsFlag, fk_member_num, fk_order_num, category_content,"
					   + " name, mobile, email"
					   + " from"
					   + " 	(select one_inquiry_num, subject, content, to_char(write_date,'yyyy-mm-dd') as write_date, answer, "
					   + " emailFlag, smsFlag, fk_member_num, fk_order_num, fk_category_num, OC.category_content,"
					   + " M.name, M.mobile, M.email"
					   + " from one_inquiry_table O "
					   + " join one_category_table OC on O.fk_category_num = OC.category_num "
					   + " join member_table M on O.fk_member_num = M.member_num where O.fk_member_num = ? order by one_inquiry_num desc )V"
					   + " )T where T.RON between ? and ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, paraMap.get("member_num"));
			pstmt.setInt(2, paraMap.get("start"));
			pstmt.setInt(3, paraMap.get("end"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OneInquiryVO ovo = new OneInquiryVO();
				MemberVO mvo = new MemberVO();
				ovo.setOne_inquiry_num(rs.getInt(2));
				ovo.setSubject(rs.getString(3));
				ovo.setContent(rs.getString(4));
				ovo.setWrite_date(rs.getString(5));
				ovo.setAnswer(rs.getString(6));
				ovo.setEmailFlag(rs.getString(7));
				ovo.setSmsFlag(rs.getString(8));
				ovo.setFk_member_num(rs.getInt(9));
				ovo.setFk_order_num(rs.getInt(10));
				ovo.setCategory_content(rs.getString(11));
				
				
				mvo.setName(rs.getString(12));
				mvo.setMobile(aes.decrypt(rs.getString(13)));
				mvo.setEmail(aes.decrypt(rs.getString(14)));
				
				ovo.setMember(mvo);
				oneInquiryList.add(ovo);
			}
			
			
		} catch(UnsupportedEncodingException | GeneralSecurityException e) {
	         e.printStackTrace();
	         
		}
		finally {
			close();
		}
		
		return oneInquiryList;
	}


	// 관리자 전용 게시글 작성 메소드
	@Override
	public int managerWrite(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = "";
			if("board".equals(paraMap.get("boardType"))) {
				sql = " insert into notice_table (notice_num, subject, content) values(seq_notice_table.nextval,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("subject"));
				pstmt.setString(2, paraMap.get("contents"));
			}
			else {
				sql = " insert into FAQ_table(FAQ_num, subject, content, fk_category_num) values(seq_FAQ_table.nextval, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("subject"));
				pstmt.setString(2, paraMap.get("contents"));
				pstmt.setString(3, paraMap.get("Qboard_category"));
			}
			result = pstmt.executeUpdate();
		}
		finally {
			close();
		}
		return result;
	}


	// 자주하는 질문 특정 글 상세조회
	@Override
	public FAQtableVO selectOneFAQ(String faq_num) throws SQLException {
		FAQtableVO fvo = null;
		try {
			conn = ds.getConnection();
			String sql = " select FAQ_num, subject, content, hits, fk_category_num, to_char(write_date,'yyyy-mm-dd')as write_date,"
					   + " category_content from FAQ_table "
					   + "join inquiry_category_table on fk_category_num = category_num "
					   + "where FAQ_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, faq_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				fvo = new FAQtableVO();
				fvo.setFaq_num(rs.getInt(1));
				fvo.setHits(rs.getInt(4));
				fvo.setFk_category_num(rs.getInt(5));
				fvo.setWrite_date(rs.getString(6));
				fvo.setCategory_content(rs.getString(7));
				
				String subject = rs.getString(2);
				String content = rs.getString(3);
				subject = subject.replaceAll("&lt;", "<");
				subject = subject.replaceAll("&gt;", ">");
				subject = subject.replaceAll("<br>", "\r\n");
				
				content = content.replaceAll("&lt;", "<");
				content = content.replaceAll("&gt;", ">");
				content = content.replaceAll("<br>", "\r\n");
				
				fvo.setSubject(subject);
				fvo.setContent(content);
			}
		}
		finally {
			close();
		}
		return fvo;
	}


	//자주하는 질문 게시판 특정 글 수정 메소드
	@Override
	public int FAQUpdate(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " update FAQ_table set subject=?, content=?, fk_category_num=? where FAQ_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("subject"));
			pstmt.setString(2, paraMap.get("content"));
			pstmt.setString(3, paraMap.get("category_num"));
			pstmt.setString(4, paraMap.get("faq_num"));
			
			result = pstmt.executeUpdate();
		}
		finally {
			close();
		}
		
		return result;
	}


	// 자주하는 질문 게시판 특정 글 삭제
	@Override
	public int FAQDelete(String faq_num) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " delete from FAQ_table where FAQ_num = ? ";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, faq_num);
			result = pstmt.executeUpdate();
		}
		finally {
			close();
		}
		return result;
	}


	// 공지사항 특정 글 상세보기 메소드
	@Override
	public List<NoticeVO> boardDetail(String notice_num) throws SQLException {
		List<NoticeVO> nvoList = new ArrayList<NoticeVO>();
		
		try {
			conn = ds.getConnection();
			String sql = " select notice_num, subject, content, to_char(write_date,'yyyy-mm-dd')as write_date, hits from notice_table where notice_num = ? order by notice_num desc ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notice_num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				NoticeVO nvo = new NoticeVO();
				nvo.setSubject(rs.getString("subject"));
				nvo.setNotice_num(rs.getInt("notice_num"));
				nvo.setContent(rs.getString("content"));
				nvo.setWrite_date(rs.getString("write_date"));
				nvo.setHit(rs.getInt("hits"));
				nvoList.add(nvo);
			}
			
			rs.close();
			
			sql = " select notice_num, subject from notice_table where notice_num > ? order by notice_num ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notice_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				NoticeVO nvo = new NoticeVO();
				nvo.setSubject(rs.getString("subject"));
				nvo.setNotice_num(rs.getInt("notice_num"));
				nvoList.add(nvo);
			}
			rs.close();
			
			sql = " select notice_num, subject from notice_table where notice_num < ? order by notice_num desc ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notice_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				NoticeVO nvo = new NoticeVO();
				nvo.setSubject(rs.getString("subject"));
				nvo.setNotice_num(rs.getInt("notice_num"));
				nvoList.add(nvo);
			}
			rs.close();
			
		}
		finally {
			close();
		}
		return nvoList;
	}


	// 공지사항 게시판 특정 글 조회수 증가
	@Override
	public int boardHitUp(String notice_num) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " update notice_table set hits = hits+1 where notice_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notice_num);
			result = pstmt.executeUpdate();
		}
		finally {
			close();
		}
		
		return result;
	}


	// 공지사항 게시판 특정 글 수정페이지 이동
	@Override
	public NoticeVO selectOneNotice(String notice_num) throws SQLException {
		NoticeVO nvo = null;
		try {
			conn = ds.getConnection();
			String sql = " select notice_num, subject, content, to_char(write_date,'yyyy-mm-dd')as write_date, hits from notice_table where notice_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notice_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				nvo = new NoticeVO();
				nvo.setNotice_num(rs.getInt("notice_num"));
				nvo.setWrite_date(rs.getString("write_date"));
				nvo.setHit(rs.getInt("hits"));
				
				String subject= rs.getString("subject");
				String content = rs.getString("content");
				
				subject = subject.replaceAll("&lt;", "<");
				subject = subject.replaceAll("&gt;", ">");
				subject = subject.replaceAll("<br>", "\r\n");
				
				content = content.replaceAll("&lt;", "<");
				content = content.replaceAll("&gt;", ">");
				content = content.replaceAll("<br>", "\r\n");
				
				nvo.setSubject(subject);
				nvo.setContent(content);
			}
		}
		finally {
			close();
		}
		
		return nvo;
	}


	// 공지사항 특정 글 수정
	@Override
	public int boardUpdate(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " update notice_table set subject=?, content=? where notice_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("subject"));
			pstmt.setString(2, paraMap.get("content"));
			pstmt.setString(3, paraMap.get("notice_num"));
			
			result = pstmt.executeUpdate();
		}
		finally {
			close();
		}
		
		return result;
	}


	// 공지사항 특정 글 삭제
	@Override
	public int boardDelete(String notice_num) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " delete from notice_table where notice_num = ? ";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, notice_num);
			result = pstmt.executeUpdate();
		}
		finally {
			close();
		}
		return result;
	}


	


}

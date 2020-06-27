package manager.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import product.model.ProductVO;

public interface InterProductDAO {
	
	//////////////////////////////// 매니저 진하
	
	// 전체 상품 조회
	List<ProductVO> selectAllProduct() throws SQLException;

	// 검색처리
	List<ProductVO> selectPagingProduct(HashMap<String, String> paraMap) throws SQLException;

	// 전체페이지 설정
	int getTotalPage(HashMap<String, String> paraMap) throws SQLException;

	// 상품 삭제
	int productDelete(String product_num) throws SQLException;

	// 상품명 중복 확인
	boolean productNameDuplicateCheck(String productName) throws SQLException;
	
	// 대분류 카테고리 불러오기 abstract
	List<HashMap<String, String>> getCategoryList() throws SQLException;

	// 소분류 구하기
	List<HashMap<String, String>> getSubCategoryList(String fk_category_num) throws SQLException;
	

}

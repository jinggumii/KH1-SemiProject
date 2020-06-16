show user;
-- USER이(가) "SEMIORAUSER1"입니다.

-- 회원 테이블 --
create table member_table
(member_num number  not null -- 회원번호 필수입력 + 유일한 값(primary) + 시퀀스 사용
,name       varchar2(30) not null -- 성명 필수입력
,userid     varchar2(50) not null -- 유저id 필수입력 + 유일한 값
,pwd        varchar2(300) not null -- 암호 필수입력 (SHA-256 암호화 대상)
,email      varchar2(300) not null -- 이메일(암호화) 필수입력 + 유일한 값
,hp1        varchar2(30) not null -- 핸드폰 번호 앞자리(010) 필수입력
,hp2        varchar2(300) not null -- 핸드폰 번호 중간자리 필수입력 (AES-256 암호화/복호화 대상) 
,hp3        varchar2(300) not null -- 핸드폰 번호 뒷자리 필수입력 (AES-256 암호화/복호화 대상)
,postcode   varchar2(100) -- 우편번호
,address    varchar2(100) -- 주소
,detailAddress  varchar2(100) -- 상세주소
,extraAddress   varchar2(100) -- 추가주소
,gender     number(1) -- 성별
,birthyear  varchar2(20) -- 생년
,birthmonth varchar2(10) -- 생월
,birthday   varchar2(20) -- 생일
,pwd_change_date date -- 암호 수정한 날짜 
,status     number(1) default 1 -- 회원상태(일반회원, 관리자, 탈퇴자)
,constraint pk_member_table PRIMARY KEY (member_num)
,constraint uq_member_table_userid UNIQUE(userid)
,constraint uq_member_table_email unique(email)
,constraint ck_member_table_gender CHECK (gender in (1,2))
,constraint ck_member_table_status CHECK (status in(1,2,3))
);

-- 회원테이블에 사용할 시퀀스 생성 --
create sequence seq_member_table
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


-- 상품 대분류 카테고리 테이블 생성 --
create table product_category_table
( category_num  number            -- 대분류 카테고리 번호 필수+고유
, category_content  varchar2(50)  -- 번호에 해당하는 실제 표시할 내용
,constraint pk_category_num primary key (category_num)
);

-- 상품 소분류 카테고리 테이블 생성 --
create table product_subcategory_table
(subcategory_num    number  -- 소분류 카테고리 번호 필수+고유
,subcategory_content varchar2(50) -- 번호에 해당하는 실제 표시할 내용
,constraint pk_product_subcategory_table primary key (subcategory_num)
);




-- 상품 테이블 생성 --
create table product_table
(product_num    number not null -- 상품번호 필수+고유 시퀀스 사용
,product_name   varchar2(50) not null -- 상품이름 필수+고유
,price          number  not null -- 가격 필수
,stock          number not null -- 재고 필수
,origin         varchar2(50) -- 원산지
,packing        varchar2(80) -- 포장방법
,unit           varchar2(50) -- 단위
,registerdate   date default sysdate -- 등록날짜
,seller         varchar2(50) -- 판매자(관리자 모드시에 사용)
,seller_phone   varchar2(80) -- 판매자 번호(관리자 모드시에 사용)
,fk_category_num    number not null -- product_category_table에 있는 category_num을 참조하는 컬럼
,fk_subcategory_num number not null -- product_subcategory_table에 있는 subcategory_num을 참조하는 컬럼
,constraint pk_product_table primary key (product_num)
,constraint uq_product_product_name   unique (product_name)
,constraint fk_product_category_num FOREIGN key(fk_category_num) REFERENCES product_category_table(category_num)
,constraint fk_product_subcategory_num FOREIGN key(fk_subcategory_num) REFERENCES product_subcategory_table(subcategory_num)
);

-- 상품 테이블에 사용할 시퀀스 생성 --
create sequence seq_product_table
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

-- 상품 이미지와 설명 테이블 생성 --
create table product_detail_table
(fk_product_num number not null -- 상품테이블에 있는 상품번호를 참조받는 컬럼
,representative_img varchar2(100) not null -- 상품을 대표하는 이미지(리스트 및 상세에서 사용)
,image1 varchar2(100) -- 상품설명에 사용할 이미지1
,image2 varchar2(100) -- 상품설명에 사용할 이미지2
,image3 varchar2(100) -- 상품설명에 사용할 이미지3
,explain    varchar2(4000) -- 상품설명
,constraint fk_prodcut_detail_num FOREIGN key (fk_product_num) REFERENCES product_table(product_num) on DELETE CASCADE
,constraint pk_product_detail_num primary key (fk_product_num)
);


-- 상품문의 테이블 생성 --
create table product_inquiry_table
(inquiry_num    number not null     -- 상품문의 번호 필수+고유 시퀀스 사용
,subject    varchar2(50) not null   -- 제목 필수
,content    varchar2(4000) not null -- 내용 필수
,write_date date default sysdate    -- 작성날짜 
,answer     varchar2(4000)          -- 관리자의 답변(문의가 들어오면 바로 답변을 받는 것이 아니기에 null허용)
,fk_member_num  number not null     -- 회원테이블의 회원번호를 참조받는 컬럼 필수(참조하는 값이 삭제되면 따라서 삭제됨)
,fk_product_num number not null     -- 상품테이블의 회원번호를 참조받는 컬럼 필수(참조하는 값이 삭제되면 따라서 삭제됨)
,constraint pk_product_inquiry primary key(inquiry_num)
,constraint fk_inquiry_member FOREIGN key(fk_member_num) REFERENCES member_table(member_num)on delete CASCADE
,constraint fk_inquiry_product foreign key(fk_product_num) REFERENCES product_table(product_num)on delete cascade
);

-- 상품문의 테이블에 사용할 시퀀스 생성 --
create sequence seq_product_inquiry
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


-- 주문상테 테이블 생성 --
create table order_state_table
(category_num   number  not null -- 주문상태 카테고리 번호
,order_state    varchar2(50) -- 번호에 해당하는 실제 내용
,constraint pk_order_state  primary key(category_num)
);

-- 주문 정보 테이블 생성 --
create table order_table
(order_num  number  not null    -- 주문번호 필수+고유 시퀀스 사용
,order_date date default sysdate    -- 주문날짜
,recipient  varchar2(50) not null   -- 받는 사람 필수
,recipient_mobile   varchar2(100) not null  -- 받는 사람의 연락처 필수
,recipient_postcode varchar2(100) not null  -- 받는 사람의 우편번호
,recipient_address  varchar2(100) not null  -- 받는 사람의 주소
,recipient_detailaddress varchar2(100) not null -- 받는 사람의 상세주소
,recipient_extraaddress varchar2(100) not null -- 받는 사람의 추가주소
,price  number  not null    -- 주문금액 필수
,memo   varchar2(200)       -- 요청사항
,fk_member_num  number  not null    -- 회원테이블의 회원번호를 참조하는 컬럼
,fk_category_num number not null    -- 주문상태 케이블의 주문상태 번호를 참조하는 컬럼
,constraint pk_order_table  primary key(order_num)
,constraint fk_order_member FOREIGN key(fk_member_num) REFERENCES member_table(member_num)
,constraint fk_order_category foreign key(fk_category_num) references order_state_table(category_num)
);

-- 주문 테이블에 사용할 시퀀스 생성 --
create sequence seq_order_table
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

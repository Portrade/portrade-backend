INSERT INTO user(user_id, created_date, birth_date, last_modified_date, name, password, college, is_graduated, profile_url, username, wanted_job, job)
VALUES ('3cbe539a33ba4550a82c63be333ac2d0', '2019-09-28T08:17:09.478881', '19801104', '2021-09-28T08:17:09.360772', '김가입', '{bcrypt}$2a$10$WXG5HiVH1nhKDkUvfd.WE.LXFqAx48dzG9jrkD17MTVLWoTIH9grO', '가나대학교', false, null, 'user1', 'programmer', '취업준비중');
INSERT INTO user(user_id, created_date, birth_date, last_modified_date, name, password, college, is_graduated, profile_url, username, wanted_job, job)
VALUES ('067d42d516394118bf54ad37d2f6f61e', '2019-11-12T08:17:09.478881', '19960212', '2021-09-28T10:10:44.727651', '사나', '{bcrypt}$2a$10$JcLQoqreG3LBlrMGFzl3RuwSPgNmDAyah3g2ppaZARA.XbLTlaiZK', '서울대학교', false, null, 'user2', 'designer', 'jyp');
INSERT INTO user(user_id, created_date, birth_date, last_modified_date, name, password, college, is_graduated, profile_url, username, wanted_job, job)
VALUES ('d9b4adce82bd48fe9456cfb20d43537d', '2016-09-28T08:24:20.281872', '19771112', '2021-09-28T08:24:20.170979', '김관리', '{bcrypt}$2a$10$x.0xNhjwVgXW0Fj/NZ7sH.ybsNFq.pEM/T5YukPz.Pdn34Njxlr1m', '가나대학교', true, null, 'admin1', 'programmer', 'portrade');
INSERT INTO user(user_id, created_date, birth_date, last_modified_date, name, password, college, is_graduated, profile_url, username, wanted_job, job)
VALUES ('db3e7741982a40d5919767ac9a0bb94a', '2021-10-08T08:24:20.281872', '19801104', '2021-09-28T08:24:20.170979', '김유저', '{bcrypt}$2a$10$x.0xNhjwVgXW0Fj/NZ7sH.ybsNFq.pEM/T5YukPz.Pdn34Njxlr1m', '가나대학교', true, null, 'user3', 'programmer', 'naver');


INSERT INTO user_roles(user_id, roles)
VALUES ('3cbe539a33ba4550a82c63be333ac2d0', 'ROLE_USER');
INSERT INTO user_roles(user_id, roles)
VALUES ('067d42d516394118bf54ad37d2f6f61e', 'ROLE_USER');
INSERT INTO user_roles(user_id, roles)
VALUES ('d9b4adce82bd48fe9456cfb20d43537d', 'ROLE_ADMIN');
INSERT INTO user_roles(user_id, roles)
VALUES ('db3e7741982a40d5919767ac9a0bb94a', 'ROLE_USER');

INSERT INTO portfolio(portfolio_id, created_date, category,	description, is_public, last_modified_date, main_image,	title, view_count, user_id)
VALUES (1, '2019-10-19T08:17:09.478881','programming', '안녕하세요. 포트폴리오1 소개입니다.', true, now(), 'https://image1.com', '포트폴리오1제목', 100, '3cbe539a33ba4550a82c63be333ac2d0');
INSERT INTO portfolio(portfolio_id, created_date, category,	description, is_public, last_modified_date, main_image,	title, view_count, user_id)
VALUES (2, '2019-10-20T08:17:09.478881','programming', '안녕하세요. 포트폴리오2 소개입니다.', true, now(), 'https://image2.com', '포트폴리오2제목', 300, '3cbe539a33ba4550a82c63be333ac2d0');
INSERT INTO portfolio(portfolio_id, created_date, category,	description, is_public, last_modified_date, main_image,	title, view_count, user_id)
VALUES (3, '2019-10-21T08:17:09.478881','programming', '안녕하세요. 포트폴리오3 소개입니다.', false, now(), 'https://image3.com', '포트폴리오3제목', 0, '3cbe539a33ba4550a82c63be333ac2d0');

INSERT INTO portfolio_image(portfolio_image_id, url, portfolio_id)
VALUES (1, 'https://portfolioImage1.com', 1);
INSERT INTO portfolio_image(portfolio_image_id, url, portfolio_id)
VALUES (2, 'https://portfolioImage1.com', 1);
INSERT INTO portfolio_image(portfolio_image_id, url, portfolio_id)
VALUES (3, 'https://portfolioImage1.com', 1);


INSERT INTO notice(notice_id, created_date, content, last_modified_date, title, view_count, user_id)
VALUES (1, '2017-01-21T08:17:09.478881', '공지사항1 내용입니다.', now(), '[공지사항 분류]공지사항 제목입니다.', 1200, 'd9b4adce82bd48fe9456cfb20d43537d');
INSERT INTO notice(notice_id, created_date, content, last_modified_date, title, view_count, user_id)
VALUES (2, '2017-01-23T08:17:09.478881', '공지사항2 내용입니다.', now(), '[공지사항 분류]공지사항 제목입니다.', 1220, 'd9b4adce82bd48fe9456cfb20d43537d');
INSERT INTO notice(notice_id, created_date, content, last_modified_date, title, view_count, user_id)
VALUES (3, '2017-01-25T08:17:09.478881', '공지사항3 내용입니다.', now(), '[공지사항 분류]공지사항 제목입니다.', 1500, 'd9b4adce82bd48fe9456cfb20d43537d');


INSERT INTO faq(faq_id, created_date, content, last_modified_date, title, user_id)
VALUES (1, '2017-01-21T08:17:09.478881', '포트레이트는 간편하게 포트폴리오를 업로드할 수 있어요.', now(), '포트폴리오 업로드는 어떻게 하나요?', 'd9b4adce82bd48fe9456cfb20d43537d');
INSERT INTO faq(faq_id, created_date, content, last_modified_date, title, user_id)
VALUES (2, '2017-01-23T08:17:09.478881', '비공개 탭을 누르시면 됩니다.', now(), '포트폴리오를 비공개로 전환하고 싶어요.', 'd9b4adce82bd48fe9456cfb20d43537d');
INSERT INTO faq(faq_id, created_date, content, last_modified_date, title, user_id)
VALUES (3, '2017-01-25T08:17:09.478881', '공유하기 버튼을 누르시면 됩니다.', now(), '포트폴리오를 암호화해서 공유하고 싶어요.', 'd9b4adce82bd48fe9456cfb20d43537d');

INSERT INTO qna(qna_id, dtype, created_date, category, content, email, is_public, last_modified_date, name, phone_number, status, title, question_id, user_id)
VALUES (1, 'QUESTION', '2017-01-21T08:17:09.478881', '업로드 문의', '이력서 업로드 문의합니다.', 'rla@naver.com', false, now(), '김가입', '12341234', 'UNANSWERED', '1:1 문의합니다.', null, '3cbe539a33ba4550a82c63be333ac2d0');
INSERT INTO qna(qna_id, dtype, created_date, category, content, email, is_public, last_modified_date, name, phone_number, status, title, question_id, user_id)
VALUES (2, 'QUESTION', '2020-11-21T08:17:09.478881', '업로드 문의', '이력서 업로드 문의합니다.', 'sa@naver.com', true, now(), '사나', '12341234', 'ANSWERED', '1:1 문의합니다.', null, '067d42d516394118bf54ad37d2f6f61e');
INSERT INTO qna(qna_id, dtype, created_date, category, content, email, is_public, last_modified_date, name, phone_number, status, title, question_id, user_id)
VALUES (3, 'ANSWER', '2020-11-22T08:17:09.478881', null, '1:1문의 답변해드립니다.', null, true, now(), null, null, null, '1:1 답변해드립니다.', 2, 'd9b4adce82bd48fe9456cfb20d43537d');
INSERT INTO qna(qna_id, dtype, created_date, category, content, email, is_public, last_modified_date, name, phone_number, status, title, question_id, user_id)
VALUES (4, 'QUESTION', '2021-01-21T08:17:09.478881', '업로드 문의', '이력서 업로드 문의합니다.', 'sa@naver.com', false, now(), '사나', '12341234', 'UNANSWERED', '1:1 문의합니다.', null, '067d42d516394118bf54ad37d2f6f61e');

-- 기업
INSERT INTO company(company_id, created_date, address, ceo, form, founding_date, homepage, industry, last_modified_date, member_count, name, sales, user_id)
VALUES (1, '2017-01-21T08:17:09.478881', '서울특별시 금천구 가산디지털1로 2 (가산동) 1213호 (주)지니프릭스', '지니', '스타트업', '2014년 2월 8일', 'https://www.gnifrix.com', 'IT', now(), '124명', '지니프릭스', '12억', 'db3e7741982a40d5919767ac9a0bb94a');
INSERT INTO company(company_id, created_date, address, ceo, form, founding_date, homepage, industry, last_modified_date, member_count, name, sales, user_id)
VALUES (2, '2019-01-21T08:17:09.478881', '서울특별시 강남구 테헤란로77길', '김헬프', '중견기업', '2000년 2월 8일', 'https://www.help-me.kr', '법률/회계', now(), '1124명', '헬프미', '12억', 'db3e7741982a40d5919767ac9a0bb94a');
INSERT INTO company(company_id, created_date, address, ceo, form, founding_date, homepage, industry, last_modified_date, member_count, name, sales, user_id)
VALUES (3, '2020-11-29T08:17:09.478882', '서울특별시 강남구 테헤란로11길', '김이구', '중견기업', '2011년 2월 8일', 'https://www.29.co.kr', '패션/디자인', now(), '24명', '29샵', '6억', 'db3e7741982a40d5919767ac9a0bb94a');
INSERT INTO company(company_id, created_date, address, ceo, form, founding_date, homepage, industry, last_modified_date, member_count, name, sales, user_id)
VALUES (4, '2020-11-22T08:17:09.478881', '서울 강남구 테헤란로 142, 12층', '이승건', '스타트업, 외부강사법인', '2013년 4월 23일(업력 8년)', '홈페이지 주소', '금융 지원 서비스업', now(), '717명(2020년 기준)', '기업A', '1,1879억 3,079만원(2020년 기준)', '3cbe539a33ba4550a82c63be333ac2d0');


-- 기업 공고
INSERT INTO recruitment(recruitment_id, created_date, address, career, category, education, last_modified_date, logo, title, pay, view_count, work_type, company_id)
VALUES (1,  '2020-12-07T08:17:09.478882', '서울특별시 강남구 테헤란로11길', 'UI/UX 디자이너', 'designer', '신입/경력', now(), 'http://www.aws1243.com', '29샵 UI/UX 디자이너 채용', '회사내규에 따름', '1300', '정규직', 3);
INSERT INTO recruitment(recruitment_id, created_date, address, career, category, education, last_modified_date, logo, title, pay, view_count, work_type, company_id)
VALUES (2,  '2020-12-08T08:17:09.478882', '서울특별시 강남구 테헤란로11길', '웹 프로그래머', 'programmer', '신입/경력', now(), 'http://www.aws1243.com', '29샵 웹 프로그래머 채용', '회사내규에 따름', '1000', '정규직', 3);
INSERT INTO recruitment(recruitment_id, created_date, address, career, category, education, last_modified_date, logo, title, pay, view_count, work_type, company_id)
VALUES (3,  '2020-12-09T08:17:09.478882', '서울특별시 강남구 테헤란로11길', '백엔드 프로그래머', 'programmer', '신입/경력', now(), 'http://www.aws1243.com', '29샵 백엔드 프로그래머 채용', '회사내규에 따름', '100', '정규직', 3);
INSERT INTO recruitment(recruitment_id, created_date, address, career, category, education, last_modified_date, logo, title, pay, view_count, work_type, company_id)
VALUES (4,  '2020-12-09T08:17:09.478882', '서울특별시 강남구 테헤란로11길', '백엔드 프로그래머', 'programmer', '신입/경력', now(), 'http://www.aws1243.com', '기업A 백엔드 프로그래머 채용', '회사내규에 따름', '100', '정규직', 4);

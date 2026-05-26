use demo_identity;

INSERT INTO realm_config
(realm_name, client_id, client_secret, server_url, active)
VALUES
('demo', 'demo-admin', 'LFOpLOF5yP2yYxhh7ElVhyJNiVXW86LQ', 'http://localhost:8180', true);

INSERT INTO roles
(code, name, realm)
VALUES
('ADMIN', 'Quản trị viên', 'demo'),
('USER', 'Người dùng', 'demo');


INSERT INTO roles
(code, name, description, status, realm)
VALUES
('MANAGER', 'Quản lý', 'Role quản lý', 1, 'demo'),
('LEADER', 'Trưởng nhóm', 'Role trưởng nhóm', 1, 'demo'),
('HR', 'Nhân sự', 'Role nhân sự', 1, 'demo'),
('ACCOUNTANT', 'Kế toán', 'Role kế toán', 1, 'demo'),
('CONTENT', 'Biên tập nội dung', 'Role content', 1, 'demo'),
('TRAINER', 'Giảng viên', 'Role đào tạo', 1, 'demo'),
('SUPPORT', 'Hỗ trợ', 'Role hỗ trợ khách hàng', 1, 'demo'),
('TESTER', 'Kiểm thử', 'Role tester', 1, 'demo'),
('DEV', 'Lập trình viên', 'Role developer', 1, 'demo');

INSERT INTO functions
(code, name, parent_id, path, function_rank, realm)
VALUES

('SETTING', 'Cài đặt', NULL, '/settings', 1, 'demo'),

('KNOWLEDGE', 'Kiến thức', NULL, '/knowledge', 2, 'demo'),

('TRAINING', 'Đào tạo', NULL, '/training', 3, 'demo'),

('REPORT', 'Báo cáo', NULL, '/reports', 4, 'demo'),

('EXAM_SYSTEM', 'Thi & Luyện tập', NULL, '/exam', 5, 'demo'),

('NOTIFICATION', 'Thông báo', NULL, '/notifications', 6, 'demo');


--

-- lấy id cha
SET @SETTING_ID = (SELECT id FROM functions WHERE code = 'SETTING' AND realm = 'demo');
SET @KNOWLEDGE_ID = (SELECT id FROM functions WHERE code = 'KNOWLEDGE' AND realm = 'demo');
SET @TRAINING_ID = (SELECT id FROM functions WHERE code = 'TRAINING' AND realm = 'demo');
--

-- CHILD SETTINGS
INSERT INTO functions(code, name, parent_id, path, function_rank, realm)
VALUES
('USER_ROLE', 'Vai trò người dùng', @SETTING_ID, '/settings/roles', 1, 'demo'),
('USER_MANAGEMENT', 'Quản lý người dùng', @SETTING_ID, '/settings/users', 2, 'demo'),
('DEPARTMENT', 'Phòng ban', @SETTING_ID, '/settings/departments', 3, 'demo');

-- CHILD KNOWLEDGE
INSERT INTO functions(code, name, parent_id, path, function_rank, realm)
VALUES
('DOCUMENT', 'Tài liệu', @KNOWLEDGE_ID, '/knowledge/documents', 1, 'demo'),
('CATEGORY', 'Danh mục', @KNOWLEDGE_ID, '/knowledge/categories', 2, 'demo'),
('TAG', 'Thẻ', @KNOWLEDGE_ID, '/knowledge/tags', 3, 'demo');

-- CHILD TRAINING
INSERT INTO functions(code, name, parent_id, path, function_rank, realm)
VALUES
('COURSE', 'Khoá học', @TRAINING_ID, '/training/courses', 1, 'demo'),
('LESSON', 'Bài học', @TRAINING_ID, '/training/lessons', 2, 'demo'),
('EXAM', 'Kỳ thi', @TRAINING_ID, '/training/exams', 3, 'demo');
--

-- ADMIN full quyền, nhớ role_id đúng role ADMIN của anh
INSERT INTO role_permission
(role_id, function_id, can_view, can_create, can_edit, can_delete, can_import, can_export, can_approve)
SELECT 1, id, 1, 1, 1, 1, 1, 1, 1
FROM functions
WHERE realm = 'demo';

-- USER chỉ view, nhớ role_id đúng role USER của anh
INSERT INTO role_permission
(role_id, function_id, can_view, can_create, can_edit, can_delete, can_import, can_export, can_approve)
SELECT 3, id, 1, 0, 0, 0, 0, 0, 0
FROM functions
WHERE realm = 'demo';
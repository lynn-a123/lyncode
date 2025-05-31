-- 添加优先级和进度列到task表
ALTER TABLE task ADD COLUMN priority INT DEFAULT 0 COMMENT '优先级';
ALTER TABLE task ADD COLUMN progress INT DEFAULT 0 COMMENT '任务进度'; 
DELETE
FROM POST_ZZANG;
DELETE
FROM POST_IMAGE;
DELETE
FROM COMMENT_ZZANG;
DELETE
FROM POST_REPORT_POST_IMAGE_URLS;
DELETE
FROM POST_REPORT;
DELETE
FROM COMMENT_REPORT;
DELETE
FROM USER_REPORT;
DELETE
FROM COMMENT;
DELETE
FROM POST;
DELETE
FROM SECTOR;
DELETE
FROM USER_IMAGE;
DELETE
FROM USER_ROLES
WHERE USER_ID > 8;
DELETE
FROM USER
WHERE ID > 8;

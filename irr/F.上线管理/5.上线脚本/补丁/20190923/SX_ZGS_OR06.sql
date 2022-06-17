CREATE OR REPLACE VIEW SX_ZGS_OR06 AS
SELECT
MAX(DECODE(IOR.INDEX_CODE,'OR06006',IOR.INDEX_RESULT_Q2)) COL_1,
MAX(DECODE(IOR.INDEX_CODE,'OR06007',IOR.INDEX_RESULT_Q2)) COL_2,
MAX(DECODE(IOR.INDEX_CODE,'OR06008',IOR.INDEX_RESULT_Q2)) COL_3,
MAX(DECODE(IOR.INDEX_CODE,'OR06009',IOR.INDEX_RESULT_Q2)) COL_4,
MAX(DECODE(IOR.INDEX_CODE,'OR06010',IOR.INDEX_RESULT_Q2)) COL_5,
MAX(DECODE(IOR.INDEX_CODE,'OR06011',IOR.INDEX_RESULT_Q2)) COL_6,
MAX(DECODE(IOR.INDEX_CODE,'OR06012',IOR.INDEX_RESULT_Q2)) COL_7,
MAX(DECODE(IOR.INDEX_CODE,'OR06001',IOR.INDEX_RESULT_Q2)) COL_8,
MAX(DECODE(IOR.INDEX_CODE,'OR06002',IOR.INDEX_RESULT_Q2)) COL_9,
MAX(DECODE(IOR.INDEX_CODE,'OR06003',IOR.INDEX_RESULT_Q2)) COL_10,
MAX(DECODE(IOR.INDEX_CODE,'OR06004',IOR.INDEX_RESULT_Q2)) COL_11,
MAX(DECODE(IOR.INDEX_CODE,'OR06013',IOR.INDEX_RESULT_Q2)) COL_12,
MAX(DECODE(IOR.INDEX_CODE,'OR06014',IOR.INDEX_RESULT_Q2)) COL_13,
MAX(DECODE(IOR.INDEX_CODE,'OR06015',IOR.INDEX_RESULT_Q2)) COL_14,
MAX(DECODE(IOR.INDEX_CODE,'OR02007',IOR.INDEX_RESULT_Q2)) COL_15,
MAX(DECODE(IOR.INDEX_CODE,'OR06005',IOR.INDEX_RESULT_Q2)) COL_16,
MAX(DECODE(IOR.INDEX_CODE,'OR02007',IOR.INDEX_RESULT_Q2)) COL_17,
MAX(DECODE(IOR.INDEX_CODE,'OR06016',IOR.INDEX_RESULT_Q2)) COL_18,
MAX(DECODE(IOR.INDEX_CODE,'OR06017',IOR.INDEX_RESULT_Q2)) COL_19,
MAX(DECODE(IOR.INDEX_CODE,'OR06018',IOR.INDEX_RESULT_Q2)) COL_20,
IOR.ORG_CODE STOCK_ID,
TO_XBRL_TASK_BATCH(IOR.TASK_BATCH) REPORT_MARK_DATE
FROM T_IRR_ORG_RESULT IOR
INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_ID=II.ID
INNER JOIN T_IRR_PROJ_TYPE PT ON II.PROJ_TYPE_ID=PT.ID
WHERE (PT.TYPE_CODE='OR06' OR PT.TYPE_CODE='OR02')
GROUP BY IOR.ORG_CODE,TO_XBRL_TASK_BATCH(IOR.TASK_BATCH);

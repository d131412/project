CREATE OR REPLACE VIEW SX_ZGS_OR15 AS
SELECT
MAX(DECODE(IOR.INDEX_CODE,'OR15001',IOR.INDEX_RESULT_Q2)) COL_1,
MAX(DECODE(IOR.INDEX_CODE,'OR15002',IOR.INDEX_RESULT_Q2)) COL_2,
MAX(DECODE(IOR.INDEX_CODE,'OR15003',IOR.INDEX_RESULT_Q2)) COL_3,
MAX(DECODE(IOR.INDEX_CODE,'OR15004',IOR.INDEX_RESULT_Q2)) COL_4,
MAX(DECODE(IOR.INDEX_CODE,'OR15005',IOR.INDEX_RESULT_Q2)) COL_5,
MAX(DECODE(IOR.INDEX_CODE,'OR15006',IOR.INDEX_RESULT_Q2)) COL_6,
MAX(DECODE(IOR.INDEX_CODE,'OR15007',IOR.INDEX_RESULT_Q2)) COL_7,
MAX(DECODE(IOR.INDEX_CODE,'OR15008',IOR.INDEX_RESULT_Q2)) COL_8,
MAX(DECODE(IOR.INDEX_CODE,'OR15009',IOR.INDEX_RESULT_Q2)) COL_9,
MAX(DECODE(IOR.INDEX_CODE,'OR15010',IOR.INDEX_RESULT_Q2)) COL_10,
MAX(DECODE(IOR.INDEX_CODE,'OR15011',IOR.INDEX_RESULT_Q2)) COL_11,
MAX(DECODE(IOR.INDEX_CODE,'OR15012',IOR.INDEX_RESULT_Q2)) COL_12,
MAX(DECODE(IOR.INDEX_CODE,'OR15013',IOR.INDEX_RESULT_Q2)) COL_13,
MAX(DECODE(IOR.INDEX_CODE,'OR15014',IOR.INDEX_RESULT_Q2)) COL_14,
MAX(DECODE(IOR.INDEX_CODE,'OR15015',IOR.INDEX_RESULT_Q2)) COL_15,
IOR.ORG_CODE STOCK_ID,
TO_XBRL_TASK_BATCH(IOR.TASK_BATCH) REPORT_MARK_DATE
FROM T_IRR_ORG_RESULT IOR
INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_ID=II.ID
INNER JOIN T_IRR_PROJ_TYPE PT ON II.PROJ_TYPE_ID=PT.ID
WHERE PT.TYPE_CODE='OR15'
GROUP BY IOR.ORG_CODE,TO_XBRL_TASK_BATCH(IOR.TASK_BATCH);

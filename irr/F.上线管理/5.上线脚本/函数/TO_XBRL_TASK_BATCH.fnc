CREATE OR REPLACE FUNCTION TO_XBRL_TASK_BATCH
(
TASK_BATCH IN VARCHAR2
)
RETURN DATE
AS
YEAR_TEMP VARCHAR2(4);
BATCH_TEMP VARCHAR2(1);
XBRL_TASK_BATCH_STR VARCHAR2(8);
BEGIN
 YEAR_TEMP := SUBSTR(TASK_BATCH,1,4);
 BATCH_TEMP := SUBSTR(TASK_BATCH,6,1);
 XBRL_TASK_BATCH_STR := YEAR_TEMP ||
 CASE BATCH_TEMP
   WHEN '1' THEN '0331'
   WHEN '2' THEN '0630'
   WHEN '3' THEN '0930'
   WHEN '4' THEN '1231'
 END;
 RETURN TO_DATE(XBRL_TASK_BATCH_STR,'YYYY-MM-DD');
END;
/

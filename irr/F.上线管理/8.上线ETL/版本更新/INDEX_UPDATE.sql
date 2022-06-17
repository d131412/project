DROP PROCEDURE SP_DROP_INDEX
/
DROP PROCEDURE SP_CREATE_INDEX
/

CREATE PROCEDURE SP_CREATE_INDEX(
 V_RTNCODE OUT VARCHAR2,
 V_RTNMSG  OUT VARCHAR2
)
AS
V_SQL        VARCHAR2(1000);
V_RECORD_NUM VARCHAR2(500);
V_START      DATE;
V_END        DATE;
BEGIN

V_SQL:='CREATE INDEX CAU_INDEX_01 ON E_BDS_CLAIMS_CLAIMSAUDIT(REPORT_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CAU_INDEX_02 ON E_BDS_CLAIMS_CLAIMSAUDIT(REPORT_ID, POLICY_NO, INSURANCE_CODE, HIS_TAG)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_CHANGE_IDX_CHANGE_ID ON E_BDS_CSBPM_POLICY_CHANGE(CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_PROSTEP_ACTION_NAME ON E_BDS_CSBPM_PROCESSSTEP(ACTION_NAME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_PROSTEP_STEP_NAME ON E_BDS_CSBPM_PROCESSSTEP(STEP_NAME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_SERVICE_IDX_CHANGE_ID ON E_BDS_CSBPM_POLICY_SERVICE(CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX DBA_052009 ON E_BDS_LISHASL_LZCARDTRACK(MANAGECOM, OPERATEFLAG, MAKEDATE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX DBA_11102602 ON E_BDS_LISHASL_LZCARDTRACK(MANAGECOM, OPERATEFLAG, STATE, MAKEDATE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX DUTY_DETAIL_INDEX01 ON E_BDS_CLAIMS_DUTY_DETAIL(DUTY_SERNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_BIDM_RENEWAL_CHNLCODE ON E_BDS_BIDM_RENEWAL_RP_FY(ADD_CHNLCOD)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_BIDM_RENEWAL_ORGCODE ON E_BDS_BIDM_RENEWAL_RP_FY(INNERORGCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_CASEID_INDEX ON E_BDS_CCCMP_CRM_CALL_LIST(CASEID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_DBA_01090901 ON E_BDS_BIDM_MGECMP_MAP(PROVINCECOMCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_DBA_13051306 ON E_BDS_CCNT_CRM_CASE3(DATIME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX$$_180390002 ON E_BDS_CCNT_CRM_CASE3(SUBSTR("CHDRCOYID",0,1), CHDRNUM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX_CRM_CASE3 ON E_BDS_CCNT_CRM_CASE3(CHDRCOYID, CHDRNUM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX_D_MAN_MAP_001 ON E_BDS_BIDM_MGECMP_MAP(BRANCHCOMCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX_D_ORGMAP_001 ON E_BDS_BIDM_ORGMAP(INNERORGCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_INDEX_D_ORGMAP1 ON E_BDS_BIDM_ORGMAP(ORIGORGCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISHASL_LPMAIN_IDE1 ON E_BDS_GBS_LPGRPEMAIN(EDORSTATE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LCGRPAPPNT_IDX ON E_BDS_GBS_LCGRPAPPNT(GRPNAME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDE3 ON E_BDS_GBS_LJSPAY(POLICYNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX1 ON E_BDS_GBS_LJSPAY(OTHERNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX2 ON E_BDS_GBS_LJSPAY(OTHERNOTYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX4 ON E_BDS_GBS_LJSPAY(CONTTYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX5 ON E_BDS_GBS_LJSPAY(MANAGECOM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX6 ON E_BDS_GBS_LJSPAY(BALANCERELANO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_POLICYNO_INDEX ON E_BDS_CCCMP_CRM_CASE3(POLICYNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_POLICY_INDEX ON E_BDS_CCCMP_CRM_CALL_LIST(POLICYNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_CLAIMS_STATUS ON E_BDS_CLAIMS_CLAIMS(STATUS)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_DUTY_INDEX01 ON E_BDS_CLAIMS_DUTY(REPORT_ID, POLICY_NO, ASS_CLAIMS_FLAG, COMPUTE_FLAG, INSURANCE_CODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_INSURANCE_KEY1 ON E_BDS_CLAIMS_INSURANCE(REPORT_ID, POLICY_NO, ASS_CLAIMS_FLAG)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_INSURANCE_KEY2 ON E_BDS_CLAIMS_INSURANCE(INSURANCE_CODE, INSURANCE_TYPE, REPORT_ID, POLICY_NO, ASS_CLAIMS_FLAG)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_T_POL_APP_CARD_ID ON E_BDS_CSBPM_POLICY_CHANGE(APPLY_CARD_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_T_PROCESSSTEP_CHANGE_ID ON E_BDS_CSBPM_PROCESSSTEP(CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_T_PAY_INFO_SUB_CHANGE_ID ON E_BDS_CSBPM_PAY_INFO(SUB_CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_T_POL_SER_SUB_CHANGE_ID ON E_BDS_CSBPM_POLICY_SERVICE(SUB_CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX IDX_LZCARDTRACK01 ON E_BDS_LISHASL_LZCARDTRACK(RECEIVECOM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX IDX_LZCARDTRACK02 ON E_BDS_LISHASL_LZCARDTRACK(SENDOUTCOM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX IDX_LZCARDTRACK03 ON E_BDS_LISHASL_LZCARDTRACK(STARTNO, ENDNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX01 ON E_BDS_CLAIMS_LIABILITY(INSURANCE_CODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX02 ON E_BDS_CLAIMS_LIABILITY(IS_JINTIE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX03 ON E_BDS_CLAIMS_LIABILITY(PRODUCT_TYPE, DUTY_TYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX04 ON E_BDS_CLAIMS_LIABILITY(NOT_PAY_TYPE, PAY_PERCENT, IS_MEDICAL_INSURANCE, VALIDFLAG, IS_OW_DUTY)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX PARAM_INDEX_01 ON E_BDS_CLAIMS_PARAM(PAR_TYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX SYS_C00169891A ON E_BDS_CLAIMS_CLAIMS(REPORT_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX TASK_TIME_INDEX01 ON E_BDS_CLAIMS_TASKTIME(REPORT_ID, CODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX TASK_TIME_INDEX02 ON E_BDS_CLAIMS_TASKTIME(END_TIME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX TFPAY_INDEX1 ON E_BDS_CLAIMS_FINANCE_PAYMENT(BIDUIID, BANKACCOUNT, BANKNAME, DETAILSUM)';
 EXECUTE IMMEDIATE V_SQL;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     '',
     'SP_CREATE_INDEX',
     '',
     V_START,
     V_END,
     '',
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  ----------------------------------????--------------------------------------
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_END     := SYSDATE;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
  --------------------------------????????-----------------------------
      INSERT INTO ETL_LOG_DETAIL
        (ID,
         INDEX_ID,
         SP_NAME,
         TABLE_NAME,
         START_TIME,
         END_TIME,
         DATA_DATE,
         RECORD_NUM,
         ERR_CODE,
         ERR_MSG)
      VALUES
        (SYS_GUID(),
         '',
         'SP_CREATE_INDEX',
         '',
         V_START,
         V_END,
         '',
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
  --------------------------------????????-----------------------------
    END;
END SP_CREATE_INDEX;

/
CREATE PROCEDURE SP_DROP_INDEX(
 V_RTNCODE OUT VARCHAR2,
 V_RTNMSG  OUT VARCHAR2
)
AS
V_SQL        VARCHAR(1000);
V_RECORD_NUM VARCHAR(500);
V_START      DATE;
V_END        DATE;
BEGIN
V_SQL:='DROP INDEX IDX_F_POLICY_INNERPOL';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_LA_F_POLICY';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_LA_F_POLICY_001';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_1';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_2';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_3';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_4';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_5';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX E_BDS_DBA_01090906';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_1';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_2';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_3';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_4';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_5';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_6';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_POL_INSURANCE';   
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_POL_INSURANCE2';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_APPLYENTITY';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_ATS_RETURNSTATE';   
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_DTCODE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_FAILTYPE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_REQSEQID';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_REQSERVED12';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_SOURCENOTECODE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_TRANSCODE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_TRANSSTATE';  
EXECUTE IMMEDIATE V_SQL; 
V_SQL:='DROP INDEX PK_ATS_RDSEQ';  
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_ATS_SEQ';  
EXECUTE IMMEDIATE V_SQL;  
V_SQL:='DROP INDEX KEY_D_FC_AREACODE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_DBA_01090901';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX_D_MAN_MAP_001';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX_D_ORGMAP_001';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_INDEX_D_ORGMAP1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_BIDM_RENEWAL_CHNLCODE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_BIDM_RENEWAL_ORGCODE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_CASEID_INDEX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_POLICY_INDEX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_CRM_CALLBACK_LIST';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_POLICYNO_INDEX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_CRM_CASE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_POLICYS_BASE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_POL_BAT_TRANS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_DBA_13051306';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX$$_180390002';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX_CRM_CASE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_CLAIMS_STATUS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C00169891A';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318636';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CAU_INDEX_01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CAU_INDEX_02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318602';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_DUTY_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318638';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX DUTY_DETAIL_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318640';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318613';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX TFPAY_INDEX1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318617';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003320058';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_INSURANCE_KEY1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_INSURANCE_KEY2';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318642';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX03';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX04';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PARAM_INDEX_01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318632';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318609';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318606';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX TASK_TIME_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX TASK_TIME_INDEX02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318620';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318592';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318594';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318596';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_T_PAY_INFO_SUB_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_CHANGE_IDX_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_T_POL_APP_CARD_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_SERVICE_IDX_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_T_POL_SER_SUB_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_PROSTEP_ACTION_NAME';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_PROSTEP_STEP_NAME';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_T_PROCESSSTEP_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318600';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318610';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003328123';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LCGRPAPPNT_IDX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321824';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX2';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX4';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX5';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX6';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LJSPAY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321851';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321859';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISHASL_LPMAIN_IDE1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321870';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LZCARDSTATE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX DBA_052009';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX DBA_11102602';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX IDX_LZCARDTRACK01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX IDX_LZCARDTRACK02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX IDX_LZCARDTRACK03';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LZCARDTRACK';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LA_CODEITEM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_CHGRANK';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_CODEDEF';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_CONTRACT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_DEPT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_MEMBERSET';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_NATUREGX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_PERSON';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_PSN2DEPT_GX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_RANKDEF';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_TEAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_TEAM2TEAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_TEAMNO';
EXECUTE IMMEDIATE V_SQL;    
V_SQL:='DROP INDEX PK_T_PERSON_1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_PERSON_2';
EXECUTE IMMEDIATE V_SQL;               
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     '',
     'SP_DROP_INDEX',
     '',
     V_START,
     V_END,
     '',
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  ----------------------------------????--------------------------------------
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_END     := SYSDATE;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
  --------------------------------????????-----------------------------
      INSERT INTO ETL_LOG_DETAIL
        (ID,
         INDEX_ID,
         SP_NAME,
         TABLE_NAME,
         START_TIME,
         END_TIME,
         DATA_DATE,
         RECORD_NUM,
         ERR_CODE,
         ERR_MSG)
      VALUES
        (SYS_GUID(),
         '',
         'SP_DROP_INDEX',
         '',
         V_START,
         V_END,
         '',
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
  --------------------------------????????-----------------------------
    END;
END SP_DROP_INDEX;

/


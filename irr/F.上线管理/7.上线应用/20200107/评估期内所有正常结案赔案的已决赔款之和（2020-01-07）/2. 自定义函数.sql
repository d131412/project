CREATE OR REPLACE FUNCTION FUN_TO_IRR(
    P_STR_TAB IN TAB_IRR,
    P_SEPARATOR IN VARCHAR2 DEFAULT ','
  )
  RETURN VARCHAR2 IS
    V_RET_STR VARCHAR2(4000);
  BEGIN
    FOR I IN 1..P_STR_TAB.COUNT LOOP
      V_RET_STR:=V_RET_STR||P_SEPARATOR||P_STR_TAB(I);
   END LOOP;
   RETURN V_RET_STR;
 END FUN_TO_IRR;
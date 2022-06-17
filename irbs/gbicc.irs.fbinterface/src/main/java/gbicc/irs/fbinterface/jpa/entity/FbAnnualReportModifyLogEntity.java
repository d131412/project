package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;
/**
 * 年报修改记录实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_MODIFY_LOG")
public class FbAnnualReportModifyLogEntity extends AuditorEntity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid2")
    @Column(name="ID",length=36)
    private String id;
    /**
     * 统一社会信用代码、注册号
     */
    @Column(name="COMPANY_ID")
    private String companyId;
    /**
     * 年报ID
     */
    @Column(name="ANNUAL_REPORT_ID")
    private String annualReportId;
    /**
     * 修改事项
     */
    @Column(name="MODIFY_DETAIL")
    private String modifyDetail;
    /**
     * 修改前
     */
    @Column(name="MODIFY_BEFORE")
    private String modifyBefore;
    /**
     * 修改后
     */
    @Column(name="MODIFY_AFTER")
    private String modifyAfter;
    /**
     * 修改日期
     */
    @Column(name="MODIFY_DATE")
    private String modifyDate;
    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getAnnualReportId() {
        return annualReportId;
    }

    public void setAnnualReportId(String annualReportId) {
        this.annualReportId = annualReportId == null ? null : annualReportId.trim();
    }

    public String getModifyDetail() {
        return modifyDetail;
    }

    public void setModifyDetail(String modifyDetail) {
        this.modifyDetail = modifyDetail == null ? null : modifyDetail.trim();
    }

    public String getModifyBefore() {
        return modifyBefore;
    }

    public void setModifyBefore(String modifyBefore) {
        this.modifyBefore = modifyBefore == null ? null : modifyBefore.trim();
    }

    public String getModifyAfter() {
        return modifyAfter;
    }

    public void setModifyAfter(String modifyAfter) {
        this.modifyAfter = modifyAfter == null ? null : modifyAfter.trim();
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate == null ? null : modifyDate.trim();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }
}
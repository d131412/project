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
 * 开庭公告其他角色实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Entity
@Table(name="FB_OPENING_NOTICE_OTHER")
public class FbOpeningNoticeOtherEntity extends AuditorEntity implements Serializable{
    
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
     * 开庭公告ID
     */
    @Column(name="OPENING_NOTICE_ID")
    private String openingNoticeId;
    /**
     * 当事人详情ID
     */
    @Column(name="OPENING_NOTICE_PARTY_ID")
    private String openingNoticePartyId;
    /**
     * 名称
     */
    @Column(name="NAME")
    private String name;

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

    public String getOpeningNoticeId() {
        return openingNoticeId;
    }

    public void setOpeningNoticeId(String openingNoticeId) {
        this.openingNoticeId = openingNoticeId == null ? null : openingNoticeId.trim();
    }

    public String getOpeningNoticePartyId() {
        return openingNoticePartyId;
    }

    public void setOpeningNoticePartyId(String openingNoticePartyId) {
        this.openingNoticePartyId = openingNoticePartyId == null ? null : openingNoticePartyId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}
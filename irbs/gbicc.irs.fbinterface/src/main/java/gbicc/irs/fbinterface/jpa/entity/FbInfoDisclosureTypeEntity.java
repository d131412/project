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
 * 信息披露公告类别实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Entity
@Table(name="FB_INFO_DISCLOSURE_TYPE")
public class FbInfoDisclosureTypeEntity extends AuditorEntity implements Serializable{
    
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
     * 信息披露ID
     */
    @Column(name="INFO_DISCLOSURE_ID")
    private String infoDisclosureId;
    /**
     * 公告类别
     */
    @Column(name="TYPE")
    private String type;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public String getInfoDisclosureId() {
        return infoDisclosureId;
    }
    public void setInfoDisclosureId(String infoDisclosureId) {
        this.infoDisclosureId = infoDisclosureId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}

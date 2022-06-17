package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEventPunishDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventPunishDetailRepository;
/**
 * 事件检索具体事件条目相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventPunishDetailService extends DaoService<FbEventPunishDetailEntity, String, FbEventPunishDetailRepository>{

}

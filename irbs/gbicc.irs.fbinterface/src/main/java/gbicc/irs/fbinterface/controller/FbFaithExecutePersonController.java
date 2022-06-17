package gbicc.irs.fbinterface.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbFaithExecutePersonEntity;
import gbicc.irs.fbinterface.jpa.repository.FbFaithExecutePersonRepository;
import gbicc.irs.fbinterface.service.FbFaithExecutePersonService;

/**
 * 失信被执行人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Controller
@RequestMapping("/faithExecutePerson")
public class FbFaithExecutePersonController extends SmartClientRestfulCrudController<FbFaithExecutePersonEntity, String, FbFaithExecutePersonRepository, FbFaithExecutePersonService>{

    private static final Logger LOGGER = LogManager.getLogger(FbFaithExecutePersonController.class);
    /**
     * 根据companyId获取失信被执行人结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getFaithExecutePerson.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getFaithExecutePerson(String companyId){
        try {
            //410100000061836
            return service.getFaithExecutePerson(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
package com.gbicc.aicr.system.flowable.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.CommentEntityImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.framework.flowable.entity.Agent;
import org.wsp.framework.flowable.exception.NoAvailableAssigneeException;
import org.wsp.framework.flowable.service.AgentService;
import org.wsp.framework.flowable.service.ProcessEntityService;
import org.wsp.framework.flowable.support.Assignee;
import org.wsp.framework.flowable.support.CompleteTaskException;
import org.wsp.framework.flowable.support.FrameworkVariableNames;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.role.entity.Role;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.jpa.service.support.protocol.criteria.FetchMode;
import org.wsp.framework.jpa.service.support.protocol.criteria.TextMatchStyle;
import org.wsp.framework.mvc.service.RoleService;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbicc.aicr.system.dto.UserInfoDTO;
import com.gbicc.aicr.system.flowable.entity.TaskInfo;
import com.gbicc.aicr.system.flowable.service.FlowableTaskService;
import com.gbicc.aicr.system.flowable.support.enums.HALifeEnum;
import com.gbicc.aicr.system.util.AppUtil;
import com.gbicc.aicr.system.util.GeneralUtil;

@Service("flowableTaskService")
public class FlowableTaskServiceImpl implements FlowableTaskService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProcessEntityService processEntityService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RuntimeService runtimeService;
	
	@Override
	public HistoryService getHistoryService() {
		return historyService;
	}
	@Override
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
	@Override
	public TaskService getTaskService() {
		return taskService;
	}
	@Override
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	@Override
	public List<Map<String, Object>> getTodoTaskList() throws Exception{
		String backlogSql = "SELECT RES.ID_ as TaskId,RES.NAME_ as TaskName,RES.CREATE_TIME_ as TaskCreateTime, RES.DUE_DATE_ as TaskDueDate, D.ID_ as PDefId,D.Name_ as PDefName,U.FD_USERNAME AS STARTUSER \n"
				+ "       FROM ACT_RU_TASK RES\n" + "       LEFT\n"
				+ "       JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_\n"
				+ "       LEFT JOIN (SELECT * FROM ACT_RU_EXECUTION  WHERE  START_ACT_ID_ IS NOT NULL) E ON RES.PROC_INST_ID_=E.PROC_INST_ID_\n"
				+ "       INNER JOIN ACT_RE_PROCDEF D ON RES.PROC_DEF_ID_ = D.ID_\n"
				+ "		LEFT JOIN FR_AA_USER U ON U.FD_LOGINNAME=E.START_USER_ID_ \n"
				+ "       WHERE (RES.ASSIGNEE_ = '" + SecurityUtil.getLoginName() + "'\n" + "       OR (\n"
				+ "              RES.ASSIGNEE_ IS NULL AND\n"
				+ "              I.TYPE_ = 'CANDIDATE' AND (I.USER_ID_ = '" + SecurityUtil.getLoginName() + "'\n"
				+ "              OR I.GROUP_ID_ IN( '" + SecurityUtil.getDefaultRoleName() + "')\n"
				+ "        ))) order by RES.CREATE_TIME_ desc\n";
		List<Map<String, Object>> retMap = jdbcTemplate.queryForList(backlogSql); 
		//恒安人寿:组装数据,添加采集人
		setOtherTaskInfo(retMap);
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getDoneTaskList() {
		String doneTaskSql = "SELECT\n" + "	ht.ID_        AS TaskId,\n" + "	ht.NAME_       AS TaskName,\n"
				+ "	ht.START_TIME_ AS TaskStartTime,\n" + "	ht.END_TIME_  AS TaskEndTime,\n"
				+ "	U.FD_USERNAME  AS assignee,\n" + "	resp.ID_      AS PDefId,\n" + "	resp.NAME_     AS PDefName \n"
				+ "FROM\n" + "	act_hi_taskinst ht\n"
				+ "	LEFT JOIN act_re_procdef resp ON ht.PROC_DEF_ID_ = resp.ID_ \n"
				+ "   INNER JOIN ACT_HI_PROCINST inst on ht.PROC_INST_ID_=inst.ID_ \n"
				+ "	LEFT JOIN FR_AA_USER U ON U.FD_LOGINNAME=ht.ASSIGNEE_ \n" + "WHERE\n" + " ht.END_TIME_ IS NOT NULL AND ht.ASSIGNEE_ = '"
				+ SecurityUtil.getLoginName() + "' order by ht.END_TIME_ desc \n";
		return jdbcTemplate.queryForList(doneTaskSql);
	}

	/**
	 * 查询待办任务
	 * 
	 * @return 待办任务集
	 */
	@Override
	public Map<String, Object> getTodoTaskList(String processKey, String createDate1, String createDate2,
			String dueDate1, String dueDate2, int start, int size) throws Exception{
	    String loginName = SecurityUtil.getLoginName();
        StringBuffer sql = new StringBuffer(
                "SELECT RES.ID_ TASKID,RES.NAME_ TASKNAME,RES.CREATE_TIME_ TASKCREATETIME,\n"
                        + "RES.DUE_DATE_ TASKDUEDATE,D.NAME_ PDEFNAME,US.FD_USERNAME ASSIGNEE,'否' AUTHFLAG\n"
                        + "FROM ACT_RU_TASK RES INNER JOIN FR_AA_USER US ON RES.ASSIGNEE_=US.FD_LOGINNAME\n"
                        + "INNER JOIN ACT_RE_PROCDEF D ON RES.PROC_DEF_ID_=D.ID_\n");
        sql.append("WHERE RES.ASSIGNEE_='" + loginName + "'\n");
        if (StringUtils.hasText(processKey)) {
            sql.append("AND D.KEY_='" + processKey + "'\n");
        }
        if (StringUtils.hasText(createDate1)) {
            sql.append("AND RES.CREATE_TIME_ >= TO_DATE('" + createDate1 + "','YYYY-MM-DD')\n");
        }
        if (StringUtils.hasText(createDate2)) {
            sql.append("AND RES.CREATE_TIME_ <= TO_DATE('" + createDate2 + "','YYYY-MM-DD')\n");
        }
        if (StringUtils.hasText(dueDate1)) {
            sql.append("AND RES.DUE_DATE_ >= TO_DATE('" + dueDate1 + "','YYYY-MM-DD')\n");
        }
        if (StringUtils.hasText(dueDate2)) {
            sql.append("AND RES.DUE_DATE_ <= TO_DATE('" + dueDate2 + "','YYYY-MM-DD')");
        }
        List<String> authUserIds = findAuthUserAssignee(SecurityUtil.getUserId());
        if (CollectionUtils.isNotEmpty(authUserIds)) {
            sql.append("UNION\n");
            sql.append("SELECT RES.ID_ TASKID,RES.NAME_ TASKNAME,RES.CREATE_TIME_ TASKCREATETIME,\n"
                    + "RES.DUE_DATE_ TASKDUEDATE,D.NAME_ PDEFNAME,US.FD_USERNAME ASSIGNEE,'是' AUTHFLAG\n"
                    + "FROM ACT_RU_TASK RES INNER JOIN FR_AA_USER US ON RES.ASSIGNEE_=US.FD_LOGINNAME\n"
                    + "INNER JOIN ACT_RE_PROCDEF D ON RES.PROC_DEF_ID_=D.ID_\n");
            sql.append("WHERE RES.ASSIGNEE_ IN (" + AppUtil.listToInString(authUserIds) + ")\n");
            if (StringUtils.hasText(processKey)) {
                sql.append("AND D.KEY_='" + processKey + "'\n");
            }
            if (StringUtils.hasText(createDate1)) {
                sql.append("AND RES.CREATE_TIME_ >= TO_DATE('" + createDate1 + "','YYYY-MM-DD')\n");
            }
            if (StringUtils.hasText(createDate2)) {
                sql.append("AND RES.CREATE_TIME_ <= TO_DATE('" + createDate2 + "','YYYY-MM-DD')\n");
            }
            if (StringUtils.hasText(dueDate1)) {
                sql.append("AND RES.DUE_DATE_ >= TO_DATE('" + dueDate1 + "','YYYY-MM-DD')\n");
            }
            if (StringUtils.hasText(dueDate2)) {
                sql.append("AND RES.DUE_DATE_ <= TO_DATE('" + dueDate2 + "','YYYY-MM-DD')\n");
            }
        }
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql.toString());
		//恒安人寿:组装数据,添加采集人
		setOtherTaskInfo(maps);
		int tot = maps.size();
		Map<String, Object> result = new HashMap<String, Object>();
		if (maps != null && tot > 0) {
			if (tot > start + size) {
				maps = maps.subList(start - 1, start + size);
			} else {
				maps = maps.subList(start - 1, maps.size());
			}
			result.put("page", start / size + 1);
			result.put("total", (tot - 1) / size + 1);
			result.put("recordsTotal", tot);
			result.put("data", maps);
			return result;
		} else {
			result.put("page", 0);
			result.put("total", 0);
			result.put("recordsTotal", 0);
			result.put("data", null);
			return result;
		}
	}

	/**
	 * 查询已办任务
	 * 
	 * @return 已办任务集
	 */
	@Override
	public Map<String, Object> getDoneTaskList(String processKey, String createDate1, String createDate2,
			String finishDate1, String finishDate2, int start, int size) {
		String doneTaskSql = "SELECT\n" + "	ht.ID_        AS TaskId,\n" + "	ht.NAME_       AS TaskName,\n"
				+ "	ht.START_TIME_ AS TaskStartTime,\n" + "	ht.END_TIME_  AS TaskEndTime,\n"
				+ "	U.FD_USERNAME AS assignee,\n" + "	resp.ID_      AS PDefId,\n" + "	resp.NAME_     AS PDefName \n"
				+ "FROM\n" + "	act_hi_taskinst ht\n"
				+ "	LEFT JOIN act_re_procdef resp ON ht.PROC_DEF_ID_ = resp.ID_ \n"
				+ "   INNER JOIN ACT_HI_PROCINST inst on ht.PROC_INST_ID_=inst.ID_ \n"
				+ "	LEFT JOIN FR_AA_USER U ON U.FD_LOGINNAME=ht.ASSIGNEE_ \n" + "WHERE\n" + "	ht.ASSIGNEE_ = '"
				+ SecurityUtil.getLoginName() + "' \n" + "   "//AND inst.END_TIME_ IS NULL
				+ "	AND ht.END_TIME_ IS NOT NULL \n";
		if (processKey != null && !processKey.equals("")) {
			doneTaskSql = doneTaskSql + " and	resp.KEY_='" + processKey + "'\n";
		}
		if (createDate1 != null && !createDate1.equals("")) {
			doneTaskSql = doneTaskSql + " and	ht.START_TIME_>=to_date('" + createDate1 + "','yyyy-mm-dd')\n";
		}
		if (createDate2 != null && !createDate2.equals("")) {
			doneTaskSql = doneTaskSql + " and	ht.START_TIME_<to_date('" + createDate2 + "','yyyy-mm-dd')\n";
		}
		if (finishDate1 != null && !finishDate1.equals("")) {
			doneTaskSql = doneTaskSql + " and	ht.END_TIME_>=to_date('" + finishDate1 + "','yyyy-mm-dd')\n";
		}
		if (finishDate2 != null && !finishDate2.equals("")) {
			doneTaskSql = doneTaskSql + " and	ht.END_TIME_<to_date('" + finishDate2 + "','yyyy-mm-dd')\n";
		}
		doneTaskSql+="ORDER BY ht.END_TIME_ DESC";
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(doneTaskSql);
		int tot = maps.size();

		// List<Task> tasks
		// =taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).list();
		Map<String, Object> result = new HashMap<String, Object>();
		if (maps != null && maps.size() > 0) {
			if (maps.size() > start + size) {
				maps = maps.subList(start - 1, start + size);
			} else {
				maps = maps.subList(start - 1, maps.size());
			}
			result.put("page", start / size + 1);
			result.put("total", (tot - 1) / size + 1);
			result.put("recordsTotal", tot);
			result.put("data", maps);
			return result;
		} else {
			result.put("page", 0);
			result.put("total", 0);
			result.put("recordsTotal", 0);
			result.put("data", null);
			return result;
		}
	}

	@Override
	public List<Map<String, Object>> getCompleteTaskList() {
		String completeTaskSql = "SELECT\n" + "	resp.NAME_ AS processName,\n" + "	count( 1 ) AS taskNum \n" + "FROM\n"
				+ "	act_hi_taskinst ht\n" + "	INNER JOIN act_re_procdef resp ON ht.PROC_DEF_ID_ = resp.ID_ \n"
				+ " INNER JOIN ACT_HI_PROCINST inst on ht.PROC_INST_ID_=inst.ID_ \n" + "WHERE\n"
				+ "	ht.ASSIGNEE_ = '" + SecurityUtil.getLoginName() + "' \n" + "   AND inst.END_TIME_ IS NULL"
				+ "	AND ht.END_TIME_ IS NOT NULL \n" + "GROUP BY\n" + "	resp.NAME_";
		return jdbcTemplate.queryForList(completeTaskSql);
	}

	@Override
	@Transactional
	public void completeTask(String taskId, Map<String, Object> variables, Map<String, Object> transientVariables,String comment) throws Exception {
		if (!StringUtils.hasText(taskId)) {
			throw new NullPointerException("Task's ID is NULL");
		}
		if (transientVariables == null) {
			transientVariables = new HashMap<String, Object>();
		}
		if (!transientVariables.containsKey(FrameworkVariableNames.ASSIGNEE)
				&& (variables == null || !variables.containsKey(FrameworkVariableNames.ASSIGNEE))) {
			transientVariables.put(FrameworkVariableNames.ASSIGNEE, null);
		}
		// 获取当前任务
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.addComment(taskId, null, comment);
		// 尝试完成任务
		taskService.complete(taskId, variables, transientVariables);
        //记录授权日志
        String assignee = task.getAssignee();
        String planIssue = findPlanIssueByProcInstId(task.getProcessInstanceId());
        String currentUserLoginName = SecurityUtil.getLoginName();
        if (!comment.contains("一票全过") && StringUtils.hasText(assignee) && StringUtils.hasText(planIssue)
                && !assignee.equals(currentUserLoginName)) {
            User assigneeUser = userService.getRepository().findByLoginName(assignee);
            StringBuffer authLogSql = new StringBuffer("INSERT INTO T_IRR_AUTH_TASK_LOG\n");
            authLogSql.append(
                    "(ID, TASK_ID, TASK_CODE, TASK_NAME, USER_ID, USER_NAME, AUTH_ID, AUTH_NAME, DEAL_RESULT, FD_CREATOR, FD_CREATE_DATE, FD_LAST_MODIFIER, FD_LAST_MODIFYDATE)\n");
            authLogSql.append("VALUES\n");
            authLogSql.append("('" + UUID.randomUUID().toString() + "','" + task.getId() + "','"
                    + AppUtil.createAuthTaskLogTaskCode(planIssue) + "','" + task.getName() + "','"
                    + assigneeUser.getId() + "','" + assigneeUser.getUserName() + "','" + SecurityUtil.getUserId()
                    + "','" + SecurityUtil.getUserName() + "','" + comment + "','" + currentUserLoginName
                    + "',SYSTIMESTAMP,'" + currentUserLoginName + "',SYSTIMESTAMP)");
            jdbcTemplate.execute(authLogSql.toString());
        }
		// 获取流程是否允许领取任务
		boolean canClaimTask = processEntityService.canClaimTask(task.getProcessDefinitionId());
		if (canClaimTask) {
			return;
		}
		// 通过当前任务获取流程实例ID
		String procInstId = task.getProcessInstanceId();
		// 获取当前任务完成后产生的的新任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
		// 如果未找到新任务,表示流程结束,正常返回
		if (tasks == null || tasks.size() == 0) {
			return;
		}
		for(Task newTask : tasks) {
			if(newTask == null || StringUtils.hasText(newTask.getAssignee())) {
				continue;
			}
			String taskDefinitionKey = newTask.getTaskDefinitionKey();
			// 如果该节点存在历史任务，表示流程重新回到此节点，
			// 此时应该将任务尽可能的分配给原处理人
			List<HistoricTaskInstance> historyTasks = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(procInstId).taskDefinitionKey(taskDefinitionKey).finished()
					.orderByHistoricTaskInstanceEndTime().desc().list();
			if (historyTasks != null && historyTasks.size() > 0) {
				HistoricTaskInstance historyTask = historyTasks.get(0);
				String historyAssignee = historyTask.getAssignee();
				if (StringUtils.hasText(historyAssignee)) {
					Agent agent = getAgent(historyAssignee);
					if (agent != null) {
						taskService.setAssignee(newTask.getId(), agent.getAgentLoginName());
					} else {
						taskService.setAssignee(newTask.getId(), historyAssignee);
					}
					continue;
				}
			}
			// 如果新任务已经成功分配到了处理人,正常返回
			if (StringUtils.hasText(newTask.getAssignee())) {
				Agent agent = getAgent(newTask.getAssignee());
				if (agent != null) {
					taskService.setAssignee(newTask.getId(), agent.getAgentLoginName());
				}
				continue;
			}
			// 如果未分配到单一的一个处理人,需要根据流程定义查找可用的处理人
			String procDefinitionId = newTask.getProcessDefinitionId();
			// 获取到流程定义模型对象
			BpmnModel model = repositoryService.getBpmnModel(procDefinitionId);
			Collection<FlowElement> elements = model.getMainProcess().getFlowElements();
			UserTask newUserTaskDefinition = null;
			if (elements != null && elements.size() > 0) {
				for (FlowElement element : elements) {
					if (taskDefinitionKey.equals(element.getId()) && (element instanceof UserTask)) {
						newUserTaskDefinition = (UserTask) element;
						break;
					}
				}
			}
			if (newUserTaskDefinition == null) {
				continue;
			}
			// 查找候选处理人
			List<Assignee> assignees = queryAssignee(task, newTask, newUserTaskDefinition);
			if (assignees != null && assignees.size() > 0) {
				if (assignees.size() > 1) {
					throw new CompleteTaskException("节点配置了多个处理人："+objectMapper.writeValueAsString(assignees));
				} else {
					taskService.setAssignee(newTask.getId(), assignees.get(0).getLoginName());
				}
			} else {
				throw new NoAvailableAssigneeException();
			}
		}
	}

	// ${flowableTaskService.getRPMultipleUser(execution)}
	/**
	 * 获取风险偏好制定各风险主管部门的经办人
	 * 
	 * @param execution
	 * @return
	 * @throws Execption
	 */
	@Override
	public List<Map<String, String>> getRPMultipleUser() throws Exception {
		List<Map<String, String>> users = new ArrayList<Map<String, String>>();
		String role = "RMOperator";
		// 查询偏好指标的机构
		String sql = "select distinct t.dep_code from T_APP_RC_INDEX_CODE t where t.status = '1' and t.quota_level = '1' ";
		String sqlInRole = "select distinct u.fd_loginname loginName  from fr_aa_user u left join fr_aa_user_role ur on u.fd_id = ur.fd_user_id  left join fr_aa_role r on ur.fd_role_id = r.fd_id  where r.fd_code = '"
				+ role + "'";
		List<String> depCodeList = jdbcTemplate.queryForList(sql, String.class);
		List<String> rmoList = jdbcTemplate.queryForList(sqlInRole, String.class);
		if (depCodeList == null || depCodeList.size() < 1) {
			return users;
		}
		for (String depCode : depCodeList) {
			Map<String, String> info = new HashMap<>();
			List<UserInfoDTO> userInfoList = getUserInfo(depCode);
			if (userInfoList == null || userInfoList.size() < 1) {
				continue;
			}
			String busio = "";
			String busir = "";
			for (UserInfoDTO userInfo : userInfoList) {
				if ("BUSIOperator".equals(userInfo.getRoleCode()) && userInfo.getLoginName() != null) {
					busio = userInfo.getLoginName();
				}
				if ("BUSIReviewer".equals(userInfo.getRoleCode()) && userInfo.getLoginName() != null) {
					busir = userInfo.getLoginName();
				}
			}
			info.put("BUSIOperator", busio);
			info.put("BUSIReviewer", busir);
			info.put(role, rmoList.get(0));
			users.add(info);
		}

		return users;
	}

	private List<UserInfoDTO> getUserInfo(String depCode) {
		String findUser = "select distinct u.fd_loginname loginName,\n" + "       u.fd_username userName,\n"
				+ "       r.fd_code roleCode,\n" + "       r.fd_name roleName,\n" + "       o.fd_code depCode,\n"
				+ "       o.fd_name depName\n" + "         from fr_aa_user u \n"
				+ "         left join fr_aa_user_role ur on u.fd_id = ur.fd_user_id\n"
				+ "         left join fr_aa_role r on ur.fd_role_id = r.fd_id\n"
				+ "         left join fr_aa_user_org uo on u.fd_id = uo.fd_user_id\n"
				+ "         left join fr_aa_org o on uo.fd_org_id = o.fd_id \n" + "         where o.fd_code = '"
				+ depCode + "' ";
		List<UserInfoDTO> list = jdbcTemplate.query(findUser, new BeanPropertyRowMapper(UserInfoDTO.class));
		return list;
	}

	@Override
	public List<String> getDeptUserByDeptAndRole(String deptCode, String roleCode) {
		String sql = "SELECT                                                        "
				+ "	U.FD_LOGINNAME                                               "
				+ "FROM                                                          "
				+ "	FR_AA_USER U                                                 "
				+ "		LEFT JOIN FR_AA_USER_ORG O                                 "
				+ "		ON U.FD_ID=O.FD_USER_ID                                    "
				+ "			LEFT JOIN FR_AA_USER_ROLE UR                             "
				+ "			ON U.FD_ID=UR.FD_USER_ID                                 "
				+ "				LEFT JOIN FR_AA_ROLE R                                 "
				+ "				ON UR.FD_ROLE_ID=R.FD_ID                               "
				+ "				LEFT JOIN FR_AA_ORG ORG                                 "
				+ "				ON O.FD_ORG_ID=ORG.FD_ID                               "
				+ "WHERE                                                         " + "	ORG.FD_CODE= '" + deptCode
				+ "' AND                         " + "	R.FD_CODE='" + roleCode + "'";

		return jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 获取上一个节点操作人对应同部门对应角色的人
	 * 
	 * @param execution
	 * @return
	 * @throws Execption
	 */
	@Override
	public List<String> getNextTaskSameDeptUsersByRole(String lastTaskUserVar, String roleCode) throws Exception {
		String sql = "SELECT                                                        "
				+ "	U.FD_LOGINNAME                                               "
				+ "FROM                                                          "
				+ "	FR_AA_USER U                                                 "
				+ "		LEFT JOIN FR_AA_USER_ORG O                                 "
				+ "		ON U.FD_ID=O.FD_USER_ID                                    "
				+ "			LEFT JOIN FR_AA_USER_ROLE UR                             "
				+ "			ON U.FD_ID=UR.FD_USER_ID                                 "
				+ "				LEFT JOIN FR_AA_ROLE R                                 "
				+ "				ON UR.FD_ROLE_ID=R.FD_ID                               "
				+ "WHERE                                                         "
				+ "	O.FD_ORG_ID IN (SELECT                                       "
				+ "						FD_ORG_ID                                          "
				+ "					FROM                                                 "
				+ "						FR_AA_USER_ORG                                     "
				+ "					WHERE                                                "
				+ "						FD_USER_ID IN (	SELECT                             "
				+ "											FD_ID                                    "
				+ "										FROM                                       "
				+ "											FR_AA_USER                               "
				+ "										WHERE                                      "
				+ "											FD_LOGINNAME ='" + lastTaskUserVar
				+ "') ) AND                         " + "	R.FD_CODE='" + roleCode + "'";

		return jdbcTemplate.queryForList(sql, String.class);
	}

	private Agent getAgent(String loginName) {
		List<Agent> agents = agentService.getAgents(loginName);
		if (agents != null && agents.size() > 0) {
			for (Agent agent : agents) {
				Date startDate = agent.getStartDate();
				Date endDate = agent.getEndDate();
				Date now = new Date();
				if (now.getTime() >= startDate.getTime() && now.getTime() <= endDate.getTime()) {
					return agent;
				}
			}
		}
		return null;
	}

	private List<Assignee> queryAssignee(Task preTask, Task task, UserTask taskDefinition) throws Exception {
		List<String> candidateUsers = taskDefinition.getCandidateUsers();
		List<String> candidateGroups = taskDefinition.getCandidateGroups();
		if (candidateUsers != null && candidateUsers.size() > 0) {
			List<User> users = userService.getRepository().findByLoginNameIn(candidateUsers);
			if (users != null && users.size() > 0) {
				List<Assignee> candidateUserAssignees = new ArrayList<Assignee>();
				for (User user : users) {
					Assignee assignee = new Assignee();
					assignee.setId(user.getId());
					assignee.setLoginName(user.getLoginName());
					assignee.setUserName(user.getUserName());
					candidateUserAssignees.add(assignee);
				}
				return candidateUserAssignees;
			}
		}
		if (candidateGroups != null && candidateGroups.size() > 0) {
			// 通过定义的组(角色)获取所包含的所有用户
			List<User> roleUsers = null;
			List<Role> roles = roleService.getRepository().findByCodeIn(candidateGroups);
			if (roles != null && roles.size() > 0) {
				List<String> roleIds = new ArrayList<String>();
				for (Role role : roles) {
					roleIds.add(role.getId());
				}
				roleUsers = userService.listUsersByRoles(roleIds);
			}
			if (roleUsers == null || roleUsers.size() == 0) {
				return null;
			}
			// String preAssignee
			// =(preTask==null?SecurityUtil.getLoginName():preTask.getAssignee());
			// User user =userService.getRepository().findByLoginName(preAssignee);
			// //如果未找到前一处理人,直接返回角色下面的用户
			// if(user==null) {
			// if(roleUsers!=null && roleUsers.size()>0){
			// List<Assignee> candidateUserAssignees =new ArrayList<Assignee>();
			// for(User _user : roleUsers){
			// Assignee assignee =new Assignee();
			// assignee.setId(_user.getId());
			// assignee.setLoginName(_user.getLoginName());
			// assignee.setUserName(_user.getUserName());
			// candidateUserAssignees.add(assignee);
			// }
			// return candidateUserAssignees;
			// }else {
			// return null;
			// }
			// }

			// 所有机构及其父机构
			// List<Org> orgAndParentOrgs =new ArrayList<Org>();
			// List<Org> orgs =user.getOrgs();
			// if(orgs!=null && orgs.size()>0){
			// for(Org org : orgs){
			// orgAndParentOrgs.add(org);
			// Org parentOrg =org.getParent();
			// while(parentOrg!=null){
			// orgAndParentOrgs.add(parentOrg);
			// parentOrg =parentOrg.getParent();
			// }
			// }
			// }
			//
			// List<String> orgIds =new ArrayList<String>();
			// for(Org org : orgAndParentOrgs){
			// orgIds.add(org.getId());
			// }
			// List<User> orgUsers =roleUsers;//serService.listUsersByOrgs(orgIds);

			// if(orgUsers==null || orgUsers.size()==0){
			// 目前对于没有机构配置的情况，直接返回角色的人
			List<Assignee> candidateUserAssignees = new ArrayList<Assignee>();
			for (User _user : roleUsers) {
				Assignee assignee = new Assignee();
				assignee.setId(_user.getId());
				assignee.setLoginName(_user.getLoginName());
				assignee.setUserName(_user.getUserName());
				candidateUserAssignees.add(assignee);
			}
			return candidateUserAssignees;
			// }

			// 获取两个用户集合的交集
			// orgUsers(roleUsers);
			// if(orgUsers!=null && orgUsers.size()>0){
			// List<Assignee> candidateUserAssignees =new ArrayList<Assignee>();
			// for(User _user : orgUsers){
			// Assignee assignee =new Assignee();
			// assignee.setId(_user.getId());
			// assignee.setLoginName(_user.getLoginName());
			// assignee.setUserName(_user.getUserName());
			// candidateUserAssignees.add(assignee);
			// }
			// return candidateUserAssignees;
			// }
		}
		return null;
	}

	public List<String> getUserDeptId(String userId) {
		String sql = "SELECT                                                        "
				+ "	R.FD_CODE                                            		"
				+ "FROM                                                          "
				+ "	FR_AA_USER U                                                 "
				+ "		LEFT JOIN FR_AA_USER_ORG O                           "
				+ "		ON U.FD_ID=O.FD_USER_ID                                    "
				+ "			LEFT JOIN FR_AA_ORG R                             "
				+ "			ON R.FD_ID=O.FD_ORG_ID                                 "
				+ "WHERE                                                         " + "	U.FD_LOGINNAME ='" + userId
				+ "'";

		return jdbcTemplate.queryForList(sql, String.class);

	}

	@Override
	@Transactional
	public void completeTask(String taskId, Map<String, Object> variables, Map<String, Object> transientVariables,
			Map<String, Object> variablesLocal, Map<String, Object> newTaskVariablesLocal, String comment)
			throws Exception {
		if (!StringUtils.hasText(taskId)) {
			throw new NullPointerException("process task's id is null");
		}
		//获取当前任务
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(task == null) {
			throw new NullPointerException("process task is null");
		}
		//通过当前任务获取流程实例ID
		String procInstId = task.getProcessInstanceId();
		if(StringUtils.hasText(comment)) {
			taskService.addComment(taskId, procInstId, comment);
		}
		//当前任务添加本地化变量
		if(variablesLocal == null) {
			variablesLocal = new HashMap<String, Object>();
		}
		for(String key : variablesLocal.keySet()) {
			taskService.setVariableLocal(taskId, key, variablesLocal.get(key));
		}
		if(variables == null) {
			variables = new HashMap<String, Object>();
		}
		if (transientVariables == null) {
			transientVariables = new HashMap<String, Object>();
		}
		//完成任务
		taskService.complete(taskId, variables, transientVariables);
		/*获取新任务进行赋值,未开发
		if(newTaskVariablesLocal == null) {
			newTaskVariablesLocal = new HashMap<String, Object>();
		}*/
	}

	@Override
	public void deleteProcessInstanceByTaskId(String taskId, String comment) throws Exception {
		if(!StringUtils.hasText(taskId)) {
			throw new NullPointerException("process's taskId is null,don't delete processInstance");
		}
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(taskId).singleResult();
		/*删除流程任务*/
		if(pi!=null) {
			runtimeService.deleteProcessInstance(pi.getId(), comment);
		}
	}

	@Override
	public List<TaskInfo> myFinishedTaskDetailByProIns(String proInsId) throws Exception {
		List<TaskInfo> ehpil = new ArrayList<TaskInfo>();
		//.taskAssignee("")  根据这个方法可以获取分配给给定用户的任务
		List<HistoricTaskInstance> htl = historyService.createHistoricTaskInstanceQuery().processInstanceId(proInsId)
				.orderByHistoricTaskInstanceEndTime().asc().orderByHistoricTaskInstanceStartTime().asc()
				.listPage(Integer.valueOf(0), Integer.valueOf(200));
		
		QueryParameter qu = new QueryParameter();
		qu.setFetchMode(FetchMode.EMPTY_CRITERIA_EMPTY);
		qu.setTextMatchStyle(TextMatchStyle.substring);
		for (HistoricTaskInstance ht : htl) {
			TaskInfo ptask = new TaskInfo();
			ptask.setId(ht.getId());
			ptask.setOwner(ht.getAssignee());
			ptask.setName(ht.getName());
			ptask.setCreateDate(ht.getStartTime());
			ptask.setFinishDate(ht.getEndTime());
			
			List<Comment> comments = taskService.getTaskComments(ht.getId());
			 
			String comment = "";
			for (Comment c : comments) {
				CommentEntityImpl ce = (CommentEntityImpl) c;
				
				String message = ce.getMessage();
				
				if (null != c.getFullMessage()) {
					comment+=message;
					//comment += new String(c.getFullMessage().getBytes(), System.getProperty("file.encoding"));
				}
			}
			
			//String name = comment.substring(comment.indexOf("*"));
			if(StringUtils.hasText(ht.getAssignee())) {
				User examUser = new User();
				examUser.setLoginName(ht.getAssignee());
				List<User> users = userService.fetch(examUser, qu);
				if(!CollectionUtils.isEmpty(users) && users.get(0) != null) {
					ptask.setOwnerInfo(users.get(0).getUserName()+"("+ht.getAssignee()+")");
					List<Org> orgs = users.get(0).getOrgs();
					String name="";
					if(comment.contains("*")) {
						name = comment.substring(comment.indexOf("*")+1);
						comment = comment.substring(0, comment.indexOf("*"));
						System.out.println(comment);
						ptask.setName(ht.getName()+"("+name+")");
					}else {
						if(!CollectionUtils.isEmpty(orgs) && orgs.get(0) != null) {
							ptask.setName(ht.getName()+"("+orgs.get(0).getName()+")");
							
						}
					}
				}
			}
			
			ptask.setTaskComment(comment);
			ehpil.add(ptask);
		}
		return ehpil;
	}
	
	@Override
	public List<TaskInfo> myFinishedTaskDetailByProInsByMyself(String proInsId,String processDefinitionId) throws Exception {
		List<TaskInfo> ehpil = new ArrayList<TaskInfo>();
		String loginName = SecurityUtil.getLoginName();
		//.taskAssignee("")  根据这个方法可以获取分配给给定用户的任务
		String sql="SELECT\n" + 
				"	t1.assignee_\n" + 
				"FROM\n" + 
				"	act_hi_actinst t1,\n" + 
				"	act_hi_comment t2,\n" + 
				"	( SELECT * FROM ( SELECT rank () over ( partition BY assignee_ ORDER BY start_time_ DESC ) r, assignee_, execution_id_ FROM act_hi_actinst ) WHERE r = 1 ) t3 \n" + 
				"WHERE\n" + 
				"	t1.task_id_ = t2.task_id_ \n" + 
				"	AND t1.proc_def_id_ = ? \n" + 
				"	AND t1.execution_id_ = t3.execution_id_ \n" + 
				"	AND t3.assignee_ =? \n" + 
				"	\n" + 
				"ORDER BY\n" + 
				"	t1.end_time_";
		List<String> queryForList2 = jdbcTemplate.queryForList(sql, String.class, processDefinitionId,loginName);
		String sql1="SELECT\n" + 
				"	t1.EXECUTION_ID_\n" + 
				"FROM\n" + 
				"	act_hi_actinst t1,\n" + 
				"	act_hi_comment t2,\n" + 
				"	( SELECT * FROM ( SELECT rank () over ( partition BY assignee_ ORDER BY start_time_ DESC ) r, assignee_, execution_id_ FROM act_hi_actinst ) WHERE r = 1 ) t3 \n" + 
				"WHERE\n" + 
				"	t1.task_id_ = t2.task_id_ \n" + 
				"	AND t1.proc_def_id_ = ? \n" + 
				"	AND t1.execution_id_ = t3.execution_id_ \n" + 
				"	AND t3.assignee_ =? \n" + 
				"	\n" + 
				"ORDER BY\n" + 
				"	t1.end_time_";
		List<String> queryForList3 = jdbcTemplate.queryForList(sql1, String.class, processDefinitionId,loginName);
		List<HistoricTaskInstance> htl;
		if(queryForList2.size()>0) {
//			htl = historyService.createHistoricTaskInstanceQuery().processInstanceId(proInsId).taskAssigneeIds(queryForList2)
//			.orderByHistoricTaskInstanceEndTime().asc().orderByHistoricTaskInstanceStartTime().asc()
//			.listPage(Integer.valueOf(0), Integer.valueOf(200));
			htl=historyService.createHistoricTaskInstanceQuery().processInstanceId(proInsId).taskAssigneeIds(queryForList2).executionId(queryForList3.get(0))
			.orderByHistoricTaskInstanceEndTime().asc().orderByHistoricTaskInstanceStartTime().asc()
			.listPage(Integer.valueOf(0), Integer.valueOf(200));
		}else {
			htl = historyService.createHistoricTaskInstanceQuery().processInstanceId(proInsId).taskAssignee(loginName)
					.orderByHistoricTaskInstanceEndTime().asc().orderByHistoricTaskInstanceStartTime().asc()
					.listPage(Integer.valueOf(0), Integer.valueOf(200));
		}
		
		QueryParameter qu = new QueryParameter();
		qu.setFetchMode(FetchMode.EMPTY_CRITERIA_EMPTY);
		qu.setTextMatchStyle(TextMatchStyle.substring);
		for (HistoricTaskInstance ht : htl) {
			TaskInfo ptask = new TaskInfo();
			ptask.setId(ht.getId());
			ptask.setOwner(ht.getAssignee());
			ptask.setName(ht.getName());
			ptask.setCreateDate(ht.getStartTime());
			ptask.setFinishDate(ht.getEndTime());
			
			List<Comment> comments = taskService.getTaskComments(ht.getId());
			String comment = "";
			for (Comment c : comments) {
				CommentEntityImpl ce = (CommentEntityImpl) c;
				String message = ce.getMessage();
				if (null != c.getFullMessage()) {
					comment+=message;
					//comment += new String(c.getFullMessage().getBytes(), System.getProperty("file.encoding"));
				}
			}
			
			//String name = comment.substring(comment.indexOf("*"));
			if(StringUtils.hasText(ht.getAssignee())) {
				User examUser = new User();
				examUser.setLoginName(ht.getAssignee());
				List<User> users = userService.fetch(examUser, qu);
				if(!CollectionUtils.isEmpty(users) && users.get(0) != null) {
					ptask.setOwnerInfo(users.get(0).getUserName()+"("+ht.getAssignee()+")");
					List<Org> orgs = users.get(0).getOrgs();
					String name="";
					if(comment.contains("*")) {
						name = comment.substring(comment.indexOf("*")+1);
						comment = comment.substring(0, comment.indexOf("*"));
						System.out.println(comment);
						ptask.setName(ht.getName()+"("+name+")");
					}else {
						if(!CollectionUtils.isEmpty(orgs) && orgs.get(0) != null) {
							ptask.setName(ht.getName()+"("+orgs.get(0).getName()+")");
							
						}
					}
				}
			}
			
			ptask.setTaskComment(comment);
			ehpil.add(ptask);
		}
		return ehpil;
	}

	@Override
	public List<TaskInfo> findFinishedTaskDetailByBussinessKey(String bussinessKey) throws Exception {
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bussinessKey).singleResult();
		if(processInstance == null) {
			return null;
		}
		return myFinishedTaskDetailByProIns(processInstance.getId());
	}

	@Override
	public List<String> findAssignByCondition(String bussinessKey, String actId) throws Exception {
		String sql = "SELECT DISTINCT HA.ASSIGNEE_ FROM ACT_HI_PROCINST HP INNER JOIN ACT_HI_ACTINST HA ON HP.PROC_INST_ID_=HA.PROC_INST_ID_ "+
					 "WHERE HP.BUSINESS_KEY_='"+bussinessKey+"' AND HA.ACT_ID_='"+actId+"'";
		return jdbcTemplate.queryForList(sql, String.class);
	}
	
	@Override
	public void setOtherTaskInfo(List<Map<String, Object>> taskInfoList) throws Exception {
		//组装数据,添加任务信息
		if(CollectionUtils.isEmpty(taskInfoList)) {
			return;
		}
		//查询所有的用户信息
		String userSql = "SELECT DISTINCT U.FD_LOGINNAME,U.FD_USERNAME,ORG.FD_NAME FROM FR_AA_USER U \n" + 
				"INNER JOIN FR_AA_USER_ORG UO ON U.FD_ID=UO.FD_USER_ID \n" + 
				"INNER JOIN FR_AA_ORG ORG ON UO.FD_ORG_ID=ORG.FD_ID";
		List<Map<String, Object>> userOrgInfo = jdbcTemplate.queryForList(userSql);
		Map<String, Map<String, Object>> userOrgMap = new HashMap<String, Map<String, Object>>();
		if(CollectionUtils.isNotEmpty(userOrgInfo)) {
			for(Map<String, Object> map : userOrgInfo) {
				Object loginNameObj =  map.get("FD_LOGINNAME");
				if(GeneralUtil.strObjIsNotEmpty(loginNameObj)) {
					userOrgMap.put(loginNameObj.toString(), map);
				}
			}
		}
		for(Map<String, Object> map : taskInfoList) {
			if(MapUtils.isEmpty(map)) {
				continue;
			}
			Object taskIdObj = map.get("TASKID");
			if(GeneralUtil.strObjIsEmpty(taskIdObj)) {
				continue;
			}
			Object collUserLoginName = taskService.getVariableLocal(taskIdObj.toString(), HALifeEnum.COLL_LOGIN_NAME_VAR.getValue());
			if(GeneralUtil.strObjIsEmpty(collUserLoginName)||!(collUserLoginName.toString().equals(SecurityUtil.getLoginName()))) {
				collUserLoginName = SecurityUtil.getLoginName();
			}
			Map<String, Object> infoMap =  userOrgMap.get(collUserLoginName);
			if(MapUtils.isNotEmpty(infoMap)) {
				String taskInfo = GeneralUtil.strObjIsEmpty(infoMap.get("FD_USERNAME")) ? "" : infoMap.get("FD_USERNAME").toString();
				if(GeneralUtil.strObjIsNotEmpty(infoMap.get("FD_NAME"))) {
					taskInfo += "(" + infoMap.get("FD_NAME").toString() + ")";
				}
				map.put("TASKINFO", taskInfo);
			}
		}
	}

    @Override
    public List<String> findAuthUserAssignee(String userId) throws Exception {
        if (!StringUtils.hasText(userId)) {
            return null;
        }
        String sql = "SELECT DISTINCT U.FD_LOGINNAME FROM T_IRR_AUTH IA INNER JOIN FR_AA_USER U ON IA.USER_ID=U.FD_ID WHERE IA.AUTH_ID='"
                + userId + "'";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public String findTaskAssignee(String taskId) throws Exception {
        if (!StringUtils.hasText(taskId)) {
            return null;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            return task.getAssignee();
        }
        return null;
    }

    @Override
    public Boolean taskAssigneeIsSame(String taskId, String userLoginName) throws Exception {
        if (!StringUtils.hasText(userLoginName)) {
            return null;
        }
        String assignee = findTaskAssignee(taskId);
        if (!StringUtils.hasText(assignee)) {
            return null;
        }
        if (userLoginName.equals(assignee)) {
            return true;
        }
        return false;
    }

    @Override
    public String getTaskTrueAssigneeUserId(String taskId, String userLoginName) throws Exception {
        if (!StringUtils.hasText(taskId) || !StringUtils.hasText(userLoginName)) {
            throw new RuntimeException("无法获取任务的真实处理人！");
        }
        String assignee = findTaskAssignee(taskId);
        if (!StringUtils.hasText(assignee)) {
            throw new RuntimeException("无法获取任务的真实处理人！");
        }
        User user = null;
        if (userLoginName.equals(assignee)) {
            user = userService.getRepository().findByLoginName(userLoginName);
        } else {
            user = userService.getRepository().findByLoginName(assignee);
        }
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    @Override
    public String findPlanIssueByProcInstId(String procInstId) throws Exception {
        if (StringUtils.hasText(procInstId)) {
            String sql = "SELECT IT.TASK_BATCH FROM ACT_HI_PROCINST HP INNER JOIN T_IRR_TASK IT ON HP.BUSINESS_KEY_=IT.ID WHERE HP.PROC_INST_ID_='"
                    + procInstId + "'";
            return jdbcTemplate.queryForObject(sql, String.class);
        }
        return "";
    }

}

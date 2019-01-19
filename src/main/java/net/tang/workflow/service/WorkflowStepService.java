package net.tang.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.tang.core.tools.CommonUtils;
import net.tang.core.tools.CriteriaBuilder;
import net.tang.workflow.WorkflowConstants;
import net.tang.workflow.mapper.WorkflowStepMapper;
import net.tang.workflow.model.WorkflowStep;
import net.tang.workflow.page.Pages;
import net.tang.workflow.page.WrapperProcess;

@Service
public class WorkflowStepService extends ServiceImpl<WorkflowStepMapper, WorkflowStep> {

	@Autowired
//	private IUserService userService;
	
	/**
	 * 查询步骤，带查询参数
	 * @param entityId
	 * @return
	 */
	public List<Map<String, Object>> wrapStepsWithArgs(Integer entityId) {
		Map<String, Object> args = new HashMap<>();
		args.put("entity", entityId);
		List<Map<String, Object>> map = wrapSteps(false, args);
		return map;
	}
	
	/**
	 * 查询步骤，不带查询参数
	 * @return
	 */
	public List<Map<String, Object>> wrapStepsNoArgs() {
		List<Map<String, Object>> map = wrapSteps(true, null);
		return map;
	}

	/**
	 * 包装流程步骤
	 * @param query
	 * @param args
	 * @return
	 */
	private List<Map<String, Object>> wrapSteps(boolean query, Map<String, Object> args) {
		List<WorkflowStep> steps = getSteps(args);
		List<Map<String, Object>> map = parseSteps(steps, query);
		return map;
	}

	/**
	 * 查询流程步骤
	 * @param args
	 * @return
	 */
	private List<WorkflowStep> getSteps(Map<String, Object> args) {
		QueryWrapper<net.tang.workflow.model.WorkflowStep> qw = new QueryWrapper<>();
		args.forEach((k, v) -> {
			qw.eq(k, v);
		});
		WrapperProcess.ensureStatus(qw);
		WrapperProcess.sortRecords(qw, false, "entity", "step");
		List<WorkflowStep> steps = this.list(qw);
		return steps;
	}

	/**
	 * 解析流程步骤
	 * @param steps
	 * @param query
	 * @return
	 */
	private List<Map<String, Object>> parseSteps(List<WorkflowStep> steps, boolean query) {
		List<Map<String, Object>> list = new ArrayList<>();
		if (query) {
			Map<String, Object> root = new HashMap<>();
			root.put("id", -1);
			root.put("name", "全部");
			list.add(root);
		}

		Map<String, Object> map = null;
		for (WorkflowStep e : steps) {
			map = new HashMap<>();
			map.put("id", e.getId());
			map.put("name", e.getEntityName() + "-" + e.getStepName());
			list.add(map);
		}
		return list;
	}

	/**
	 * 获取流程的所有步骤
	 * @param entity
	 * @return
	 */
	public List<WorkflowStep> getEntitySteps(Integer entity) {
		List<WorkflowStep> steps = null;

		if (entity != null) {
			WorkflowStep step = new WorkflowStep();
			step.setEntity(entity);
			QueryWrapper<WorkflowStep> qw = new QueryWrapper<>(step);
			WrapperProcess.ensureStatus(qw);
			IPage<WorkflowStep> pages = Pages.getPage();
			this.page(pages, qw);

			if (pages != null)
				steps = pages.getRecords();
		}

		return steps;
	}

	/**
	 * 判断用户在指定的业务里是申请人还是审核人
	 * 
	 * @param entityId 业务实体
	 * @param userId 用户id
	 * @return
	 */
	public Integer workflowPart(Integer entityId, Integer userId) {
//		int ident = 1;
//		if (entityId != null && userId != null) {
//			List<WorkflowStep> steps = getStepByEntiry(entityId);
//			Set<Integer> set = new HashSet<>();
//			String deal = null;
//			String[] deals = null;
//			for (WorkflowStep workflowStep : steps) {
//				deal = workflowStep.getDeal();
//				if (deal != null) {
//					deals = deal.split(",");
//					for (int i = deals.length - 1; i >= 0; i--) {
//						set.add(CommonUtils.parseInt(deals[i]));
//					}
//				}
//			}
//			User user = userService.getById(userId);
//			if (user != null) {
//				String role = user.getRoleId();
//				if (role != null) {
//					String[] roles = role.split(",");
//					for (int i = 0, len = roles.length; i < len; i++) {
//						if (set.contains(CommonUtils.parseInt(roles[i]))) {
//							ident = 2;
//							break;
//						}
//					}
//				}
//			}
//		}
		return -1;
	}

	/**
	 * 查询某一流程实体的流程步骤
	 * @param entityId
	 * @return
	 */
	private List<WorkflowStep> getStepByEntiry(Integer entityId) {
		WorkflowStep step = new WorkflowStep();
		step.setEntity(entityId);
		QueryWrapper<WorkflowStep> qw = new QueryWrapper<>(step);
		List<WorkflowStep> steps = this.list(qw);
		return steps;
	}

	/**
	 * 根据部门查询审批人
	 * 
	 * @param step
	 * @return
	 */
//	public String parseDealByDept(WorkflowStep step) {
//		String dealer = null;
//		if (step != null) {
//			String deal = step.getDeal();
//			Integer flag = step.getFlag();
//			List<User> loginUsers = userService.selectUserList();
//			List<User> deptUsers = new ArrayList<>();
//			out: for (User simpleUser : loginUsers) {
//				String dept = simpleUser.getDeptId();
//				String[] depts = dept.split(",");
//				String temp = null;
//				for (int i = 0, len = depts.length; i < len; i++) {
//					if (deal.equals(depts[i])) {
//						temp = String.valueOf(simpleUser.getId());
//						if (flag == 1) {
//							dealer = temp;
//							break out;
//						} else {
//							deptUsers.add(simpleUser);
//							continue out;
//						}
//					}
//				}
//			}
//			if (dealer == null) {
//				String[] users = new String[loginUsers.size()];
//				loginUsers.toArray(users);
//				dealer = randomArray(users);
//			}
//		}
//		return dealer;
//	}

	/**
	 * 产生0-(users.length-1)的整数值,也是数组的索引
	 * @param users
	 * @return
	 */
	private String randomArray(String[] users) {
		int index = (int) (Math.random() * users.length);
		String rand = users[index];
		return rand;
	}

	/**
	 * 根据角色查询审批人
	 * 
	 * @param step
	 * @param deptId
	 * @return
	 */
//	public String parseDealByRole(WorkflowStep step, Integer userId) {
//		String dealer = null;
//		if (step != null) {
//			String deal = step.getDeal();
//			Integer flag = step.getFlag();
//			List<User> loginUsers = userService.selectUserList();
//			//有该角色的人处理
//			if (flag == 1) {
//				out: for (User simpleUser : loginUsers) {
//					String role = simpleUser.getRoleId();
//					String[] roles = role.split(",");
//					for (int i = 0, len = roles.length; i < len; i++) {
//						if (deal.equals(roles[i])) {
//							dealer = String.valueOf(simpleUser.getId());
//							break out;
//						}
//					}
//				}
//			} else { 
//				//有该角色的人且与申请人是同一部门的人处理
//				User user = userService.getById(userId);
//				String deptId = user.getDeptId();
//				String[] deals = deal.split(",");
//				for (int i = 0, len = deals.length; i < len; i++) {
//					out: for (User simpleUser : loginUsers) {
//						String role = simpleUser.getRoleId();
//						String[] roles = role.split(",");
//						String dept = simpleUser.getDeptId();
//						String[] depts = dept.split(",");
//						for (int j = 0, len2 = roles.length; j < len2; j++) {
//							if (roles[j].equals(deals[i])) {
//								for (int k = 0, len3 = depts.length; k < len3; k++) {
//									if (deptId != null && deptId.contains(depts[k])) {
//										dealer = String.valueOf(simpleUser.getId());
//										break out;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return dealer;
//	}

}

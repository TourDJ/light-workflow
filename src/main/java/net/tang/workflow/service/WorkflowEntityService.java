package net.tang.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.tang.tools.WrapperProcess;
import net.tang.workflow.mapper.WorkflowEntityMapper;
import net.tang.workflow.model.WorkflowEntity;

@Service
public class WorkflowEntityService extends ServiceImpl<WorkflowEntityMapper, WorkflowEntity> {

	/**
	 * 包装流程实体
	 * @param query
	 * @return
	 */
	public List<Map<String, Object>> wrapEntities(boolean query) {
		List<WorkflowEntity> entities = getEntities();
		List<Map<String, Object>> map = parseEntities(entities, query);
		return map;
	}

	/**
	 * 查询流程实体
	 * @return
	 */
	private List<WorkflowEntity> getEntities() {
		QueryWrapper<WorkflowEntity> qw = new QueryWrapper<>();
		qw.setEntity(new WorkflowEntity());
		WrapperProcess.ensureStatus(qw, null);
		WrapperProcess.sortRecords(qw, false, new String[] { "id" });
		List<WorkflowEntity> entities = this.list(qw);
		return entities;
	}
	
	/**
	 * 解析流程实体
	 * @param entities
	 * @param query
	 * @return
	 */
	private List<Map<String, Object>> parseEntities(List<WorkflowEntity> entities, boolean query) {
		List<Map<String, Object>> list = new ArrayList<>();
		if(query) {
			Map<String, Object> root = new HashMap<>();
			root.put("id", -1);
			root.put("name", "全部");
			list.add(root);
		}
		
		Map<String, Object> map = null;
		for (WorkflowEntity e : entities) {
			map = new HashMap<>();
			map.put("id", e.getId());
			map.put("name", e.getName());
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 根据code获取流程实体
	 * @param entityCode
	 * @return
	 */
	public WorkflowEntity getEntity(String entityCode) {
		WorkflowEntity entity = null;
		if(entityCode != null) {
			QueryWrapper<WorkflowEntity> qw = new QueryWrapper<>();
			qw.setEntity(new WorkflowEntity());
			qw.eq("code", entityCode);
			WrapperProcess.ensureStatus(qw, null);
			entity = this.getOne(qw);
		}
		return entity;
	}
  
}

package org.tang.excel.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.tang.entity.Student;
import net.tang.tools.CommonTools;
import net.tang.tools.ExcelTools;

@RestController
@RequestMapping("excel")
public class ExcelController {

	/**
	 * 
	 * 使用Response实现下载文件，浏览器并没有弹出文件保存框，请问什么原因？
	 * JQuery的ajax函数的返回类型只有xml、text、json、html等类型，没有“流”类型，
	 * 所以我们要实现ajax下载，不能够使用相应的ajax函数进行文件下载。
	 * 但可以用js生成一个form，用这个form提交参数，并返回“流”类型的数据；或者使用a标签：
	 * <pre>
	 * 	<a href="" download="test.txt">
	 * </pre>
	 * 可以弹出文件保存框。
	 * 
	 * 导出excel并保存到客户端
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/export")
	public Object export(HttpServletRequest request, HttpServletResponse response, @RequestParam("token") String token) {
		boolean flag = false;
		response.setContentType("application/octet-stream");
		Integer userId = CommonTools.getUserIdByToken(token);

		try {
			List<Student> list = new ArrayList<>();
			
			String filename = "学生成绩表";
			String sheetName = "学生成绩表";
			
			String[] heads = {"姓名", "年龄", "分数", "备注"};
			
			int size = heads.length;
			Object[][] datas = new Object[list.size()][size];
			int index = 0;
			for (Student student : list) {
				datas[index][0] = student.getName();
				datas[index][1] = student.getAge();
				datas[index][2] = student.getScore();
				datas[index][3] = student.getRemark();
				index++;
			}
			
			response.addHeader("Content-Disposition", "attachment; filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1" )+".xls");
			OutputStream out = response.getOutputStream();
			flag = ExcelTools.export(sheetName, heads, datas, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
}

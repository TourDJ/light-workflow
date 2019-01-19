package net.tang.workflow.service;

import java.util.Map;

public interface UserService extends BaseService {

	Map<Integer, String> loginUsers();
}

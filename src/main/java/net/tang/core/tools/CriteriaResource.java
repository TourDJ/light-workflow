/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tang.core.tools;

/**
 * Criteria definition interface for every business model at here.
 * @author tang
 * @since 2018
 */
interface CriteriaResource {

	/**
	 * Exclude fields of query process.
	 */
	public static final String[] NOT_INCLUDE_FIELDS = { "serialVersionUID", "tree" };
	
	/**
	 * 退货申请
	 */
	public static final String[] SEARCH_FIELDS_CERTIFICATE_BLUR = { "product" };
	public static final String[] SEARCH_FIELDS_CERTIFICATE_SECTION = { "apply" };
	public static final String[] SEARCH_FIELDS_CERTIFICATE_EQUAL = { "workflowStatus" };
	
}

package com.specmate.test.integration;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;
import org.junit.Test;

import com.specmate.common.SpecmateException;
import com.specmate.common.SpecmateValidationException;
import com.specmate.rest.RestClient;
import com.specmate.rest.RestResult;
import com.specmate.test.integration.support.DummyProject;
import com.specmate.test.integration.support.DummyProjectService;
import com.specmate.usermodel.UserSession;

public class AuthenticationTest extends EmfRestTest {
	private JSONObject projectA, projectB;
	private String projectAName, projectBName;
	private JSONObject requirementA, requirementB;

	public AuthenticationTest() throws Exception {
		super();

		// Setup is performed with REST clients that do not require authentication
		projectA = postFolderToRoot();
		projectAName = getId(projectA);
		requirementA = postRequirement(projectAName);

		projectB = postFolderToRoot();
		projectBName = getId(projectB);
		requirementB = postRequirement(projectBName);

		if (projectService instanceof DummyProjectService) {
			DummyProjectService dummyProjectService = (DummyProjectService) projectService;
			dummyProjectService.addProject(new DummyProject(projectAName));
			dummyProjectService.addProject(new DummyProject(projectBName));
		}
	}

	@Test
	public void testUnauthorizedPost() throws SpecmateException, SpecmateValidationException {
		UserSession session = authenticationService.authenticate("resttest", "resttest", projectAName);
		RestClient clientProjectA = new RestClient(REST_ENDPOINT, session.getId(), logService);

		RestResult<JSONObject> result = clientProjectA.post(listUrl(projectAName), requirementB);
		assertEquals(Status.OK.getStatusCode(), result.getResponse().getStatus());

		result = clientProjectA.post(listUrl(projectBName), requirementA);
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), result.getResponse().getStatus());
	}

	@Test
	public void testUnauthorizedGet() throws SpecmateException, SpecmateValidationException {
		UserSession session = authenticationService.authenticate("resttest", "resttest", projectAName);
		RestClient clientProjectA = new RestClient(REST_ENDPOINT, session.getId(), logService);

		RestResult<JSONObject> result = clientProjectA.get(detailUrl(projectAName));
		assertEquals(Status.OK.getStatusCode(), result.getResponse().getStatus());

		result = clientProjectA.get(projectBName);
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), result.getResponse().getStatus());
	}
}

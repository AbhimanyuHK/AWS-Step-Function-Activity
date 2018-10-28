/**
 * 
 */
package com.cxt;

import java.util.concurrent.TimeUnit;

/**
 * @author abhimanyu_h_k
 *
 */
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskRequest;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import com.amazonaws.services.stepfunctions.model.SendTaskFailureRequest;
import com.amazonaws.services.stepfunctions.model.SendTaskSuccessRequest;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

public class GreeterActivities {

	private static final String ACTIVITY_ARN = "arn:aws:states:us-west-2:337226440425:activity:Hello";

	public String getGreeting(String who) throws Exception {
		return "{\"Hello\": \"" + who + "\"}";
	}

	public static void main(final String[] args) throws Exception {
		
		GreeterActivities greeterActivities = new GreeterActivities();
		
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setSocketTimeout((int) TimeUnit.SECONDS.toMillis(70));

		AWSStepFunctions client = AWSStepFunctionsClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new DefaultAWSCredentialsProviderChain())
				.withClientConfiguration(clientConfiguration).build();

		while (true) {
			GetActivityTaskResult getActivityTaskResult = client
					.getActivityTask(new GetActivityTaskRequest().withActivityArn(ACTIVITY_ARN));

			if (getActivityTaskResult.getTaskToken() != null) {
				try {
					JsonNode json = Jackson.jsonNodeOf(getActivityTaskResult.getInput());
					String greetingResult = greeterActivities.getGreeting(json.get("who").textValue());
					client.sendTaskSuccess(new SendTaskSuccessRequest().withOutput(greetingResult)
							.withTaskToken(getActivityTaskResult.getTaskToken()));
				} catch (Exception e) {
					client.sendTaskFailure(
							new SendTaskFailureRequest().withTaskToken(getActivityTaskResult.getTaskToken()));
				}
			} else {
				Thread.sleep(1000);
			}
		}
	}
}

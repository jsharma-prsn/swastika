package edu.training.jsharma;

import java.io.IOException;
import java.util.Map;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class SQSServiceListener {
  @SqsListener(value = "<sqs endpoint>", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receive(Map<String, Object> message) throws Exception {
    try {
      System.out.println("Message" + message);

      // filter s3 events
      if (!"NA".equals(message.getOrDefault("Records", "NA"))) {
        System.out.println("S3 event: " + message);
      } else {
        throw new Exception("Invalid message:" + message);
      }

    } catch (IOException e) {
      throw new Exception(e.getMessage());
    }
  }
}

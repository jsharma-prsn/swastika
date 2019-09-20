package edu.jsharma.training;

import org.apache.beam.repackaged.beam_runners_core_construction_java.com.google.common.collect.ImmutableMap;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.Values;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ApacheBeamKafkaIntegration {
  public static void main(String[] args) {

    PipelineOptions options = PipelineOptionsFactory.create();

    Pipeline p = Pipeline.create(options);

    p.apply(
            KafkaIO.<Long, String>read()
                .withBootstrapServers("localhost:9092")
                .withTopic(
                    "USER_TEST") // use withTopics(List<String>) to read from multiple topics.
                .withKeyDeserializer(LongDeserializer.class)
                .withValueDeserializer(StringDeserializer.class)
                // .withStartReadTime(new Instant())

                // Above four are required configuration. returns PCollection<KafkaRecord<Long,
                // String>>

                // Rest of the settings are optional :

                // you can further customize KafkaConsumer used to read the records by adding more
                // settings for ConsumerConfig. e.g :
                .updateConsumerProperties(ImmutableMap.of("group.id", "my_beam_app"))

                // set event times and watermark based on LogAppendTime. To provide a custom
                // policy see withTimestampPolicyFactory(). withProcessingTime() is the default.
                // .withLogAppendTime()

                // restrict reader to committed messages on Kafka (see method documentation).
                .withReadCommitted()

                // offset consumed by the pipeline can be committed back.
                .commitOffsetsInFinalize()

                // finally, if you don't need Kafka metadata, you can drop it.g
                .withoutMetadata() // PCollection<KV<Long, String>>
            )
        .apply(Values.<String>create())
        .apply(MapElements.via(new PrintFn()));

    p.run().waitUntilFinish();
  }

  static class PrintFn extends SimpleFunction<String, String> {

    private static final long serialVersionUID = 1L;

    @Override
    public String apply(String record) {
      System.out.println("Received message:" + record);
      return "" + record;
    }
  }
}

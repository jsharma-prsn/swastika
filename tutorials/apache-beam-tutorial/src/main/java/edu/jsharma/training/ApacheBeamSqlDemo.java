package edu.jsharma.training;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.sql.SqlTransform;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import java.util.stream.Collectors;

public class ApacheBeamSqlDemo {

  public static interface ApacheBeamSqlDemoOption extends PipelineOptions {
    @Description("Input file path")
    @Default.String("inpatientCharges.tsv")
    String getInputFile();

    void setInputFile(String value);

    @Description("Output file path")
    @Required
    String getOutput();

    void setOutput(String value);
  }

  public static class print extends DoFn<Row, String> {
    @ProcessElement
    public void processElement(ProcessContext c) {
      String line =
          c.element().getValues().stream().map(Object::toString).collect(Collectors.joining("|"));
      System.out.println(line);
      c.output(line);
    }
  }

  static class ReadAndSetDataFn extends DoFn<String, Row> {

    Schema schema =
        Schema.builder()
            .addStringField("DRGDefinition")
            .addStringField("ProviderId")
            .addStringField("ProviderName")
            .addStringField("ProviderStreetAddress")
            .addStringField("ProviderCity")
            .addStringField("ProviderState")
            .addStringField("ProviderZipCode")
            .addStringField("HospitalReferralRegionDescription")
            .addStringField("TotalDischarges")
            .addStringField("AverageCoveredCharges")
            .addStringField("AverageTotalPayments")
            .addStringField("AverageMedicarePayments")
            .build();

    @ProcessElement
    public void processelement(@Element String c, OutputReceiver<Row> output) {
      String[] strArr = c.split("\t");
      if (!strArr[0].equalsIgnoreCase("DRGDefinition")) {
        Row recordRow =
            Row.withSchema(schema)
                .addValues(
                    strArr[0],
                    strArr[1],
                    strArr[2],
                    strArr[3],
                    strArr[4],
                    strArr[5],
                    strArr[6],
                    strArr[7],
                    strArr[8],
                    strArr[9],
                    strArr[10],
                    strArr[11])
                .build();
        output.output(recordRow);
      }
    }
  }

  static void calcualteMetrics(ApacheBeamSqlDemoOption options) {
    Schema schema =
        Schema.builder()
            .addStringField("DRGDefinition")
            .addStringField("ProviderId")
            .addStringField("ProviderName")
            .addStringField("ProviderStreetAddress")
            .addStringField("ProviderCity")
            .addStringField("ProviderState")
            .addStringField("ProviderZipCode")
            .addStringField("HospitalReferralRegionDescription")
            .addStringField("TotalDischarges")
            .addStringField("AverageCoveredCharges")
            .addStringField("AverageTotalPayments")
            .addStringField("AverageMedicarePayments")
            .build();

    Pipeline p = Pipeline.create(options);

    PCollection<Row> t =
        p.apply(TextIO.read().from(options.getInputFile()))
            .apply(ParDo.of(new ReadAndSetDataFn()))
            .setRowSchema(schema);

    PCollection<Row> t2 =
        t.apply(
            SqlTransform.query(
                "select ProviderZipCode , count(*) from PCOLLECTION group by ProviderZipCode"));

    display(t2, 20);

    p.run().waitUntilFinish();
  }

  /** @return */
  private static void display(PCollection<Row> t2, int record) {
    PCollection<Row> t3 = t2.apply(SqlTransform.query("select * from PCOLLECTION limit " + record));
    System.out.println(t3.getSchema().toString());
    t3.apply(
        ParDo.of(
            new DoFn<Row, String>() {
              @ProcessElement
              public void processElement(ProcessContext c) {
                String line =
                    c.element()
                        .getValues()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("|"));

                System.out.println("|" + line + "|");
                c.output(line);
              }
            }));
  }

  public static void main(String[] args) {
    System.out.println("Welcome to Sql API for Apache Beam");
    ApacheBeamSqlDemoOption option =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(ApacheBeamSqlDemoOption.class);
    calcualteMetrics(option);
  }
}

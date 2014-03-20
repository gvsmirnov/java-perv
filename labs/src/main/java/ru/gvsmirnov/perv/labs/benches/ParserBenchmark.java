package ru.gvsmirnov.perv.labs.benches;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.logic.BlackHole;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class ParserBenchmark {

    @GenerateMicroBenchmark
    public void measureBaseline(Json json, BlackHole bh) {
        json.text.chars().forEach(bh::consume);
    }

    @GenerateMicroBenchmark
    public void measureSax_baseline_no_filter(Json json, BlackHole bh) throws IOException {
        JsonToken token = json.parser.nextToken();

        while ((token = json.parser.nextToken()) != null) {
            String key = json.parser.getText();

            json.parser.nextToken();

            String value = json.parser.getText();

            bh.consume(key);
            bh.consume(value);
        }
    }

    @GenerateMicroBenchmark
    public void measureSax_baseline(Json json, BlackHole bh) throws IOException {
        JsonToken token = json.parser.nextToken();

        while ((token = json.parser.nextToken()) != null) {

            if(token != JsonToken.FIELD_NAME) {
                break;
            }

            String key = json.parser.getText();

            json.parser.nextToken();

            String value = json.parser.getText();

            bh.consume(key);
            bh.consume(value);
        }
    }

    @GenerateMicroBenchmark
    public void measureSax_token_consumed_no_filter(Json json, BlackHole bh) throws IOException {
        JsonToken token = json.parser.nextToken();

        while ((token = json.parser.nextToken()) != null) {

            bh.consume(token);

            String key = json.parser.getText();

            json.parser.nextToken();

            String value = json.parser.getText();

            bh.consume(key);
            bh.consume(value);
        }
    }

    @GenerateMicroBenchmark
    public void measureSax_token_consumed(Json json, BlackHole bh) throws IOException {
        JsonToken token = json.parser.nextToken();

        while ((token = json.parser.nextToken()) != null) {

            bh.consume(token);

            if(token != JsonToken.FIELD_NAME) {
                break;
            }

            String key = json.parser.getText();

            json.parser.nextToken();

            String value = json.parser.getText();

            bh.consume(key);
            bh.consume(value);
        }
    }

    @GenerateMicroBenchmark
    public Map<String, String> measureSax_map_returned_no_filter(Json json) throws IOException {
        Map<String, String> result = new HashMap<>();

        JsonToken token = json.parser.nextToken();
        while ((token = json.parser.nextToken()) != null) {
            String key = json.parser.getText();

            json.parser.nextToken();

            String value = json.parser.getText();

            result.put(key, value);
        }

        return result;
    }

    @GenerateMicroBenchmark
    public Map<String, String> measureSax_map_returned(Json json) throws IOException {
        Map<String, String> result = new HashMap<>();

        JsonToken token = json.parser.nextToken();
        while ((token = json.parser.nextToken()) != null) {

            if(token != JsonToken.FIELD_NAME) {
                break;
            }

            String key = json.parser.getText();

            json.parser.nextToken();

            String value = json.parser.getText();

            result.put(key, value);
        }

        return result;
    }

    @State(Scope.Thread)
    public static class Json {

        @Param({"1", "256", "65536"})
        public long size;


        private String text;
        private JsonParser parser;

        @Setup
        public void createText() {
            StringBuilder builder = new StringBuilder("{");

            for(int i = 0; i < size; i ++) {
                builder.append("\"key-" + i + "\": " + i + " ,");
            }

            builder.append("\"dummy\": -1}");

            text = builder.toString();
        }

        @Setup(Level.Invocation)
        public void init() throws IOException {
            parser = new JsonFactory().createParser(text);
        }
    }

/*
Benchmark                                                       (size)   Mode   Samples         Mean   Mean error    Units
r.g.p.l.b.ParserBenchmark.measureBaseline                            1   avgt        10        0.220        0.001    us/op

r.g.p.l.b.ParserBenchmark.measureSax_baseline                        1   avgt        10        0.446        0.004    us/op
r.g.p.l.b.ParserBenchmark.measureSax_baseline_no_filter              1   avgt        10        0.371        0.002    us/op

r.g.p.l.b.ParserBenchmark.measureSax_map_returned                    1   avgt        10        0.531        0.058    us/op
r.g.p.l.b.ParserBenchmark.measureSax_map_returned_no_filter          1   avgt        10        0.412        0.001    us/op

r.g.p.l.b.ParserBenchmark.measureSax_token_consumed                  1   avgt        10        0.458        0.018    us/op
r.g.p.l.b.ParserBenchmark.measureSax_token_consumed_no_filter        1   avgt        10        0.390        0.017    us/op

r.g.p.l.b.ParserBenchmark.measureBaseline                          256   avgt        10        8.276        0.052    us/op

r.g.p.l.b.ParserBenchmark.measureSax_baseline                      256   avgt        10       76.978        0.586    us/op
r.g.p.l.b.ParserBenchmark.measureSax_baseline_no_filter            256   avgt        10       77.333        0.418    us/op

r.g.p.l.b.ParserBenchmark.measureSax_map_returned                  256   avgt        10       84.397        1.748    us/op
r.g.p.l.b.ParserBenchmark.measureSax_map_returned_no_filter        256   avgt        10       87.104        4.855    us/op

r.g.p.l.b.ParserBenchmark.measureSax_token_consumed                256   avgt        10       81.227        2.121    us/op
r.g.p.l.b.ParserBenchmark.measureSax_token_consumed_no_filter      256   avgt        10       77.566        1.469    us/op

r.g.p.l.b.ParserBenchmark.measureBaseline                        65536   avgt        10     2687.278       25.963    us/op

r.g.p.l.b.ParserBenchmark.measureSax_baseline                    65536   avgt        10    29765.424      495.534    us/op
r.g.p.l.b.ParserBenchmark.measureSax_baseline_no_filter          65536   avgt        10    29047.454     1062.211    us/op

r.g.p.l.b.ParserBenchmark.measureSax_map_returned                65536   avgt        10    34761.547     1806.040    us/op
r.g.p.l.b.ParserBenchmark.measureSax_map_returned_no_filter      65536   avgt        10    31902.658     1019.505    us/op

r.g.p.l.b.ParserBenchmark.measureSax_token_consumed              65536   avgt        10    30258.426     1009.782    us/op
r.g.p.l.b.ParserBenchmark.measureSax_token_consumed_no_filter    65536   avgt        10    30661.869      788.414    us/op
 */
}
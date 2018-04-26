/*
 *  * Copyright 2017 Skymind, Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 */

package org.datavec.api.transform.serde.legacy;

import org.datavec.api.transform.Transform;
import org.datavec.api.transform.analysis.columns.*;
import org.datavec.api.transform.condition.BooleanCondition;
import org.datavec.api.transform.condition.Condition;
import org.datavec.api.transform.condition.column.*;
import org.datavec.api.transform.condition.sequence.SequenceLengthCondition;
import org.datavec.api.transform.condition.string.StringRegexColumnCondition;
import org.datavec.api.transform.filter.ConditionFilter;
import org.datavec.api.transform.filter.Filter;
import org.datavec.api.transform.filter.FilterInvalidValues;
import org.datavec.api.transform.filter.InvalidNumColumns;
import org.datavec.api.transform.metadata.*;
import org.datavec.api.transform.ndarray.NDArrayColumnsMathOpTransform;
import org.datavec.api.transform.ndarray.NDArrayDistanceTransform;
import org.datavec.api.transform.ndarray.NDArrayMathFunctionTransform;
import org.datavec.api.transform.ndarray.NDArrayScalarOpTransform;
import org.datavec.api.transform.rank.CalculateSortedRank;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.schema.SequenceSchema;
import org.datavec.api.transform.sequence.ReduceSequenceTransform;
import org.datavec.api.transform.sequence.SequenceComparator;
import org.datavec.api.transform.sequence.SequenceSplit;
import org.datavec.api.transform.sequence.comparator.NumericalColumnComparator;
import org.datavec.api.transform.sequence.comparator.StringComparator;
import org.datavec.api.transform.sequence.split.SequenceSplitTimeSeparation;
import org.datavec.api.transform.sequence.split.SplitMaxLengthSequence;
import org.datavec.api.transform.sequence.trim.SequenceTrimTransform;
import org.datavec.api.transform.sequence.window.OverlappingTimeWindowFunction;
import org.datavec.api.transform.sequence.window.ReduceSequenceByWindowTransform;
import org.datavec.api.transform.sequence.window.TimeWindowFunction;
import org.datavec.api.transform.sequence.window.WindowFunction;
import org.datavec.api.transform.stringreduce.IStringReducer;
import org.datavec.api.transform.stringreduce.StringReducer;
import org.datavec.api.transform.transform.categorical.*;
import org.datavec.api.transform.transform.column.*;
import org.datavec.api.transform.transform.condition.ConditionalCopyValueTransform;
import org.datavec.api.transform.transform.condition.ConditionalReplaceValueTransform;
import org.datavec.api.transform.transform.condition.ConditionalReplaceValueTransformWithDefault;
import org.datavec.api.transform.transform.doubletransform.*;
import org.datavec.api.transform.transform.integer.*;
import org.datavec.api.transform.transform.longtransform.LongColumnsMathOpTransform;
import org.datavec.api.transform.transform.longtransform.LongMathOpTransform;
import org.datavec.api.transform.transform.nlp.TextToCharacterIndexTransform;
import org.datavec.api.transform.transform.parse.ParseDoubleTransform;
import org.datavec.api.transform.transform.sequence.SequenceDifferenceTransform;
import org.datavec.api.transform.transform.sequence.SequenceMovingWindowReduceTransform;
import org.datavec.api.transform.transform.sequence.SequenceOffsetTransform;
import org.datavec.api.transform.transform.string.*;
import org.datavec.api.transform.transform.time.DeriveColumnsFromTimeTransform;
import org.datavec.api.transform.transform.time.StringToTimeTransform;
import org.datavec.api.transform.transform.time.TimeMathOpTransform;
import org.datavec.api.writable.*;
import org.datavec.api.writable.comparator.*;
import org.nd4j.shade.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class LegacyMappingHelper {
    
    private static Map<String,String> getLegacyMappingTransform(){
        
        //The following classes all used their class short name
        Map<String,String> m = new HashMap<>();
        m.put("CategoricalToIntegerTransform", CategoricalToIntegerTransform.class.getName());
        m.put("CategoricalToOneHotTransform", CategoricalToOneHotTransform.class.getName());
        m.put("IntegerToCategoricalTransform", IntegerToCategoricalTransform.class.getName());
        m.put("StringToCategoricalTransform", StringToCategoricalTransform.class.getName());
        m.put("DuplicateColumnsTransform", DuplicateColumnsTransform.class.getName());
        m.put("RemoveColumnsTransform", RemoveColumnsTransform.class.getName());
        m.put("RenameColumnsTransform", RenameColumnsTransform.class.getName());
        m.put("ReorderColumnsTransform", ReorderColumnsTransform.class.getName());
        m.put("ConditionalCopyValueTransform", ConditionalCopyValueTransform.class.getName());
        m.put("ConditionalReplaceValueTransform", ConditionalReplaceValueTransform.class.getName());
        m.put("ConditionalReplaceValueTransformWithDefault", ConditionalReplaceValueTransformWithDefault.class.getName());
        m.put("DoubleColumnsMathOpTransform", DoubleColumnsMathOpTransform.class.getName());
        m.put("DoubleMathOpTransform", DoubleMathOpTransform.class.getName());
        m.put("Log2Normalizer", Log2Normalizer.class.getName());
        m.put("MinMaxNormalizer", MinMaxNormalizer.class.getName());
        m.put("StandardizeNormalizer", StandardizeNormalizer.class.getName());
        m.put("SubtractMeanNormalizer", SubtractMeanNormalizer.class.getName());
        m.put("IntegerColumnsMathOpTransform", IntegerColumnsMathOpTransform.class.getName());
        m.put("IntegerMathOpTransform", IntegerMathOpTransform.class.getName());
        m.put("ReplaceEmptyIntegerWithValueTransform", ReplaceEmptyIntegerWithValueTransform.class.getName());
        m.put("ReplaceInvalidWithIntegerTransform", ReplaceInvalidWithIntegerTransform.class.getName());
        m.put("LongColumnsMathOpTransform", LongColumnsMathOpTransform.class.getName());
        m.put("LongMathOpTransform", LongMathOpTransform.class.getName());
        m.put("MapAllStringsExceptListTransform", MapAllStringsExceptListTransform.class.getName());
        m.put("RemoveWhiteSpaceTransform", RemoveWhiteSpaceTransform.class.getName());
        m.put("ReplaceEmptyStringTransform", ReplaceEmptyStringTransform.class.getName());
        m.put("ReplaceStringTransform", ReplaceStringTransform.class.getName());
        m.put("StringListToCategoricalSetTransform", StringListToCategoricalSetTransform.class.getName());
        m.put("StringMapTransform", StringMapTransform.class.getName());
        m.put("DeriveColumnsFromTimeTransform", DeriveColumnsFromTimeTransform.class.getName());
        m.put("StringToTimeTransform", StringToTimeTransform.class.getName());
        m.put("TimeMathOpTransform", TimeMathOpTransform.class.getName());
        m.put("ReduceSequenceByWindowTransform", ReduceSequenceByWindowTransform.class.getName());
        m.put("DoubleMathFunctionTransform", DoubleMathFunctionTransform.class.getName());
        m.put("AddConstantColumnTransform", AddConstantColumnTransform.class.getName());
        m.put("RemoveAllColumnsExceptForTransform", RemoveAllColumnsExceptForTransform.class.getName());
        m.put("ParseDoubleTransform", ParseDoubleTransform.class.getName());
        m.put("ConvertToStringTransform", ConvertToString.class.getName());
        m.put("AppendStringColumnTransform", AppendStringColumnTransform.class.getName());
        m.put("SequenceDifferenceTransform", SequenceDifferenceTransform.class.getName());
        m.put("ReduceSequenceTransform", ReduceSequenceTransform.class.getName());
        m.put("SequenceMovingWindowReduceTransform", SequenceMovingWindowReduceTransform.class.getName());
        m.put("IntegerToOneHotTransform", IntegerToOneHotTransform.class.getName());
        m.put("SequenceTrimTransform", SequenceTrimTransform.class.getName());
        m.put("SequenceOffsetTransform", SequenceOffsetTransform.class.getName());
        m.put("NDArrayColumnsMathOpTransform", NDArrayColumnsMathOpTransform.class.getName());
        m.put("NDArrayDistanceTransform", NDArrayDistanceTransform.class.getName());
        m.put("NDArrayMathFunctionTransform", NDArrayMathFunctionTransform.class.getName());
        m.put("NDArrayScalarOpTransform", NDArrayScalarOpTransform.class.getName());
        m.put("ChangeCaseStringTransform", ChangeCaseStringTransform.class.getName());
        m.put("ConcatenateStringColumns", ConcatenateStringColumns.class.getName());
        m.put("StringListToCountsNDArrayTransform", StringListToCountsNDArrayTransform.class.getName());
        m.put("StringListToIndicesNDArrayTransform", StringListToIndicesNDArrayTransform.class.getName());
        m.put("PivotTransform", PivotTransform.class.getName());
        m.put("TextToCharacterIndexTransform", TextToCharacterIndexTransform.class.getName());

        return m;
    }

    private static Map<String,String> getLegacyMappingColumnAnalysis(){
        Map<String,String> m = new HashMap<>();
        m.put("BytesAnalysis", BytesAnalysis.class.getName());
        m.put("CategoricalAnalysis", CategoricalAnalysis.class.getName());
        m.put("DoubleAnalysis", DoubleAnalysis.class.getName());
        m.put("IntegerAnalysis", IntegerAnalysis.class.getName());
        m.put("LongAnalysis", LongAnalysis.class.getName());
        m.put("StringAnalysis", StringAnalysis.class.getName());
        m.put("TimeAnalysis", TimeAnalysis.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingCondition(){
        Map<String,String> m = new HashMap<>();
        m.put("TrivialColumnCondition", TrivialColumnCondition.class.getName());
        m.put("CategoricalColumnCondition", CategoricalColumnCondition.class.getName());
        m.put("DoubleColumnCondition", DoubleColumnCondition.class.getName());
        m.put("IntegerColumnCondition", IntegerColumnCondition.class.getName());
        m.put("LongColumnCondition", LongColumnCondition.class.getName());
        m.put("NullWritableColumnCondition", NullWritableColumnCondition.class.getName());
        m.put("StringColumnCondition", StringColumnCondition.class.getName());
        m.put("TimeColumnCondition", TimeColumnCondition.class.getName());
        m.put("StringRegexColumnCondition", StringRegexColumnCondition.class.getName());
        m.put("BooleanCondition", BooleanCondition.class.getName());
        m.put("NaNColumnCondition", NaNColumnCondition.class.getName());
        m.put("InfiniteColumnCondition", InfiniteColumnCondition.class.getName());
        m.put("SequenceLengthCondition", SequenceLengthCondition.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingFilter(){
        Map<String,String> m = new HashMap<>();
        m.put("ConditionFilter", ConditionFilter.class.getName());
        m.put("FilterInvalidValues", FilterInvalidValues.class.getName());
        m.put("InvalidNumCols", InvalidNumColumns.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingColumnMetaData(){
        Map<String,String> m = new HashMap<>();
        m.put("Categorical", CategoricalMetaData.class.getName());
        m.put("Double", DoubleMetaData.class.getName());
        m.put("Float", FloatMetaData.class.getName());
        m.put("Integer", IntegerMetaData.class.getName());
        m.put("Long", LongMetaData.class.getName());
        m.put("String", StringMetaData.class.getName());
        m.put("Time", TimeMetaData.class.getName());
        m.put("NDArray", NDArrayMetaData.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingCalculateSortedRank(){
        Map<String,String> m = new HashMap<>();
        m.put("CalculateSortedRank", CalculateSortedRank.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingSchema(){
        Map<String,String> m = new HashMap<>();
        m.put("Schema", Schema.class.getName());
        m.put("SequenceSchema", SequenceSchema.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingSequenceComparator(){
        Map<String,String> m = new HashMap<>();
        m.put("NumericalColumnComparator", NumericalColumnComparator.class.getName());
        m.put("StringComparator", StringComparator.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingSequenceSplit(){
        Map<String,String> m = new HashMap<>();
        m.put("SequenceSplitTimeSeparation", SequenceSplitTimeSeparation.class.getName());
        m.put("SplitMaxLengthSequence", SplitMaxLengthSequence.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingWindowFunction(){
        Map<String,String> m = new HashMap<>();
        m.put("TimeWindowFunction", TimeWindowFunction.class.getName());
        m.put("OverlappingTimeWindowFunction", OverlappingTimeWindowFunction.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingIStringReducer(){
        Map<String,String> m = new HashMap<>();
        m.put("StringReducer", StringReducer.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingWritable(){
        Map<String,String> m = new HashMap<>();
        m.put("ArrayWritable", ArrayWritable.class.getName());
        m.put("BooleanWritable", BooleanWritable.class.getName());
        m.put("ByteWritable", ByteWritable.class.getName());
        m.put("DoubleWritable", DoubleWritable.class.getName());
        m.put("FloatWritable", FloatWritable.class.getName());
        m.put("IntWritable", IntWritable.class.getName());
        m.put("LongWritable", LongWritable.class.getName());
        m.put("NullWritable", NullWritable.class.getName());
        m.put("Text", Text.class.getName());
        m.put("BytesWritable", BytesWritable.class.getName());
        return m;
    }

    private static Map<String,String> getLegacyMappingWritableComparator(){
        Map<String,String> m = new HashMap<>();
        m.put("DoubleWritableComparator", DoubleWritableComparator.class.getName());
        m.put("FloatWritableComparator", FloatWritableComparator.class.getName());
        m.put("IntWritableComparator", IntWritableComparator.class.getName());
        m.put("LongWritableComparator", LongWritableComparator.class.getName());
        m.put("TextWritableComparator", TextWritableComparator.class.getName());
        return m;
    }

    public static Map<String,String> getLegacyMappingImageTransform(){
        Map<String,String> m = new HashMap<>();
        m.put("EqualizeHistTransform", "org.datavec.image.transform.EqualizeHistTransform");
        m.put("RotateImageTransform", "org.datavec.image.transform.RotateImageTransform");
        m.put("ColorConversionTransform", "org.datavec.image.transform.ColorConversionTransform");
        m.put("WarpImageTransform", "org.datavec.image.transform.WarpImageTransform");
        m.put("BoxImageTransform", "org.datavec.image.transform.BoxImageTransform");
        m.put("CropImageTransform", "org.datavec.image.transform.CropImageTransform");
        m.put("FilterImageTransform", "org.datavec.image.transform.FilterImageTransform");
        m.put("FlipImageTransform", "org.datavec.image.transform.FlipImageTransform");
        m.put("LargestBlobCropTransform", "org.datavec.image.transform.LargestBlobCropTransform");
        m.put("ResizeImageTransform", "org.datavec.image.transform.ResizeImageTransform");
        m.put("RandomCropTransform", "org.datavec.image.transform.RandomCropTransform");
        m.put("ScaleImageTransform", "org.datavec.image.transform.ScaleImageTransform");
        return m;
    }

    public static void main(String[] args) {
        String s = "@JsonSubTypes.Type(value = ColorConversionTransform.class, name = \"ColorConversionTransform\"),\n" +
                "                @JsonSubTypes.Type(value = BoxImageTransform.class, name = \"BoxImageTransform\"),\n" +
                "                @JsonSubTypes.Type(value = CropImageTransform.class, name = \"CropImageTransform\"),\n" +
                "                @JsonSubTypes.Type(value = EqualizeHistTransform.class, name = \"EqualizeHistTransform\"),\n" +
                "                @JsonSubTypes.Type(value = FilterImageTransform.class, name = \"FilterImageTransform\"),\n" +
                "                @JsonSubTypes.Type(value = FlipImageTransform.class, name = \"FlipImageTransform\"),\n" +
                "                @JsonSubTypes.Type(value = LargestBlobCropTransform.class, name = \"LargestBlobCropTransform\"),\n" +
                "                @JsonSubTypes.Type(value = RandomCropTransform.class, name = \"RandomCropTransform\"),\n" +
                "                @JsonSubTypes.Type(value = ResizeImageTransform.class, name = \"ResizeImageTransform\"),\n" +
                "                @JsonSubTypes.Type(value = RotateImageTransform.class, name = \"RotateImageTransform\"),\n" +
                "                @JsonSubTypes.Type(value = ScaleImageTransform.class, name = \"ScaleImageTransform\"),\n" +
                "                @JsonSubTypes.Type(value = WarpImageTransform.class, name = \"WarpImageTransform\")";

        String[] str = s.split("\n");
        for(String s2 : str){
            String[] str2 = s2.split(",");
            int first = str2[0].indexOf(" = ");
            int second = str2[0].indexOf(".class");

            String className = str2[0].substring(first+3, second);

            int a = str2[1].indexOf("\"");
            int b = str2[1].indexOf("\"", a+1);
            String oldName = str2[1].substring(a+1,b);

            System.out.println("m.put(\"" + oldName + "\", " + className + ".class.getName());");
        }
    }

    @JsonDeserialize(using = LegacyTransformDeserializer.class)
    public static class TransformHelper { }

    public static class LegacyTransformDeserializer extends GenericLegacyDeserializer<Transform> {
        public LegacyTransformDeserializer() {
            super(Transform.class, getLegacyMappingTransform());
        }
    }

    @JsonDeserialize(using = LegacyColumnAnalysisDeserializer.class)
    public static class ColumnAnalysisHelper { }

    public static class LegacyColumnAnalysisDeserializer extends GenericLegacyDeserializer<ColumnAnalysis> {
        public LegacyColumnAnalysisDeserializer() {
            super(ColumnAnalysis.class, getLegacyMappingColumnAnalysis());
        }
    }

    @JsonDeserialize(using = LegacyConditionDeserializer.class)
    public static class ConditionHelper { }

    public static class LegacyConditionDeserializer extends GenericLegacyDeserializer<Condition> {
        public LegacyConditionDeserializer() {
            super(Condition.class, getLegacyMappingCondition());
        }
    }

    @JsonDeserialize(using = LegacyFilterDeserializer.class)
    public static class FilterHelper { }

    public static class LegacyFilterDeserializer extends GenericLegacyDeserializer<Filter> {
        public LegacyFilterDeserializer() {
            super(Filter.class, getLegacyMappingFilter());
        }
    }

    @JsonDeserialize(using = LegacyColumnMetaDataDeserializer.class)
    public static class ColumnMetaDataHelper { }

    public static class LegacyColumnMetaDataDeserializer extends GenericLegacyDeserializer<ColumnMetaData> {
        public LegacyColumnMetaDataDeserializer() {
            super(ColumnMetaData.class, getLegacyMappingColumnMetaData());
        }
    }

    @JsonDeserialize(using = LegacyCalculateSortedRankDeserializer.class)
    public static class CalculateSortedRankHelper { }

    public static class LegacyCalculateSortedRankDeserializer extends GenericLegacyDeserializer<CalculateSortedRank> {
        public LegacyCalculateSortedRankDeserializer() {
            super(CalculateSortedRank.class, getLegacyMappingCalculateSortedRank());
        }
    }

    @JsonDeserialize(using = LegacySchemaDeserializer.class)
    public static class SchemaHelper { }

    public static class LegacySchemaDeserializer extends GenericLegacyDeserializer<Schema> {
        public LegacySchemaDeserializer() {
            super(Schema.class, getLegacyMappingSchema());
        }
    }

    @JsonDeserialize(using = LegacySequenceComparatorDeserializer.class)
    public static class SequenceComparatorHelper { }

    public static class LegacySequenceComparatorDeserializer extends GenericLegacyDeserializer<SequenceComparator> {
        public LegacySequenceComparatorDeserializer() {
            super(SequenceComparator.class, getLegacyMappingSequenceComparator());
        }
    }

    @JsonDeserialize(using = LegacySequenceSplitDeserializer.class)
    public static class SequenceSplitHelper { }

    public static class LegacySequenceSplitDeserializer extends GenericLegacyDeserializer<SequenceSplit> {
        public LegacySequenceSplitDeserializer() {
            super(SequenceSplit.class, getLegacyMappingSequenceSplit());
        }
    }

    @JsonDeserialize(using = LegacyWindowFunctionDeserializer.class)
    public static class WindowFunctionHelper { }

    public static class LegacyWindowFunctionDeserializer extends GenericLegacyDeserializer<WindowFunction> {
        public LegacyWindowFunctionDeserializer() {
            super(WindowFunction.class, getLegacyMappingWindowFunction());
        }
    }


    @JsonDeserialize(using = LegacyIStringReducerDeserializer.class)
    public static class IStringReducerHelper { }

    public static class LegacyIStringReducerDeserializer extends GenericLegacyDeserializer<IStringReducer> {
        public LegacyIStringReducerDeserializer() {
            super(IStringReducer.class, getLegacyMappingIStringReducer());
        }
    }


    @JsonDeserialize(using = LegacyWritableDeserializer.class)
    public static class WritableHelper { }

    public static class LegacyWritableDeserializer extends GenericLegacyDeserializer<Writable> {
        public LegacyWritableDeserializer() {
            super(Writable.class, getLegacyMappingWritable());
        }
    }

    @JsonDeserialize(using = LegacyWritableComparatorDeserializer.class)
    public static class WritableComparatorHelper { }

    public static class LegacyWritableComparatorDeserializer extends GenericLegacyDeserializer<WritableComparator> {
        public LegacyWritableComparatorDeserializer() {
            super(WritableComparator.class, getLegacyMappingWritableComparator());
        }
    }
}

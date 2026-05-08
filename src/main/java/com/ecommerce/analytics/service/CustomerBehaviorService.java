package com.ecommerce.analytics.service;

import com.ecommerce.analytics.ml.CustomerRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerBehaviorService {

    private  Classifier model;
    private Instances dataset;
    public CustomerBehaviorService() {
        // no injection needed
    }


@PostConstruct
    public void init() throws Exception {
        trainModel();
    }

    private void trainModel() throws Exception {

        List<CustomerRecord> records = new CsvToBeanBuilder<CustomerRecord>(
                new FileReader(toPath().toFile())).withType(CustomerRecord.class).build().parse();

        //
        ArrayList<String> genders = extract(records, CustomerRecord::getGender);
        ArrayList<String> cities = extract(records, CustomerRecord::getCity);
        ArrayList<String> categories = extract(records, CustomerRecord::getCategory);
        ArrayList<String> paymentMethods = extract(records, CustomerRecord::getPaymentMethod);
        ArrayList<String> deviceTypes = extract(records, CustomerRecord::getDeviceType);
        ArrayList<String> returningValues = extract(records, CustomerRecord::getIsReturningCustomer);

        ArrayList<Attribute> attributes = new ArrayList<>();

        attributes.add(new Attribute("age"));
        attributes.add(new Attribute("gender", genders));
        attributes.add(new Attribute("city", cities));
        attributes.add(new Attribute("category", categories));
        attributes.add(new Attribute("payment", paymentMethods));
        attributes.add(new Attribute("device", deviceTypes));
        attributes.add(new Attribute("session"));
        attributes.add(new Attribute("pages"));
        attributes.add(new Attribute("delivery"));
        attributes.add(new Attribute("rating"));
        attributes.add(new Attribute("returning", returningValues));

        dataset = new Instances("CustomerBehavior", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);

        for (CustomerRecord r : records) {

            Instance inst = new DenseInstance(dataset.numAttributes());
            inst.setDataset(dataset);

            inst.setValue(0, r.getAge());
            inst.setValue(1, r.getGender());
            inst.setValue(2, r.getCity());
            inst.setValue(3, r.getCategory());
            inst.setValue(4, r.getPaymentMethod());
            inst.setValue(5, r.getDeviceType());
            inst.setValue(6, r.getSessionDuration());
            inst.setValue(7, r.getPagesViewed());
            inst.setValue(8, r.getDeliveryTime());
            inst.setValue(9, r.getRating());
            inst.setValue(10, r.getIsReturningCustomer());

            dataset.add(inst);
        }

        model = new RandomForest();
        model.buildClassifier(dataset);
        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(model, dataset, 10, new Random(1));
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
        System.out.println("Customer behavior model trained ");
    }

    private ArrayList<String> extract(List<CustomerRecord> records,
                                      Function<CustomerRecord, String> getter) {
        return records.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // =========================
    // Prediction
    // =========================
    public String predict(CustomerRecord customerRecord) throws Exception {

        Instance inst = new DenseInstance(dataset.numAttributes());
        inst.setDataset(dataset);

        inst.setValue(0, customerRecord.getAge());
        inst.setValue(1, customerRecord.getGender());
        inst.setValue(2, customerRecord.getCity());
        inst.setValue(3, customerRecord.getCategory());
        inst.setValue(4, customerRecord.getPaymentMethod());
        inst.setValue(5, customerRecord.getDeviceType());
        inst.setValue(6, customerRecord.getSessionDuration());
        inst.setValue(7, customerRecord.getPagesViewed());
        inst.setValue(8, customerRecord.getDeliveryTime());
        inst.setValue(9, customerRecord.getRating());
        inst.setMissing(10);

        double prediction = model.classifyInstance(inst);

        return dataset.classAttribute().value((int) prediction);
    }
    private static Path toPath() {
        try {
            URL fileUrl = EcommerceService.class.getClassLoader().getResource("ecommerce_customers_2025_2026.csv");
            assert fileUrl != null;
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}


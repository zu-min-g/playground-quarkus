package com.zu_min.playground.quarkus.extension.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.hibernate.orm.deployment.AdditionalJpaModelBuildItem;
import io.quarkus.resteasy.reactive.spi.CustomContainerRequestFilterBuildItem;

import com.zu_min.playground.quarkus.extension.runtime.Fruit;
import com.zu_min.playground.quarkus.extension.runtime.MyService;
import com.zu_min.playground.quarkus.extension.runtime.ReactiveRequestFilter;

class ExtensionProcessor {

    private static final String FEATURE = "my-extension";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void additionalBean(BuildProducer<AdditionalBeanBuildItem> beans) {
        beans.produce(AdditionalBeanBuildItem.builder().addBeanClasses(MyService.class).build());
    }

    @BuildStep
    void filter(BuildProducer<CustomContainerRequestFilterBuildItem> filters) {
        filters.produce(
                new CustomContainerRequestFilterBuildItem(ReactiveRequestFilter.class.getName()));
    }

    @BuildStep
    void entity(BuildProducer<AdditionalJpaModelBuildItem> entities) {
        entities.produce(new AdditionalJpaModelBuildItem(Fruit.class.getName()));
    }

    @BuildStep
    void entityIndex(BuildProducer<AdditionalIndexedClassesBuildItem> entities) {
        entities.produce(new AdditionalIndexedClassesBuildItem(Fruit.class.getName()));
    }
}

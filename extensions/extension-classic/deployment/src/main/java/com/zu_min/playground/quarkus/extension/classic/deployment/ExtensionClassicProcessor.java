package com.zu_min.playground.quarkus.extension.classic.deployment;

import com.zu_min.playground.quarkus.extension.classic.runtime.ClassicRequestFilter;
import com.zu_min.playground.quarkus.extension.classic.runtime.ClassicResponseFilter;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.resteasy.common.spi.ResteasyJaxrsProviderBuildItem;

class ExtensionClassicProcessor {

    private static final String FEATURE = "extension-classic";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void filter(BuildProducer<ResteasyJaxrsProviderBuildItem> filters) {
        filters.produce(new ResteasyJaxrsProviderBuildItem(ClassicRequestFilter.class.getName()));
        filters.produce(new ResteasyJaxrsProviderBuildItem(ClassicResponseFilter.class.getName()));
    }
}

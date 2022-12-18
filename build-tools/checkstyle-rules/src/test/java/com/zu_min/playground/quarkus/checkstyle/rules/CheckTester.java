package com.zu_min.playground.quarkus.checkstyle.rules;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class CheckTester {
    DefaultConfiguration createConfig(Class<?> clazz) {
        return new DefaultConfiguration(clazz.getName());
    }

    DefaultConfiguration createTreeWalkerConfig(Configuration config) {
        final DefaultConfiguration dc = new DefaultConfiguration("configuration");
        final DefaultConfiguration treeWorkerConfig = new DefaultConfiguration(TreeWalker.class.getName());
        dc.addProperty("charset", StandardCharsets.UTF_8.name());
        dc.addChild(treeWorkerConfig);
        treeWorkerConfig.addChild(config);
        return dc;
    }

    static class Listener implements AuditListener {

        List<AuditEvent> errors = new ArrayList<>();

        public List<AuditEvent> getErrors() {
            return new ArrayList<>(errors);
        }

        @Override
        public void auditStarted(AuditEvent event) {
        }

        @Override
        public void auditFinished(AuditEvent event) {

        }

        @Override
        public void fileStarted(AuditEvent event) {
        }

        @Override
        public void fileFinished(AuditEvent event) {
        }

        @Override
        public void addError(AuditEvent event) {
            errors.add(event);
        }

        @Override
        public void addException(AuditEvent event, Throwable throwable) {
        }

    }

    protected List<AuditEvent> verify(List<File> files, Configuration config) throws CheckstyleException {
        final var checker = new Checker();
        try {
            checker.setModuleClassLoader(Thread.currentThread().getContextClassLoader());
            checker.configure(config);
            var listener = new Listener();
            checker.addListener(listener);
            checker.process(files);
            return listener.getErrors();
        } finally {
            checker.destroy();
        }
    }

    protected File getFile(Class<?> clazz, String path) {
        return new File(clazz.getResource(path).getPath());
    }
}

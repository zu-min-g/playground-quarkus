package com.zu_min.playground.quarkus.checkstyle.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import org.junit.jupiter.api.Test;

/**
 * {@link ResourceClassCheck} のテスト。
 */
class ResourceClassCheckTest extends CheckTester {

    @Test
    void testNoModifiers() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_no_modifiers.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(0, errors.size());
    }

    @Test
    void testCorrectCase() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_correct.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(0, errors.size());
    }

    @Test
    void testCorrectJakartaCase() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_correct_jakarta.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(0, errors.size());
    }

    @Test
    void testCorrectNotRestAnnotationCase() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_correct_not_rest.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(0, errors.size());
    }

    @Test
    void testCorrectWithoutImportCase() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_correct_without_import.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(0, errors.size());
    }

    @Test
    void testIncorrectCase() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_incorrect.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(2, errors.size());

        var error1 = errors.get(0);
        assertEquals(6, error1.getLine());
        assertEquals("リソースクラスとメソッドは public にする必要があります。", error1.getViolation().getViolation());

        var error2 = errors.get(1);
        assertEquals(8, error2.getLine());
        assertEquals("リソースクラスとメソッドは public にする必要があります。", error2.getViolation().getViolation());

    }

    @Test
    void testIncorrectWithoutImportCase() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_incorrect_without_import.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(2, errors.size());

        var error1 = errors.get(0);
        assertEquals(4, error1.getLine());
        assertEquals("リソースクラスとメソッドは public にする必要があります。", error1.getViolation().getViolation());

        var error2 = errors.get(1);
        assertEquals(6, error2.getLine());
        assertEquals("リソースクラスとメソッドは public にする必要があります。", error2.getViolation().getViolation());

    }

    @Test
    void testIncorrectJakartaCase() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_incorrect_jakarta.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(2, errors.size());

        var error1 = errors.get(0);
        assertEquals(7, error1.getLine());
        assertEquals("リソースクラスとメソッドは public にする必要があります。", error1.getViolation().getViolation());

        var error2 = errors.get(1);
        assertEquals(9, error2.getLine());
        assertEquals("リソースクラスとメソッドは public にする必要があります。", error2.getViolation().getViolation());

    }

    @Test
    void testMissingPathAtClass() throws CheckstyleException {
        var errors = verify(List.of(
                getFile(ResourceClassCheckTest.class, "ResourceClass_missing_path.java")),
                createTreeWalkerConfig(createConfig(ResourceClassCheck.class)));

        assertEquals(1, errors.size());

        var error1 = errors.get(0);
        assertEquals(6, error1.getLine());
        assertEquals("クラスにも @Path アノテーションを付ける必要があります。", error1.getViolation().getViolation());
    }
}

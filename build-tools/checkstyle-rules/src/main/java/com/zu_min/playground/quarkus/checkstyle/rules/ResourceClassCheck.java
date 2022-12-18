package com.zu_min.playground.quarkus.checkstyle.rules;

import java.util.Objects;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * ルール。
 */
public class ResourceClassCheck extends AbstractCheck {

    static final String ANNOTATION_NAME_PATH = "Path";
    static final String ANNOTATION_NAME_PATH_JAVAX = "javax.ws.rs.Path";
    static final String ANNOTATION_NAME_PATH_JAKARTA = "jakarta.ws.rs.Path";

    @Override
    public int[] getDefaultTokens() {
        return new int[] { TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.METHOD_DEF };
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[] { TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.METHOD_DEF,
                TokenTypes.ANNOTATION };
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[] { TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.METHOD_DEF,
                TokenTypes.ANNOTATION };
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getType() == TokenTypes.CLASS_DEF) {
            if (!isPublic(ast) && hasPathAnnotation(ast)) {
                // 言及するのは class に対して public がついていないことなので、 LITERAL_CLASS を記録する
                // CLASS_DEF だとアノテーションの開始部分の行数になる
                var classLiteral = ast.findFirstToken(TokenTypes.LITERAL_CLASS);
                log(classLiteral, "resource.path.should.be.public");
            }
        } else if (ast.getType() == TokenTypes.METHOD_DEF) {
            if (hasPathAnnotation(ast)) {
                if (!isPublic(ast)) {
                    // 言及するのはメソッドに対して public がついていないことなので、 IDENT (メソッド名) を記録する
                    // METHOD_DEF だとアノテーションの開始部分の行数になる
                    var classLiteral = ast.findFirstToken(TokenTypes.IDENT);
                    log(classLiteral, "resource.path.should.be.public");
                }
                var classDef = ast.getParent().getParent();
                if (classDef.getType() != TokenTypes.CLASS_DEF) {
                    throw new IllegalStateException("クラスの取得に失敗しました。");
                }
                if (!hasPathAnnotation(classDef)) {
                    var classLiteral = classDef.findFirstToken(TokenTypes.LITERAL_CLASS);
                    log(classLiteral, "resource.path.missing.path.on.class");
                }
            }
        }
    }

    private DetailAST rootAST;

    @Override
    public void beginTree(DetailAST rootAST) {
        this.rootAST = rootAST;
    }

    boolean isPublic(DetailAST ast) {
        Objects.requireNonNull(ast);
        if (ast.getType() != TokenTypes.CLASS_DEF && ast.getType() != TokenTypes.METHOD_DEF) {
            throw new IllegalArgumentException("ast には CLASS_DEF または METHOD_DEF を指定できます。");
        }

        var modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers == null) {
            return false;
        }
        var isPublic = modifiers.findFirstToken(TokenTypes.LITERAL_PUBLIC);
        return isPublic != null;
    }

    boolean hasPathAnnotation(DetailAST ast) {
        Objects.requireNonNull(ast);
        if (ast.getType() != TokenTypes.CLASS_DEF && ast.getType() != TokenTypes.METHOD_DEF) {
            throw new IllegalArgumentException("ast には CLASS_DEF または METHOD_DEF を指定できます。");
        }

        var modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers == null) {
            return false;
        }

        var current = modifiers.findFirstToken(TokenTypes.ANNOTATION);
        while (current != null) {
            if (current.getType() == TokenTypes.ANNOTATION
                    && isPathAnnotation(current)) {
                return true;
            }
            current = current.getNextSibling();
        }
        return false;
    }

    boolean isPathAnnotation(DetailAST annotation) {
        Objects.requireNonNull(annotation);
        if (annotation.getType() != TokenTypes.ANNOTATION) {
            throw new IllegalArgumentException("annotation には ANNOTATION 以外を指定できません。");
        }

        var name = getAnnotationName(annotation);
        return ANNOTATION_NAME_PATH_JAVAX.equals(name)
                || ANNOTATION_NAME_PATH_JAKARTA.equals(name)
                || (ANNOTATION_NAME_PATH.equals(name) && hasPathImport());
    }

    boolean hasPathImport() {
        var current = rootAST.findFirstToken(TokenTypes.IMPORT);
        while (current != null) {
            if (current.getType() == TokenTypes.IMPORT) {
                var importName = getImportName(current).toString();
                if (ANNOTATION_NAME_PATH_JAVAX.equals(importName)
                        || ANNOTATION_NAME_PATH_JAKARTA.equals(importName)) {
                    return true;
                }
            }
            current = current.getNextSibling();
        }
        return false;
    }

    String getImportName(DetailAST importAst) {
        Objects.requireNonNull(importAst);
        if (importAst.getType() != TokenTypes.IMPORT) {
            throw new IllegalArgumentException("importAst には IMPORT 以外を指定できません。");
        }

        var first = importAst.getFirstChild();
        var second = first.getNextSibling();
        var result = new StringBuilder();

        if (first.getType() == TokenTypes.DOT) {
            readDot(first, result);
        } else {
            result.append(first.getText());
        }
        if (second.getType() != TokenTypes.SEMI) {
            throw new IllegalStateException("想定していない状態です。");
        }

        return result.toString();
    }

    String getAnnotationName(DetailAST annotation) {
        Objects.requireNonNull(annotation);
        if (annotation.getType() != TokenTypes.ANNOTATION) {
            throw new IllegalArgumentException("annotation には ANNOTATION 以外を指定できません。");
        }

        var first = annotation.getFirstChild();
        var second = first.getNextSibling();
        var result = new StringBuilder();

        if (first.getType() != TokenTypes.AT) {
            throw new IllegalStateException("想定していない状態です。");
        }

        if (second.getType() == TokenTypes.DOT) {
            readDot(second, result);
        } else {
            result.append(second.getText());
        }

        return result.toString();
    }

    void readDot(DetailAST dot, StringBuilder result) {
        Objects.requireNonNull(dot);
        Objects.requireNonNull(result);
        if (dot.getType() != TokenTypes.DOT) {
            throw new IllegalArgumentException("dot には DOT 以外を指定できません。");
        }

        var first = dot.getFirstChild();
        var second = first.getNextSibling();

        if (first.getType() == TokenTypes.DOT) {
            readDot(first, result);
        } else if (first.getType() == TokenTypes.IDENT) {
            result.append(first.getText());
        } else {
            throw new IllegalStateException("想定していない状態です。");
        }

        if (second.getType() != TokenTypes.IDENT) {
            throw new IllegalStateException("想定していない状態です。");
        }
        result.append(".");
        result.append(second.getText());
    }

}

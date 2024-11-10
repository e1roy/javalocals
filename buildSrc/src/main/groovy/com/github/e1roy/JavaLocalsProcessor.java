package com.github.e1roy;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.*;
import spoon.reflect.visitor.CtScanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 21/10/2014.
 */
public class JavaLocalsProcessor extends AbstractProcessor<CtClass> {

    int count = 0;
    static String fillMethodName = "printLocals";
    static boolean showLineNumber = false;

    public JavaLocalsProcessor() {
//        new RuntimeException().printStackTrace();
    }

    public void setFillMethodName(String fillMethodName) {
        this.fillMethodName = fillMethodName;
    }

    @Override
    public void process(CtClass ctClass) {
        count++;
        ctClass.accept(new VariableCollectorVisitor());
//        System.out.println(ctClass);
    }

    public void processCtClassForTest(CtClass clazz) {
        process(clazz);
    }

    @Override
    public void processingDone() {
        super.processingDone();
        System.out.println("JavaLocalsProcessor done    " + count);
    }


    static class VariableCollectorVisitor extends CtScanner {
        private List<CtVariableReference<?>> variablesInScope = new ArrayList<>();
        private boolean isForLoop = false;

        @Override
        public <T> void visitCtMethod(CtMethod<T> m) {
            // Add method parameters to variablesInScope
            m.getParameters().forEach(param -> variablesInScope.add(param.getReference()));
            super.visitCtMethod(m);
            // Clear variablesInScope after finishing the method
            variablesInScope.clear();
        }

        @Override
        public <T> void visitCtBlock(CtBlock<T> block) {
            backUpAndRecover(() -> {
                super.visitCtBlock(block);
            });
        }

        @Override
        public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
            // 循环遍历, 先访问变量,后访问block,并且block中能访问这个变量.
            super.visitCtLocalVariable(localVariable);
            variablesInScope.add(localVariable.getReference());
        }


        @Override
        public <S> void visitCtCase(CtCase<S> caseStatement) {
            backUpAndRecover(() -> super.visitCtCase(caseStatement));
        }

        @Override
        public <S> void visitCtSwitch(CtSwitch<S> switchStatement) {
            backUpAndRecover(() -> super.visitCtSwitch(switchStatement));
        }

        @Override
        public void visitCtFor(CtFor forLoop) {
            var backIsForLoop = this.isForLoop;
            this.isForLoop = true;
            backUpAndRecover(() -> {
                super.visitCtFor(forLoop);
            });
            this.isForLoop = backIsForLoop;
        }

        @Override
        public void visitCtTryWithResource(CtTryWithResource tryWithResource) {
            backUpAndRecover(() -> {
                super.visitCtTryWithResource(tryWithResource);
            });
        }

        @Override
        public void visitCtAnonymousExecutable(CtAnonymousExecutable anonymousExec) {
            /*
            {
                int a = 1;
                printLocals();
            }
            static {
                int staticA = 1;
                printLocals();
            }
             */
            super.visitCtAnonymousExecutable(anonymousExec);
        }

        @Override
        public void visitCtRecordComponent(CtRecordComponent recordType) {
            super.visitCtRecordComponent(recordType);
        }

        @Override
        public void visitCtRecord(CtRecord recordType) {
            super.visitCtRecord(recordType);
        }

        @Override
        public void visitCtTypePattern(CtTypePattern pattern) {
            System.out.println(pattern);
            super.visitCtTypePattern(pattern);
        }

        @Override
        public void visitCtYieldStatement(CtYieldStatement statement) {
            super.visitCtYieldStatement(statement);
        }

        @Override
        public void visitCtTypeMemberWildcardImportReference(CtTypeMemberWildcardImportReference wildcardReference) {
            super.visitCtTypeMemberWildcardImportReference(wildcardReference);
        }

        @Override
        public void visitCtPackageDeclaration(CtPackageDeclaration packageDeclaration) {
            super.visitCtPackageDeclaration(packageDeclaration);
        }

        @Override
        public void visitCtCompilationUnit(CtCompilationUnit compilationUnit) {
            super.visitCtCompilationUnit(compilationUnit);
        }

        @Override
        public void visitCtUsedService(CtUsedService usedService) {
            super.visitCtUsedService(usedService);
        }

        @Override
        public void visitCtProvidedService(CtProvidedService moduleProvidedService) {
            super.visitCtProvidedService(moduleProvidedService);
        }

        @Override
        public void visitCtModuleRequirement(CtModuleRequirement moduleRequirement) {
            super.visitCtModuleRequirement(moduleRequirement);
        }

        @Override
        public void visitCtPackageExport(CtPackageExport moduleExport) {
            super.visitCtPackageExport(moduleExport);
        }

        @Override
        public void visitCtModuleReference(CtModuleReference moduleReference) {
            super.visitCtModuleReference(moduleReference);
        }

        @Override
        public void visitCtModule(CtModule module) {
            super.visitCtModule(module);
        }

        @Override
        public void visitCtImport(CtImport ctImport) {
            super.visitCtImport(ctImport);
        }

        @Override
        public void visitCtJavaDocTag(CtJavaDocTag docTag) {
            super.visitCtJavaDocTag(docTag);
        }

        @Override
        public void visitCtJavaDoc(CtJavaDoc javaDoc) {
            super.visitCtJavaDoc(javaDoc);
        }

        @Override
        public void visitCtComment(CtComment comment) {
            super.visitCtComment(comment);
        }

        @Override
        public <T> void visitCtSuperAccess(CtSuperAccess<T> f) {
            super.visitCtSuperAccess(f);
        }

        @Override
        public <T> void visitCtFieldWrite(CtFieldWrite<T> fieldWrite) {
            super.visitCtFieldWrite(fieldWrite);
        }

        @Override
        public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
            super.visitCtFieldRead(fieldRead);
        }

        @Override
        public <T> void visitCtUnboundVariableReference(CtUnboundVariableReference<T> reference) {
            super.visitCtUnboundVariableReference(reference);
        }

        @Override
        public void visitCtCodeSnippetStatement(CtCodeSnippetStatement statement) {
            super.visitCtCodeSnippetStatement(statement);
        }

        @Override
        public <T> void visitCtCodeSnippetExpression(CtCodeSnippetExpression<T> expression) {
            super.visitCtCodeSnippetExpression(expression);
        }

        @Override
        public void visitCtWhile(CtWhile whileLoop) {
            super.visitCtWhile(whileLoop);
        }

        @Override
        public <T> void visitCtVariableWrite(CtVariableWrite<T> variableWrite) {
            super.visitCtVariableWrite(variableWrite);
        }

        @Override
        public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
            super.visitCtVariableRead(variableRead);
        }

        @Override
        public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
            super.visitCtUnaryOperator(operator);
        }

        @Override
        public <T> void visitCtTypeAccess(CtTypeAccess<T> typeAccess) {
            super.visitCtTypeAccess(typeAccess);
        }

        @Override
        public <T> void visitCtTypeReference(CtTypeReference<T> reference) {
            super.visitCtTypeReference(reference);
        }

        @Override
        public <T> void visitCtIntersectionTypeReference(CtIntersectionTypeReference<T> reference) {
            super.visitCtIntersectionTypeReference(reference);
        }

        @Override
        public void visitCtWildcardReference(CtWildcardReference wildcardReference) {
            super.visitCtWildcardReference(wildcardReference);
        }

        @Override
        public void visitCtTypeParameterReference(CtTypeParameterReference ref) {
            super.visitCtTypeParameterReference(ref);
        }

        @Override
        public void visitCtTry(CtTry tryBlock) {
            super.visitCtTry(tryBlock);
        }

        @Override
        public void visitCtThrow(CtThrow throwStatement) {
            super.visitCtThrow(throwStatement);
        }

        @Override
        public void visitCtSynchronized(CtSynchronized synchro) {
            super.visitCtSynchronized(synchro);
        }

        @Override
        public <T, S> void visitCtSwitchExpression(CtSwitchExpression<T, S> switchExpression) {
            super.visitCtSwitchExpression(switchExpression);
        }

        @Override
        public <R> void visitCtStatementList(CtStatementList statements) {
            super.visitCtStatementList(statements);
        }

        @Override
        public <R> void visitCtReturn(CtReturn<R> returnStatement) {
            super.visitCtReturn(returnStatement);
        }

        @Override
        public <T> void visitCtParameterReference(CtParameterReference<T> reference) {
            super.visitCtParameterReference(reference);
        }

        @Override
        public <T> void visitCtParameter(CtParameter<T> parameter) {
            super.visitCtParameter(parameter);
        }

        @Override
        public void visitCtPackageReference(CtPackageReference reference) {
            super.visitCtPackageReference(reference);
        }

        @Override
        public void visitCtPackage(CtPackage ctPackage) {
            super.visitCtPackage(ctPackage);
        }

        @Override
        public <T, A extends T> void visitCtOperatorAssignment(CtOperatorAssignment<T, A> assignment) {
            super.visitCtOperatorAssignment(assignment);
        }

        @Override
        public <T, E extends CtExpression<?>> void visitCtExecutableReferenceExpression(CtExecutableReferenceExpression<T, E> expression) {
            super.visitCtExecutableReferenceExpression(expression);
        }

        @Override
        public <T> void visitCtNewClass(CtNewClass<T> newClass) {
            super.visitCtNewClass(newClass);
        }

        @Override
        public <T> void visitCtConstructorCall(CtConstructorCall<T> ctConstructorCall) {
            super.visitCtConstructorCall(ctConstructorCall);
        }

        @Override
        public <T> void visitCtNewArray(CtNewArray<T> newArray) {
            super.visitCtNewArray(newArray);
        }

        @Override
        public <T> void visitCtAnnotationMethod(CtAnnotationMethod<T> annotationMethod) {
            super.visitCtAnnotationMethod(annotationMethod);
        }

        @Override
        public <T> void visitCtCatchVariableReference(CtCatchVariableReference<T> reference) {
            super.visitCtCatchVariableReference(reference);
        }

        @Override
        public <T> void visitCtCatchVariable(CtCatchVariable<T> catchVariable) {
            super.visitCtCatchVariable(catchVariable);
        }

        @Override
        public <T> void visitCtLocalVariableReference(CtLocalVariableReference<T> reference) {
            super.visitCtLocalVariableReference(reference);
        }

        @Override
        public void visitCtTextBlock(CtTextBlock literal) {
            super.visitCtTextBlock(literal);
        }

        @Override
        public <T> void visitCtLiteral(CtLiteral<T> literal) {
            super.visitCtLiteral(literal);
        }

        @Override
        public <T> void visitCtInterface(CtInterface<T> intrface) {
            super.visitCtInterface(intrface);
        }

        @Override
        public void visitCtIf(CtIf ifElement) {
            super.visitCtIf(ifElement);
        }

        @Override
        public void visitCtForEach(CtForEach foreach) {
            super.visitCtForEach(foreach);
        }

        @Override
        public <T> void visitCtFieldReference(CtFieldReference<T> reference) {
            super.visitCtFieldReference(reference);
        }

        @Override
        public <T> void visitCtAnnotationFieldAccess(CtAnnotationFieldAccess<T> annotationFieldAccess) {
            super.visitCtAnnotationFieldAccess(annotationFieldAccess);
        }

        @Override
        public <T> void visitCtThisAccess(CtThisAccess<T> thisAccess) {
            super.visitCtThisAccess(thisAccess);
        }

        @Override
        public <T> void visitCtEnumValue(CtEnumValue<T> enumValue) {
            super.visitCtEnumValue(enumValue);
        }

        @Override
        public <T> void visitCtField(CtField<T> f) {
            super.visitCtField(f);
        }

        @Override
        public <T> void visitCtExecutableReference(CtExecutableReference<T> reference) {
            super.visitCtExecutableReference(reference);
        }

        @Override
        public <T extends Enum<?>> void visitCtEnum(CtEnum<T> ctEnum) {
            super.visitCtEnum(ctEnum);
        }

        @Override
        public void visitCtDo(CtDo doLoop) {
            super.visitCtDo(doLoop);
        }

        @Override
        public void visitCtContinue(CtContinue continueStatement) {
            super.visitCtContinue(continueStatement);
        }

        @Override
        public <T> void visitCtConstructor(CtConstructor<T> c) {
            super.visitCtConstructor(c);
        }

        @Override
        public <T> void visitCtConditional(CtConditional<T> conditional) {
            super.visitCtConditional(conditional);
        }

        @Override
        public void visitCtTypeParameter(CtTypeParameter typeParameter) {
            super.visitCtTypeParameter(typeParameter);
        }

        @Override
        public <T> void visitCtClass(CtClass<T> ctClass) {
            super.visitCtClass(ctClass);
        }

        @Override
        public void visitCtCatch(CtCatch catchBlock) {
            super.visitCtCatch(catchBlock);
        }

        @Override
        public void visitCtBreak(CtBreak breakStatement) {
            super.visitCtBreak(breakStatement);
        }

        @Override
        public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
            super.visitCtBinaryOperator(operator);
        }

        @Override
        public <T, A extends T> void visitCtAssignment(CtAssignment<T, A> assignement) {
            super.visitCtAssignment(assignement);
        }

        @Override
        public <T> void visitCtAssert(CtAssert<T> asserted) {
            super.visitCtAssert(asserted);
        }

        @Override
        public <T> void visitCtArrayTypeReference(CtArrayTypeReference<T> reference) {
            super.visitCtArrayTypeReference(reference);
        }

        @Override
        public <T> void visitCtArrayWrite(CtArrayWrite<T> arrayWrite) {
            super.visitCtArrayWrite(arrayWrite);
        }

        @Override
        public <T> void visitCtArrayRead(CtArrayRead<T> arrayRead) {
            super.visitCtArrayRead(arrayRead);
        }

        @Override
        public <A extends Annotation> void visitCtAnnotationType(CtAnnotationType<A> annotationType) {
            super.visitCtAnnotationType(annotationType);
        }

        @Override
        public void scan(CtRole role, Object o) {
            super.scan(role, o);
        }

        @Override
        public void scan(Object o) {
            super.scan(o);
        }

        @Override
        public <A extends Annotation> void visitCtAnnotation(CtAnnotation<A> annotation) {
            super.visitCtAnnotation(annotation);
        }

        @Override
        public <T> void visitCtInvocation(CtInvocation<T> invocation) {
            if (invocation.getExecutable().getSimpleName().equals(fillMethodName)) {
                // Set arguments to the variables currently in scope
                List<CtVariableReference<?>> vars = new ArrayList<>(variablesInScope);
                List<CtExpression<?>> args = new ArrayList<>();
                // 添加当前代码的行号作为第一个参数
                if (showLineNumber) {
                    args.add(invocation.getFactory().Code().createLiteral(invocation.getPosition().getLine()));
                }
                for (CtVariableReference<?> varRef : vars) {
                    args.add(invocation.getFactory().Code().createVariableRead(varRef, false));
                }
                invocation.setArguments(args);
            }
            super.visitCtInvocation(invocation);
        }


        @Override
        public <T> void visitCtLambda(CtLambda<T> lambda) {
            backUpAndRecover(() -> {
                for (CtParameter<?> param : lambda.getParameters()) {
                    variablesInScope.add(param.getReference());
                }
                super.visitCtLambda(lambda);
            });
        }

        public void backUpAndRecover(Runnable run) {
            // 备份variablesInScope
            List<CtVariableReference<?>> variablesBeforeBlock = new ArrayList<>(variablesInScope);
            run.run();
            // 恢复variablesInScope
            variablesInScope = variablesBeforeBlock;
        }
    }
}
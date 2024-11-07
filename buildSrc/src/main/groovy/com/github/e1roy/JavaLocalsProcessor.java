package com.github.e1roy;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.CtScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 21/10/2014.
 */
public class JavaLocalsProcessor extends AbstractProcessor<CtMethod> {

    int count = 0;
    static String fillMethodName = "printLocals";

    public JavaLocalsProcessor() {
//        new RuntimeException().printStackTrace();
    }

    public void setFillMethodName(String fillMethodName) {
        this.fillMethodName = fillMethodName;
    }

    @Override
    public void process(CtMethod method) {
        count++;
        method.accept(new VariableCollectorVisitor());
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
        public <T> void visitCtInvocation(CtInvocation<T> invocation) {
            if (invocation.getExecutable().getSimpleName().equals(fillMethodName)) {
                // Set arguments to the variables currently in scope
                List<CtVariableReference<?>> vars = new ArrayList<>(variablesInScope);
                List<CtExpression<?>> args = new ArrayList<>();
                // 添加当前代码的行号作为第一个参数
                args.add(invocation.getFactory().Code().createLiteral(invocation.getPosition().getLine()));
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